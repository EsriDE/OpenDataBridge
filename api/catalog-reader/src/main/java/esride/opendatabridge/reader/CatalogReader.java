package esride.opendatabridge.reader;

import esride.opendatabridge.itemtransform.*;
import esride.opendatabridge.reader.request.CatalogRequestObj;
import esride.opendatabridge.reader.request.CatalogResponseObj;
import esride.opendatabridge.reader.request.ICatalogRequest;
import org.apache.log4j.Logger;

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

    public List<TransformedItem> getItemsFromCatalog() throws ReaderException{
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CatalogReader: Start Harvesting and transformation process");

        sLogger.info("Catalog-Reader: Start Harvesting Metadata from catalog");
        boolean pagination = true;
        int maxRecords = Integer.valueOf(templateItems.get(maxRecordsId));
        int startPosition = 1;
        List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>();

        while(pagination){
            templateItems.put(startPositionId, String.valueOf(startPosition));
            CatalogRequestObj reqObj = new CatalogRequestObj(catalogUrl,templateItems, headerItems);
            try {
                
                CatalogResponseObj responseObj = searchRequest.executeRequest(reqObj);

                if(responseObj.getMetadataDocuments() != null){
                    metadataObjectList.addAll(responseObj.getMetadataDocuments());
                }
                int matchedRecords = responseObj.getNumbersOfRecordMatched();
                int returnedRecords = responseObj.getNumbersOfRecordReturned();
                sLogger.info("Number of matched records: " + matchedRecords);
                if(matchedRecords >= returnedRecords + startPosition){
                    startPosition = startPosition + maxRecords;
                    sLogger.info("Next start position: " + startPosition);
                }else{
                    pagination = false;
                    sLogger.info("No further request");
                }
            } catch (IOException e) {
                String message = "CatalogRequest failed at startPosition: " + startPosition;
                sLogger.error(message, e);
                throw new ReaderException(message, e);
            }

        }

        if(sLogger.isDebugEnabled()){
            sLogger.debug("Number of objects: "  + metadataObjectList.size());
        }
        sLogger.info("Catalog-Reader: Harvesting Metadata from catalog finished");

        sLogger.info("Catalog-Reader: Get further metadata information from capabilities");
        List<MetadataObject> failureList = new ArrayList<MetadataObject>();
        for(int i=0; i<metadataObjectList.size(); i++){
            MetadataObject object = metadataObjectList.get(i);
            IResource resource = resourceMap.get(object.getResourceType().toLowerCase());
            if(resource != null){
                try {
                    object.setCapabilitiesDoc(resource.getRecourceMetadata(object.getCapabilitiesUrl(), object.getResourceType()));
                    object.setCapabilitiesType(capabilitiesMapper.get(object.getResourceType().toLowerCase()));
                } catch (ResourceException e) {
                    sLogger.error("The Resource (" + object.getCapabilitiesUrl() + ") is not available. " +
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
        sLogger.info("Catalog-Reader: Get further metadata information from capabilities finished");

        sLogger.info("Catalog-Reader: Transform metadata information into Agol Item format");
        List<TransformedItem> lTransformedItems = new ArrayList<TransformedItem>();
        for(int i=0;i<metadataObjectList.size(); i++){
            MetadataResource resource = new MetadataResource();
            resource.setResourceType(metadataObjectList.get(i).getResourceType());
            List<MetadataSet> setList = new ArrayList<MetadataSet>();
            MetadataSet cswSet = new MetadataSet();
            cswSet.setXmlDoc(metadataObjectList.get(i).getMetadataDoc());
            cswSet.setMetadataType(catalogType);
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
                //item.setResourceUrl(metadataObjectList.get(i).getResourceUrl());
                lTransformedItems.add(item);
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
                sLogger.error("The metadataset with the file Identifier: " + metadataObjectList.get(i).getMetadataFileIdentifier() + " cannot be transformed", e);
            } catch (ItemGenerationException e) {
                sLogger.error("The metadataset with the file Identifier: " + metadataObjectList.get(i).getMetadataFileIdentifier() + " cannot be transformed", e);
            }


        }
        sLogger.info("Catalog-Reader: Transform metadata information into Agol Item format finished");

        if(failureList.size() > 0){
            sLogger.info("Please check the following urls. They are not accessible during the transformation");
            for (MetadataObject aFailureList : failureList) {
                sLogger.info(aFailureList.getCapabilitiesUrl());
            }
        }
        sLogger.info("CatalogReader: Harvesting and transformation process finished");
        sLogger.info("------------------------------------------------ ");
        return lTransformedItems;
    }

    
}
