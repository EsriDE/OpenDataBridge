package esride.opendatabridge.reader.csw;



import esride.opendatabridge.itemtransform.*;
import esride.opendatabridge.reader.*;

import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.*;

/**
 * THe CSWReader implements the IReader interface. The CSWReader class collects metadata from CSW metadata catalogues
 * and transforms the metadata into an AGOL Item compatible data model. For running an CSWReader component the following resources must be
 * available:
 * - the <p>csw.url</p> (HTTP POST endpoint for the CSW GetRecords endpoint)
 * - 0..* <p>csw_request_getrecords_header_{*}</p> properties for overwriting the HTTP Header
 * - 0..* <p>csw_request_getrecords_template_{*}</p> placeholder for overwriting the GetRecords POST request
 * - 0..* <p>csw_response_xpath_{*}</p> XPath values if you don't use the ISO metadata model (like Dublin Core)
 *
 * The GetRecords request is build by an XML Template. The template could be injected from a classpath path.
 *
 * For the AGOL Item transformation an <p>itemelement_{*}.properties</p> files must be available. For each CSW Metadata resource
 * (like WMS, Shapefile, CSV file) one property file must be created.
 *
 * User: sma
 * Date: 05.04.13
 * Time: 15:02
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
    private IResource capabilitiesResource;
    
    private IItemTransformer agolItemTransformer;


    public void setGetRecordsRequest(CSWGetRecordsRequest getRecordsRequest) {
        this.getRecordsRequest = getRecordsRequest;
    }

    public void setCapabilitiesResource(IResource capabilitiesResource) {
        this.capabilitiesResource = capabilitiesResource;
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

        List<Integer> failureList = new ArrayList<Integer>();
        for(int i=0; i<metadataObjectList.size(); i++){
            MetadataObject object = metadataObjectList.get(i);
            if(object.getResourceUrl() != null || object.getResourceUrl().trim().length() > 0){
                try {
                    object.setCapabilitiesDoc(capabilitiesResource.getRecourceMetadata(object.getResourceUrl(), object.getMetadataResource()));
                } catch (ResourceException e) {
                    sLogger.error("The WMS (" + object.getResourceUrl() + ") is not available. " +
                            "The metadataset with the file Identifier: " + object.getMetadataFileIdentifier() + " is removed from the list", e);
                    failureList.add(i);
                }
            }
        }
        if(failureList.size() > 0){
            for(int k=0; k<failureList.size(); k++){
                metadataObjectList.remove(failureList.get(k));
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
            if(metadataObjectList.get(i).getCapabilitiesDoc() != null){
                MetadataSet capabilitiesSet = new MetadataSet();
                capabilitiesSet.setXmlDoc(metadataObjectList.get(i).getCapabilitiesDoc());
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

    public void setProperties(HashMap<String, String> properties, String processId) throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CSW-Modul: Prepare CSW Module. Set Module properties");
        
        cswUrl = properties.get("csw.url");
        if(cswUrl == null || cswUrl.trim().length() == 0){
            throw new ReaderException("The property csw.url is missing");
        }
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

        //sLogger.info("CSW-Modul: Prepare CSW Module (CSWGetRecordsResponse). Set XPath Values");
        //getRecordsRequest.getGetRecordsResponse().setXPathValues(xPathItems);

        sLogger.info("CSW-Modul: Prepare CSW Module (GetRecordsRequestTemplate). Set XPath Values");
        try {
            getRecordsRequest.getRequestTemplate().setGetRecordsTemplate(processId);
        } catch (IOException e) {
            throw new ReaderException("Cannot load template for the getRecords request", e);
        }

        this.processId = processId;

    }
}
