package esride.opendatabridge.processing;


import esride.opendatabridge.agolwriter.*;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;
import esride.opendatabridge.reader.TransformedItem;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.*;


/**
 * The Transformer class controls the whole transformation process.
 * User: gvs
 * Date: 06.03.13
 * Time: 15:15
 */
public class Transformer {
    
    private static Logger sLogger = Logger.getLogger(Transformer.class);

    public void executeProcessDeleteDuplicate(IAgolService agolService, String searchString, String accessType, String ownerType)throws TransformerException {
        Map<String, ArrayList<AgolItem>> itemMap = null;
        try {
            itemMap =  agolService.searchItems(searchString, OwnerType.valueOf(ownerType));
        } catch (IOException e) {
            sLogger.error("Exception during ArcGIS Online Search", e);
            throw new TransformerException("Exception during ArcGIS Online Search", e);
        } catch (AgolItemInvalidException e) {
            sLogger.error("Exception during ArcGIS Online Search", e);
            throw new TransformerException("Exception during ArcGIS Online Search", e);
        }

        HashMap<String, ArrayList<DeleteItem>> titleList = new HashMap<String, ArrayList<DeleteItem>>();
        if(itemMap != null){
            Set<String> keys = itemMap.keySet();
            Iterator<String> keyIter = keys.iterator();
            while(keyIter.hasNext()){
                String key = keyIter.next();
                ArrayList<AgolItem> agolList = itemMap.get(key);
                for(int i=0; i<agolList.size(); i++){
                    AgolItem agolItem = agolList.get(i);
                    String title = agolItem.getAttributes().get("title");
                    DeleteItem delitem = new DeleteItem();
                    delitem.setId(agolItem.getAttributes().get("id"));
                    delitem.setUrl(agolItem.getAttributes().get("url"));
                    delitem.setThumbnailUrl(agolItem.getAttributes().get("thumbnail"));
                    delitem.setTitle(title);
                    if(titleList.containsKey(title)){
                        titleList.get(title).add(delitem);

                    }else{
                        ArrayList<DeleteItem> arrayList = new ArrayList<DeleteItem>();
                        arrayList.add(delitem);
                        titleList.put(title, arrayList);
                    }
                }
            }
        }

        //iterate through double entries
        Set<String> titleKeys = titleList.keySet();
        Iterator<String> titleKeyItr = titleKeys.iterator();
        while(titleKeyItr.hasNext()){
            String key = titleKeyItr.next();
            int arraySize = titleList.get(key).size();
            if(arraySize > 1){
                ArrayList<DeleteItem> realDeleteList = new ArrayList<DeleteItem>();




                //Hier wird nun entschieden wer rausfliegt (aber wie??)
                sLogger.info("---------------------------------------------------");
                ArrayList<DeleteItem> deleteList =  titleList.get(key);
                for(int k=0; k<deleteList.size(); k++){
                    DeleteItem delItem2 = deleteList.get(k);
                    if(delItem2.getThumbnailUrl() == null){
                        realDeleteList.add(delItem2);
                    }
                    sLogger.info("URL: " + delItem2.getUrl());
                    sLogger.info("ID: " + delItem2.getId());
                    sLogger.info("Title: " + delItem2.getTitle());
                    sLogger.info("Thumbnail: " + delItem2.getThumbnailUrl());
                }

                if(realDeleteList.size() == deleteList.size()){
                    realDeleteList.remove(0);
                }else{
                    sLogger.info("Thumbnail found;");
                }


                for(int j=0; j<realDeleteList.size(); j++){
                    String id = null;
                    try {
                        id = realDeleteList.get(j).getId();
                        agolService.deleteItems(id);
                    } catch (AgolPublishBatchPartlyFailedException e) {
                        sLogger.error("ID Delete failed" + id);
                    }
                }

            }
        }





        //in einer Liste werden nun die gleichen Titel gesammelt

    }

    /**
     * This method executes the transformation process. The transformation process selects the items from ArcGIS Online to check them, if they have to be updates
     * or delete, if the deleteStrategy parameter is true. After that, the items from the catalog (ckan, csw, etc...) would be transformed and inserted/updated in ArcGIS Online
     * @param reader the reader adapter (csw, ckan, etc...)
     * @param agolService the ArcGIS Online adapter
     * @param deleteStrategy if deleteStrategy=false, no agolItem would be deleted if no corresponding catalogItem exists
     * @param overwriteAccessType if overwriteShareType=true, the submitted accessType will be used and overwrite the existing one in an update situation
     * @param searchString the query statement for the Agol service query
     * @param accessType the flag for the access type. Allowed values are PRIVATE, SHARED, ORG, PUBLIC
     * @param ownerType the flag for the owner type. Allowed values are  USER, ORG
     * @throws TransformerException if the transformation process failed
     */
    public void executeProcessTransformation(IReader reader, IAgolService agolService, 
                                             boolean deleteStrategy, boolean overwriteAccessType,
                                             String searchString, String accessType, String ownerType) throws TransformerException {
        HashMap<String, ItemContainer> itemContainer = new HashMap<String, ItemContainer>();
        Map<String, ArrayList<AgolItem>> itemMap = null;
        try {
            itemMap =  agolService.searchItems(searchString, OwnerType.valueOf(ownerType));
        } catch (IOException e) {
            sLogger.error("Exception during ArcGIS Online Search", e);
            throw new TransformerException("Exception during ArcGIS Online Search", e);
        } catch (AgolItemInvalidException e) {
            sLogger.error("Exception during ArcGIS Online Search", e);
            throw new TransformerException("Exception during ArcGIS Online Search", e);
        }
        
        if(itemMap != null){
            Set<String> keys = itemMap.keySet();
            Iterator<String> keyIter = keys.iterator();
            while(keyIter.hasNext()){
                String key = keyIter.next();
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("URL from ArcGIS Online: " + key);
                }
                ItemContainer elem = new ItemContainer();
                elem.setAgolItems(itemMap.get(key));
                itemContainer.put(key,elem);
            }
        }

