package esride.opendatabridge.reader.csw;



import esride.opendatabridge.itemtransform.*;
import esride.opendatabridge.reader.*;

import esride.opendatabridge.reader.request.CatalogRequestObj;
import esride.opendatabridge.reader.request.CatalogResponseObj;
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
 * - 0..* <p>csw_response_xpath_{*}</p>XPath values for using a different domain model (like Dublin Core)
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
    private Properties propertyItems = new Properties();
    //private HashMap<String, String> xPathItems = new HashMap<String, String>();
    
    private String cswUrl = null;
    private String processId;
    //private String httpMethod = null;
    

    
    private CSWGetRecordsRequest getRecordsRequest;
    
    private HashMap<String, IResource> resourceMap;
    
    private IItemTransformer agolItemTransformer;
    
    private HashMap<String, String> capabilitiesMapper;


    public void setGetRecordsRequest(CSWGetRecordsRequest getRecordsRequest) {
        this.getRecordsRequest = getRecordsRequest;
    }

    public void setResourceMap(HashMap<String, IResource> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public void setAgolItemTransformer(IItemTransformer agolItemTransformer) {
        this.agolItemTransformer = agolItemTransformer;
    }

    public void setCapabilitiesMapper(HashMap<String, String> capabilitiesMapper) {
        this.capabilitiesMapper = capabilitiesMapper;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<TransformedItem> getItemsFromCatalog() throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CSW-Modul: Start Requesting Metadata from catalog");


        boolean pagination = true;
        int maxRecords = Integer.valueOf(templateItems.get("csw_request_getrecords_template_maxRecords"));
        int startPosition = 1;
        List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>();

        while(pagination){
            templateItems.put("csw_request_getrecords_template_startPosition", String.valueOf(startPosition));
            CatalogRequestObj reqObj = new CatalogRequestObj(cswUrl,templateItems, headerItems);
            try {
                CatalogResponseObj responseObj = getRecordsRequest.executeGetRecordsRequest(reqObj);

                if(responseObj.getMetadataDocuments() != null){
                    metadataObjectList.addAll(responseObj.getMetadataDocuments());
                }
                int matchedRecords = responseObj.getNumbersOfRecordMatched();
                int returnedRecords = responseObj.getNumbersOfRecordReturned();
                sLogger.info("CSW-Modul: Number of matched records: " + matchedRecords);
                if(matchedRecords >= returnedRecords + startPosition){
                    startPosition = startPosition + maxRecords;
                    sLogger.info("CSW-Modul: Next start position: " + startPosition);
                }else{
                    pagination = false;
                    sLogger.info("CSW-Modul: No further request");
                }
            } catch (IOException e) {
                String message = "CSW Request failed at startPosition: " + startPosition;
                sLogger.error(message, e);
                throw new ReaderException(message, e);
            }

        }

        if(sLogger.isDebugEnabled()){
            sLogger.debug("Number of objects: "  + metadataObjectList.size());
        }


        List<MetadataObject> failureList = new ArrayList<MetadataObject>();
        for(int i=0; i<metadataObjectList.size(); i++){
            MetadataObject object = metadataObjectList.get(i);
            IResource resource = resourceMap.get(object.getResourceType().toLowerCase());
            if(resource != null){
                try {
                    object.setCapabilitiesDoc(resource.getRecourceMetadata(object.getResourceUrl(), object.getResourceType()));
                    object.setCapabilitiesType(capabilitiesMapper.get(object.getResourceType().toLowerCase()));
                } catch (ResourceException e) {
                    sLogger.error("The Resource (" + object.getResourceUrl() + ") is not available. " +
                            "The metadataset with the file Identifier: " + object.getMetadataFileIdentifier() + " is removed from the list", e);
                    failureList.add(object);
                }
            }


        }
        if(sLogger.isDebugEnabled()){
            sLogger.debug("Number of failure objects: "  + failureList.size());
        }
        if(failureList.size() > 0){
            metadataObjectList.removeAll(failureList);
        }
        if(sLogger.isDebugEnabled()){
            sLogger.debug("Number of cleaned objects: "  + metadataObjectList.size());
        }


        //agolitems erzeugen
        //ReaderItems lTransformedItems = new ReaderItems();
        List<TransformedItem> lTransformedItems = new ArrayList<TransformedItem>();
        for(int i=0;i<metadataObjectList.size(); i++){
            MetadataResource resource = new MetadataResource();
            resource.setResourceType(metadataObjectList.get(i).getResourceType());
            List<MetadataSet> setList = new ArrayList<MetadataSet>();
            MetadataSet cswSet = new MetadataSet();
            cswSet.setXmlDoc(metadataObjectList.get(i).getMetadataDoc());
            cswSet.setMetadataType("csw");
            setList.add(cswSet);
            if(metadataObjectList.get(i).getCapabilitiesDoc() != null){
                MetadataSet capabilitiesSet = new MetadataSet();
                capabilitiesSet.setXmlDoc(metadataObjectList.get(i).getCapabilitiesDoc());
                capabilitiesSet.setMetadataType(metadataObjectList.get(i).getCapabilitiesType());
                setList.add(capabilitiesSet);
            }
            resource.setContainer(setList);
            try {
                HashMap<String, String> agolItems = agolItemTransformer.transform2AgolItem(resource, processId);
                TransformedItem item = new TransformedItem();
                item.setItemElements(agolItems);
                item.setResourceUrl(metadataObjectList.get(i).getResourceUrl());
                lTransformedItems.add(item);
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("Item elements:---------------------");
                    sLogger.debug("Item URL: "  + item.getResourceUrl());
                    Set<String> keySet =  agolItems.keySet();
                    Iterator<String> keyIter = keySet.iterator();
                    while(keyIter.hasNext()){
                        String key = keyIter.next();
                        String value = agolItems.get(key);
                        int valueLength = value.length();
                        if(valueLength > 150){
                            sLogger.debug("Item: " + key + ": Value: " + value.substring(0,150) + "...");
                        }else{
                            sLogger.debug("Item: " + key + ": Value: " + agolItems.get(key));
                        }

                    }
                }

            } catch (ItemTransformationException e) {
                sLogger.error("The metadataset with the file Identifier: " + metadataObjectList.get(i).getMetadataFileIdentifier() + " cannot be transformed", e);
            } catch (ItemGenerationException e) {
                sLogger.error("The metadataset with the file Identifier: " + metadataObjectList.get(i).getMetadataFileIdentifier() + " cannot be transformed", e);
            }

            
        }
       
       sLogger.info("CSW-Modul: Requesting Metadata from catalog finished");
       sLogger.info("------------------------------------------------ ");
       return lTransformedItems;
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
            if(key.startsWith("csw.reader.")){
                propertyItems.put(key, value);
            }
        }

        if(propertyItems.size() > 0){
            sLogger.info("CSW-Modul: Prepare CSW Module (CSWGetRecordsResponse). Overwrite XPath Values");
            getRecordsRequest.getGetRecordsResponse().setXpathValue(propertyItems);
        }

        sLogger.info("CSW-Modul: Prepare CSW Module (GetRecordsRequestTemplate). Set XPath Values");
        try {
            getRecordsRequest.getRequestTemplate().setGetRecordsTemplate(processId);
        } catch (IOException e) {
            throw new ReaderException("Cannot load template for the getRecords request", e);
        }

        this.processId = processId;

    }
}
