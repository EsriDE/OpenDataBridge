package esride.opendatabridge.reader;

import esride.opendatabridge.itemtransform.*;
import esride.opendatabridge.reader.request.CatalogRequestObj;
import esride.opendatabridge.reader.request.CatalogResponseObj;
import esride.opendatabridge.reader.request.ICatalogRequest;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 11.05.13
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
public abstract class CatalogReader implements IReader {
    private static Logger sLogger = Logger.getLogger(CatalogReader.class);
    
    protected String maxRecordsId;
    protected String startPositionId;
    
    protected String catalogType;
    
    protected String catalogUrl;

    private HashMap<String, String> capabilitiesMapper;

    protected HashMap<String, String> templateItems = new HashMap<String, String>();
    protected HashMap<String, String> headerItems = new HashMap<String, String>();

    protected HashMap<String, IResource> resourceMap;

    protected IItemTransformer agolItemTransformer;
    
    protected ICatalogRequest searchRequest;

    protected String processId;

    public void setResourceMap(HashMap<String, IResource> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public void setCapabilitiesMapper(HashMap<String, String> capabilitiesMapper) {
        this.capabilitiesMapper = capabilitiesMapper;
    }

    public void setAgolItemTransformer(IItemTransformer agolItemTransformer) {
        this.agolItemTransformer = agolItemTransformer;
    }

    public void setSearchRequest(ICatalogRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public MetadataObjectResult getItemsFromCatalog(int startPos) throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CatalogReader: Harvesting and transformation process StartPosition is: " + startPos);

        int maxRecords = Integer.valueOf(templateItems.get(maxRecordsId));
        List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>();

        templateItems.put(startPositionId, String.valueOf(startPos));
        CatalogRequestObj reqObj = new CatalogRequestObj(catalogUrl,templateItems, headerItems);
        MetadataObjectResult result = new MetadataObjectResult();
        try {

            CatalogResponseObj responseObj = searchRequest.executeRequest(reqObj);

            if(responseObj.getMetadataDocuments() != null){
                metadataObjectList.addAll(responseObj.getMetadataDocuments());
            }
            result.setMetadataObjectList(metadataObjectList);
            int matchedRecords = responseObj.getNumbersOfRecordMatched();
            int returnedRecords = responseObj.getNumbersOfRecordReturned();
            sLogger.info("Number of matched records: " + matchedRecords);
            if(matchedRecords >= returnedRecords + startPos){
                int startPosition = startPos + maxRecords;
                sLogger.info("Next start position: " + startPosition);
                result.setHasNextStartPosition(true);
                result.setNextStartPosition(startPosition);
            }else{
                result.setHasNextStartPosition(false);
                result.setNextStartPosition(-1);
                //pagination = false;
                sLogger.info("No further request");
            }
        } catch (IOException e) {
            String message = "CatalogRequest failed at startPosition: " + startPos;
            sLogger.error(message, e);
            throw new ReaderException(message, e);
        }
        return result;
    }

    public TransformedItemResult getTramsformedItemsFromCatalog(int startPos) throws ReaderException{
        sLogger.info("------------------------------------------------ ");
        sLogger.info("Catalog-Reader: Start Harvesting and transformation process");

        //boolean pagination = true;
        int maxRecords = Integer.valueOf(templateItems.get(maxRecordsId));
        //int startPosition = 1;
        List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>();
        TransformedItemResult result = new TransformedItemResult();

        //while(pagination){
        templateItems.put(startPositionId, String.valueOf(startPos));
        CatalogRequestObj reqObj = new CatalogRequestObj(catalogUrl,templateItems, headerItems);
        try {

            CatalogResponseObj responseObj = searchRequest.executeRequest(reqObj);

            if(responseObj.getMetadataDocuments() != null){
                metadataObjectList.addAll(responseObj.getMetadataDocuments());
            }
            int matchedRecords = responseObj.getNumbersOfRecordMatched();
            int returnedRecords = responseObj.getNumbersOfRecordReturned();
            sLogger.info("Number of matched records: " + matchedRecords);
            if(matchedRecords >= returnedRecords + startPos){
                int startPosition = startPos + maxRecords;
                sLogger.info("Next start position: " + startPosition);
                result.setHasNextStartPosition(true);
                result.setNextStartPosition(startPosition);
            }else{
                result.setHasNextStartPosition(false);
                result.setNextStartPosition(-1);
            }
        } catch (IOException e) {
            String message = "CatalogRequest failed at startPosition: " + startPos;
            sLogger.error(message, e);
            throw new ReaderException(message, e);
        }

        //}

        if(sLogger.isDebugEnabled()){
            sLogger.debug("Number of objects: "  + metadataObjectList.size());
        }
        sLogger.info("Catalog-Reader: Harvesting Metadata from catalog with startPosition " + startPos + " finished");

        sLogger.info("Catalog-Reader: Get further metadata information from capabilities");
        List<MetadataObject> failureList = new ArrayList<MetadataObject>();
        List<TransformedItem> lTransformedItems = new ArrayList<TransformedItem>();

        for(int i=0; i<metadataObjectList.size(); i++){
            MetadataObject object = metadataObjectList.get(i);

            MetadataResource mdResource = new MetadataResource();
            mdResource.setResourceType(object.getResourceType());
            mdResource.addDoc(catalogType, object.getMetadataDoc());

            IResource resource = resourceMap.get(object.getResourceType().toLowerCase());
            boolean objectFailed = false;
            if(resource != null){
                try {
                    Document capabilitiesDoc = resource.getRecourceMetadata(object.getCapabilitiesUrl(), object.getResourceType());
                    String capabilitiesType = capabilitiesMapper.get(object.getResourceType().toLowerCase());

                    mdResource.addDoc(capabilitiesType, capabilitiesDoc);
                } catch (ResourceException e) {
                    sLogger.error("The Resource (" + object.getCapabilitiesUrl() + ") is not available. " +
                            "The metadataset with the file Identifier: " + object.getMetadataFileIdentifier() + " is removed from the list", e);
                    objectFailed = true;
                    failureList.add(object);
                }
            }

            if(!objectFailed){
                TransformedItem item = this.createTransformedItem(object.getMetadataFileIdentifier(), mdResource);
                if(item != null){
                    lTransformedItems.add(item);
                }
            }

        }
        result.setTransformedResultList(lTransformedItems);

        if(failureList.size() > 0){
            sLogger.info("Number of failure objects: "  + failureList.size());
            metadataObjectList.removeAll(failureList);
            sLogger.info("Please check the following urls. They are not accessible during the transformation");
            for (MetadataObject aFailureList : failureList) {
                sLogger.info(aFailureList.getCapabilitiesUrl());
            }
        }

        sLogger.info("------------------------------------------------ ");
        return result;
    }

    private TransformedItem createTransformedItem(String fileIdentifier, MetadataResource mdResource){
        TransformedItem item = null;

        try {
            HashMap<String, String> agolItems = agolItemTransformer.transform2AgolItem(mdResource, processId);
            item = new TransformedItem();
            item.setItemElements(agolItems);
            //item.setResourceUrl(metadataObjectList.get(i).getResourceUrl());

            if(sLogger.isDebugEnabled()){
                sLogger.debug("Item elements:---------------------");
                sLogger.debug("Item URL: "  + item.getResourceUrl());
                Set<String> keySet =  agolItems.keySet();
                Iterator<String> keyIter = keySet.iterator();
                while(keyIter.hasNext()){
                    String key = keyIter.next();
                    String value = agolItems.get(key);
                    if(value != null){
                        int valueLength = value.length();
                        if(valueLength > 150){
                            sLogger.debug("Item: " + key + ": Value: " + value.substring(0,150) + "...");
                        }else{
                            sLogger.debug("Item: " + key + ": Value: " + agolItems.get(key));
                        }
                    }

                }
            }

        } catch (ItemTransformationException e) {
            sLogger.error("The metadataset with the file Identifier: " + fileIdentifier + " cannot be transformed", e);
        } catch (ItemGenerationException e) {
            sLogger.error("The metadataset with the file Identifier: " + fileIdentifier + " cannot be transformed", e);
        }

        return item;
    }

    
}
