package esride.opendatabridge.reader.csw;



import esride.opendatabridge.itemtransform.*;
import esride.opendatabridge.reader.*;
import esride.opendatabridge.reader.capabilities.OGCCapabilitiesRequest;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 05.04.13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class CSWReader implements IReader, IReaderFactory {
    private static Logger sLogger = Logger.getLogger(CSWReader.class);
    
    private HashMap<String, String> templateItems = new HashMap<String, String>();
    private HashMap<String, String> headerItems = new HashMap<String, String>();
    private HashMap<String, String> xPathItems = new HashMap<String, String>();
    
    private String cswUrl = null;
    private String processId;
    //private String httpMethod = null;
    

    
    private CSWGetRecordsRequest getRecordsRequest;

    private OGCCapabilitiesRequest capabilitiesRequest;
    
    private IItemTransformer agolItemTransformer;


    public void setGetRecordsRequest(CSWGetRecordsRequest getRecordsRequest) {
        this.getRecordsRequest = getRecordsRequest;
    }

    public void setCapabilitiesRequest(OGCCapabilitiesRequest capabilitiesRequest) {
        this.capabilitiesRequest = capabilitiesRequest;
    }

    public void setAgolItemTransformer(IItemTransformer agolItemTransformer) {
        this.agolItemTransformer = agolItemTransformer;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public ReaderItems getItemsFromCatalog() throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CSW-Modul: Start Requesting Metadata from catalog");


        boolean pagination = true;
        int maxRecords = Integer.valueOf(templateItems.get("csw_request_getrecords_template_maxRecords"));
        int startPosition = 1;
        List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>();

        while(pagination){
            templateItems.put("csw_request_getrecords_template_startPosition", String.valueOf(startPosition));
            CSWRequestObj reqObj = new CSWRequestObj(cswUrl,templateItems, headerItems);
            try {
                CSWResponseObj resonseObj = getRecordsRequest.executeGetRecordsRequest(reqObj);
                metadataObjectList.addAll(resonseObj.getMetadataDocuments());
                int matchedRecords = resonseObj.getNumbersOfRecordMatched();
                int returnedRecords = resonseObj.getNumbersOfRecordReturned();
                if(matchedRecords >= returnedRecords + startPosition){
                    startPosition = startPosition + maxRecords;
                }else{
                    pagination = false;
                }
            } catch (IOException e) {
                String message = "CSW Request failed at startPosition: " + startPosition;
                sLogger.error(message, e);
                throw new ReaderException(message, e);
            }

        }
        
        //Besonderheit, nun werden die WMS Capabilities herausgeholt

        for(int i=0; i<metadataObjectList.size(); i++){
            MetadataObject object = metadataObjectList.get(i);
            if(object.getResourceUrl() != null || object.getResourceUrl().trim().length() > 0){
                try {
                    object.setOgcCapabilitiesDoc(capabilitiesRequest.getCapabilitiesDocument(object.getResourceUrl()));
                } catch (IOException e) {
                    sLogger.error("The WMS (" + object.getResourceUrl() + ") is not available. " +
                            "The metadataset with the file Identifier: " + object.getMetadataFileIdentifier() + " is removed from the list", e);
                    metadataObjectList.remove(object);
                } catch (SAXException e) {
                    sLogger.error("The WMS (" + object.getResourceUrl() + ") is not available. " +
                            "The metadataset with the file Identifier: " + object.getMetadataFileIdentifier() + " is removed from the list", e);
                    metadataObjectList.remove(object);
                }
            }
        }

        //agolitems erzeugen
        ReaderItems readerItems = new ReaderItems();
        for(int i=0;i<metadataObjectList.size(); i++){
            MetadataResource resource = new MetadataResource();
            resource.setResourceType(metadataObjectList.get(i).getMetadataResource());
            List<MetadataSet> setList = new ArrayList<MetadataSet>();
            MetadataSet cswSet = new MetadataSet();
            cswSet.setXmlDoc(metadataObjectList.get(i).getCswMetadataDoc());
            cswSet.setMetadataType("csw");
            setList.add(cswSet);
            if(metadataObjectList.get(i).getOgcCapabilitiesDoc() != null){
                MetadataSet capabilitiesSet = new MetadataSet();
                capabilitiesSet.setXmlDoc(metadataObjectList.get(i).getOgcCapabilitiesDoc());
                capabilitiesSet.setMetadataType("capabilities");
                setList.add(capabilitiesSet);
            }
            resource.setContainer(setList);
            try {
                HashMap agolItems = agolItemTransformer.transform2AgolItem(resource, processId);
                ReaderItem item = new ReaderItem();
                item.setItemElements(agolItems);
                item.setResourceUrl(metadataObjectList.get(i).getResourceUrl());
                readerItems.addItem(item);

            } catch (ItemTransformationException e) {
                sLogger.error("The metadataset with the file Identifier: " + metadataObjectList.get(i).getMetadataFileIdentifier() + " cannot be transformed", e);
            } catch (ItemGenerationException e) {
                sLogger.error("The metadataset with the file Identifier: " + metadataObjectList.get(i).getMetadataFileIdentifier() + " cannot be transformed", e);
            }
        }
        
        

       return readerItems;
    }

    public void setProperties(HashMap<String, String> properties, String processId) {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CSW-Modul: Prepare CSW Module. Set Module properties");
        
        cswUrl = properties.get("csw.url");
        sLogger.info("Module property: csw.url=" + cswUrl);
        /*httpMethod = properties.get("csw_request_method");
        sLogger.info("Module property: csw_request_method=" + httpMethod);*/
        
        Set<String> keySet = properties.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            String value = properties.get(key);

            sLogger.info("Module property:" + key + "=" + value);
            if(key.startsWith("csw_request_getrecords_header_")){
                headerItems.put(key, value);
            }
            if(key.startsWith("csw_request_getrecords_template_")){
                templateItems.put(key, value);
            }
            if(key.startsWith("csw_response_xpath_")){
                xPathItems.put(key, value);
            }
        }

        sLogger.info("CSW-Modul: Prepare CSW Module (CSWGetRecordsResponse). Set XPath Values");
        getRecordsRequest.getGetRecordsResponse().setXPathValues(xPathItems);

        sLogger.info("CSW-Modul: Prepare CSW Module (GetRecordsRequestGenerator). Set XPath Values");
        getRecordsRequest.getRequestGenerator().setGetRecordsTemplate(this.getClass().getResourceAsStream("/templates/" + processId + ".xml"));
                
        this.processId = processId;

    }
}