        //hole die Items vom Katalog
        List<TransformedItem> transformedItemList = null;
        try {
            transformedItemList = reader.getItemsFromCatalog();
        } catch (ReaderException e) {
            sLogger.error("Exception during Catalog Search", e);
            throw new TransformerException("Exception during Catalog Search", e);
        }

        if(transformedItemList != null){
            int listSize = transformedItemList.size();
            for(int i=0; i<listSize; i++){
                TransformedItem item = transformedItemList.get(i);
                ItemContainer elem;

                //try is ? or without ?
                String url1 = item.getResourceUrl();
                String url2;
                if(url1.charAt(url1.length() -1) == '?'){
                    url2 =  url1.substring(0, url1.length() -1);
                }else{
                    url2 = url1;
                }


                if(itemContainer.containsKey(url1)){
                    elem = itemContainer.get(url1);
                } else if(itemContainer.containsKey(url2)){
                    elem = itemContainer.get(url2);
                }
                else{
                    elem = new ItemContainer();
                    itemContainer.put(url2, elem);
                }
                elem.addCatalogItem(item);                                                
                
            }
        }

        Set<String> keySet = itemContainer.keySet();
        Iterator<String> iter = keySet.iterator();

        sLogger.info("Sorting " + itemContainer.size() + " items for insert, update and delete");
        List<AgolItem> insertList = new ArrayList<AgolItem>();
        List<AgolItem> updateList = new ArrayList<AgolItem>();
        List<AgolItem> deleteList = new ArrayList<AgolItem>();
        
        while(iter.hasNext()){
            String key = iter.next();
            ItemContainer containerElement = itemContainer.get(key);
            List<ItemTransaction> itemTransList = containerElement.getItemsForPublishing();
            for(ItemTransaction itemTrans : itemTransList){
                int status = itemTrans.getTransactionStatus();
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("Item Container. URL: " + key + " and Transaction Status: " + status);
                }
                switch (status){
                    case 1:
                        AgolItem agolItem = null;
                        try {
                            agolItem = agolService.createAgolItem(itemTrans.getTransformedItem().getItemElements());
                            insertList.add(agolItem);
                        } catch (AgolItemInvalidException e) {
                            sLogger.warn("Some transactions failed. See Logfile for more details");
                        }
                        break;
                    case 2:
                        AgolItem newAgolItem = null;
                        try {
                            newAgolItem = agolService.createAgolItem(itemTrans.getTransformedItem().getItemElements());
                            AgolItem updateAgolItem = agolService.mergeAgolItems(itemTrans.getAgolItem(), newAgolItem);
                            updateList.add(updateAgolItem);
                        } catch (AgolItemInvalidException e) {
                            sLogger.warn("Some transactions failed. See Logfile for more details");
                        }
                        break;
                    case 3:
                        deleteList.add(itemTrans.getAgolItem());
                        break;
                    default:
                        sLogger.warn("Wrong status number: " + status);
                }
                
            }
        }

        sLogger.info("Number of insert items: " + insertList.size());
        sLogger.info("Number of update items: " + updateList.size());
        sLogger.info("Number of delete items: " + deleteList.size() + ". Delete option enabled: " + deleteStrategy);

        try {
            if(insertList.size()>0){
                agolService.addItems(insertList, AccessType.valueOf(accessType));
            }
            if(overwriteAccessType){
                if(updateList.size() > 0){
                    agolService.updateItems(updateList, AccessType.valueOf(accessType), "");
                }
            }else{
                if(updateList.size() > 0){
                    agolService.updateItems(updateList);
                }
            }
            if(deleteStrategy && deleteList.size() > 0){
                agolService.deleteItems(deleteList);
            }
        } catch (AgolPublishBatchPartlyFailedException e) {
            List<String> failureList = e.getFailureList();
            if(failureList != null && failureList.size() > 0){
                sLogger.warn("Some transactions failed.");
                for(int i=0; i<failureList.size(); i++){
                    sLogger.warn(failureList.get(i));
                }
            }
        }


    }

}
