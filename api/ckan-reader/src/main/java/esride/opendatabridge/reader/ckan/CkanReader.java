package esride.opendatabridge.reader.ckan;

import esride.opendatabridge.itemtransform.*;
import esride.opendatabridge.reader.*;
import esride.opendatabridge.reader.request.CatalogRequestObj;
import esride.opendatabridge.reader.request.CatalogResponseObj;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * THe CkanReader implements the IReader interface. The CkanReader class collects metadata from Ckan Open Data catalogues
 * and transforms the metadata into an AGOL Item compatible data model. For running an CkanReader component the following resources must be
 * available:
 * - the <p>ckan.url</p> (HTTP GET endpoint for the Ckan catalogue endpoint)
 * - 0..* <p>ckan_request_search_header__{*}</p> properties for overwriting the HTTP Header
 * - 0..* <p>ckan_request_search_param__{*}</p> the request parameters for the select statement
 * - 0..* <p>csw_response_xpath_{*}</p> XPath values for using a different domain model
 *
 * For the AGOL Item transformation an <p>itemelement_{*}.properties</p> files must be available. For each Ckan Metadata resource
 * (like WMS, Shapefile, CSV file) one property file must be created.
 *
 * User: sma
 * Date: 23.04.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class CkanReader implements IReader, IReaderFactory {
    private static Logger sLogger = Logger.getLogger(CkanReader.class);

    private HashMap<String, String> templateItems = new HashMap<String, String>();
    private HashMap<String, String> headerItems = new HashMap<String, String>();
    private Properties propertyItems = new Properties();
    
    private String ckanUrl;

    private String processId;

    private CkanSearchRequest searchRequest;

    private IItemTransformer agolItemTransformer;

    private HashMap<String, IResource> resourceMap;

    public void setAgolItemTransformer(IItemTransformer agolItemTransformer) {
        this.agolItemTransformer = agolItemTransformer;
    }

    public void setResourceMap(HashMap<String, IResource> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public CkanSearchRequest getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(CkanSearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<TransformedItem> getItemsFromCatalog() throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("Ckan-Modul: Start Requesting Metadata from ckan-catalog");

        boolean pagination = true;
        int maxRecords = Integer.valueOf(templateItems.get("limit"));
        int startPosition = 0;
        List<MetadataObject> metadataObjectList = new ArrayList<MetadataObject>();

        while(pagination){
            sLogger.info("Ckan-Modul: Start request with startPosition: " + startPosition);
            templateItems.put("offset", String.valueOf(startPosition));
            CatalogRequestObj reqObj = new CatalogRequestObj(ckanUrl,templateItems, headerItems);
            try {
                CatalogResponseObj responseObj = searchRequest.executeCkanRequest(reqObj);
                if(responseObj.getMetadataDocuments() != null){
                    metadataObjectList.addAll(responseObj.getMetadataDocuments());
                }
                int matchedRecords = responseObj.getNumbersOfRecordMatched();
                int returnedRecords = responseObj.getNumbersOfRecordReturned();
                sLogger.info("Ckan-Modul: Number of matched records: " + matchedRecords);
                if(matchedRecords > returnedRecords + startPosition){
                    startPosition = startPosition + maxRecords;
                    sLogger.info("Ckan-Modul: Next start position: " + startPosition);
                }else{
                    pagination = false;
                    sLogger.info("Ckan-Modul: No further request");
                }
            } catch (IOException e) {
                String message = "Ckan Request failed at startPosition: " + startPosition;
                sLogger.error(message, e);
                throw new ReaderException(message, e);
            }

        }

        List<Integer> failureList = new ArrayList<Integer>();
        for(int i=0; i<metadataObjectList.size(); i++){
            MetadataObject object = metadataObjectList.get(i);
            IResource resource = resourceMap.get(object.getResourceType().toLowerCase());
            if(resource != null){
                try {
                    object.setCapabilitiesDoc(resource.getRecourceMetadata(object.getResourceUrl(), object.getResourceType()));
                } catch (ResourceException e) {
                    sLogger.error("The Resource (" + object.getResourceUrl() + ") is not available. " +
                            "The metadataset with the file Identifier: " + object.getMetadataFileIdentifier() + " is removed from the list", e);
                    failureList.add(i);
                }
            }

        }
        if(failureList.size() > 0){
            for(int k=0; k<failureList.size(); k++){
                metadataObjectList.remove(failureList.get(k).intValue());
            }

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
            cswSet.setMetadataType("ckan");
            setList.add(cswSet);
            if(metadataObjectList.get(i).getCapabilitiesDoc() != null){
                MetadataSet capabilitiesSet = new MetadataSet();
                capabilitiesSet.setXmlDoc(metadataObjectList.get(i).getCapabilitiesDoc());
                capabilitiesSet.setMetadataType("capabilities");
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


        sLogger.info("Ckan-Modul: Requesting Metadata from catalog finished");
        sLogger.info("------------------------------------------------ ");
        return lTransformedItems;

    }

    public void setProperties(HashMap<String, String> properties, String processId) throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("Ckan-Modul: Prepare Ckan Module. Set Module properties");

        ckanUrl = properties.get("ckan.url");
        if(ckanUrl == null || ckanUrl.trim().length() == 0){
            throw new ReaderException("The property ckan.url is missing");
        }
        sLogger.info("Module property: ckan.url=" + ckanUrl);

        Set<String> keySet = properties.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            String value = properties.get(key);

            sLogger.info("Module property:" + key + "=" + value);
            if(key.startsWith("ckan_request_search_header_")){
                headerItems.put(key, value);
            }
            if(key.startsWith("ckan_request_search_param_")){
                templateItems.put(key.substring(26), value);
            }
            if(key.startsWith("csw_response_xpath_")){
                propertyItems.put(key, value);
            }
        }

        if(propertyItems.size() > 0){
            sLogger.info("Ckan-Modul: Prepare Ckan Module (SearchRequest). Overwrite XPath Values");
            searchRequest.getSearchResponse().setXpathValue(propertyItems);
        }

        this.processId = processId;
    }
}
