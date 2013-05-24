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

    /**
     * This method executes the transformation process. The transformation process selects the items from ArcGIS Online to check them, if they have to be updates
     * or delete, if the deleteStrategy parameter is true. After that, the items from the catalog (ckan, csw, etc...) would be transformed and inserted/updated in ArcGIS Online
     * @param reader the reader adapter (csw, ckan, etc...)
     * @param agolService the ArcGIS Online adapter
     * @param deleteStrategy if deleteStrategy=false, no agolItem would be deleted if no corresponding catalogItem exists
     * @param overwriteAccessType if overwriteShareType=true, the submitted accessType will be used and overwrite the existing one in an update situation
     * @param accessType the flag for the access type. Allowed values are PRIVATE, SHARED, ORG, PUBLIC
     * @param ownerType the flag for the owner type. Allowed values are  USER, ORG
     */
    public void executeProcessTransformation(IReader reader, IAgolService agolService, 
                                             boolean deleteStrategy, boolean overwriteAccessType,
                                             String searchString, String accessType, String ownerType){
        HashMap<String, ItemContainer> itemContainer = new HashMap<String, ItemContainer>();
        Map<String, ArrayList<AgolItem>> itemMap = null;
        try {
            itemMap =  agolService.searchItems(searchString, OwnerType.valueOf(ownerType));
        } catch (IOException e) {
            //ToDo: Exception Handling  
        } catch (AgolItemInvalidException e) {
            e.printStackTrace();  //ToDo: Exception Handling
        }

        if(itemMap != null){
            Set<String> keys = itemMap.keySet();
            Iterator<String> keyIter = keys.iterator();
            while(keyIter.hasNext()){
                String key = keyIter.next();
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
            //ToDo: Exception Handling 
        }

        if(transformedItemList != null){
            int listSize = transformedItemList.size();
            for(int i=0; i<listSize; i++){
                TransformedItem item = transformedItemList.get(i);
                ItemContainer elem;
                if(itemContainer.containsKey(item.getResourceUrl())){
                    elem = itemContainer.get(item.getResourceUrl());                    
                }else{
                    elem = new ItemContainer();
                    itemContainer.put(item.getResourceUrl(), elem);                    
                }
                elem.addCatalogItem(item);                                                
                
            }
        }

        Set<String> keySet = itemContainer.keySet();
        Iterator<String> iter = keySet.iterator();
        
        List<AgolItem> insertList = new ArrayList<AgolItem>();
        List<AgolItem> updateList = new ArrayList<AgolItem>();
        List<AgolItem> deleteList = new ArrayList<AgolItem>();
        
        while(iter.hasNext()){
            String key = iter.next();
            ItemContainer containerElement = itemContainer.get(key);
            List<ItemTransaction> itemTransList = containerElement.getItemsForPublishing();
            for(ItemTransaction itemTrans : itemTransList){
                int status = itemTrans.getTransactionStatus();
                switch (status){
                    case 1:
                        AgolItem agolItem = null;
                        try {
                            agolItem = agolService.createAgolItem(itemTrans.getTransformedItem().getItemElements());
                        } catch (AgolItemInvalidException e) {
                            e.printStackTrace();  //ToDo: Exception Handling
                        }
                        insertList.add(agolItem);
                        break;
                    case 2:
                        AgolItem newAgolItem = null;
                        try {
                            newAgolItem = agolService.createAgolItem(itemTrans.getTransformedItem().getItemElements());
                        } catch (AgolItemInvalidException e) {
                            e.printStackTrace();  //ToDo: Exception Handling
                        }
                        AgolItem updateAgolItem = agolService.mergeAgolItems(itemTrans.getAgolItem(), newAgolItem);
                        updateList.add(updateAgolItem);
                        break;
                    case 3:
                        deleteList.add(itemTrans.getAgolItem());
                        break;
                    default:
                        sLogger.warn("Wrong status number: " + status);
                }
                
            }
        }

        try {
            if(insertList.size()>0){
                agolService.addItems(insertList, AccessType.valueOf(accessType));
            }
            if(overwriteAccessType){
                if(updateList.size() > 0){
                    agolService.updateItems(updateList, AccessType.valueOf(accessType));
                }
            }else{
                if(updateList.size() > 0){
                    agolService.updateItems(updateList);
                }
            }
            if(deleteStrategy && deleteList.size() > 0){
                agolService.deleteItems(deleteList);
            }
        } catch (AgolTransactionFailedException e) {
            //ToDo: Exception Handling
        } catch (IOException e) {
            //ToDo: Exception Handling
        }


    }

    public void testProcessTransformation(){}
   


}
