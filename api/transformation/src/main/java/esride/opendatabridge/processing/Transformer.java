package esride.opendatabridge.processing;


import esride.opendatabridge.agolreader.IAgolItemReader;
import esride.opendatabridge.agolwriter.*;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.pipeline.InvalidObjectException;
import esride.opendatabridge.pipeline.controller.PipelineController;
import esride.opendatabridge.reader.*;
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

    /*public void executeProcessDeleteDuplicate(IAgolService agolService, String searchString, String accessType, String ownerType)throws TransformerException {
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

    } */

    /**
     * This method executes the transformation process. The transformation process selects the items from ArcGIS Online to check them, if they have to be updates
     * or delete, if the deleteStrategy parameter is true. After that, the items from the catalog (ckan, csw, etc...) would be transformed and inserted/updated in ArcGIS Online
     * @param reader the reader adapter (csw, ckan, etc...)
     * @param agolService the ArcGIS Online adapter
     * @param deleteStrategy if deleteStrategy=false, no agolItem would be deleted if no corresponding catalogItem exists
     * @param overwriteAccessType if overwriteShareType=true, the submitted accessType will be used and overwrite the existing one in an update situation
     * @param accessType the flag for the access type. Allowed values are PRIVATE, SHARED, ORG, PUBLIC
     * @throws TransformerException if the transformation process failed
     */
    public void executeProcessTransformation(IReader reader, IAgolService agolService, PipelineController pipelineController, IAgolItemReader agolReader,
                                             boolean deleteStrategy, boolean overwriteAccessType,
                                             String accessType) throws TransformerException {

        int startPosition = 1;
        boolean hasNextElements = true;
        while(hasNextElements){
            try {
                //get metadata sets and further capabilities files from the catalog
                TransformedItemResult transformedResult = reader.getTramsformedItemsFromCatalog(startPosition);
                startPosition = transformedResult.getNextStartPosition();
                hasNextElements = transformedResult.isHasNextStartPosition();

                //iterate over the result
                List<TransformedItem> itemList = transformedResult.getTransformedResultList();
                for(int i=0; i<itemList.size(); i++){
                    TransformedItem item = itemList.get(i);

                    try {
                        //enrich, change or validate the item elements
                        pipelineController.examineAndChangeObject(item.getItemElements());

                        //check, if item is already stored in AGOL
                        ItemTransaction transactionStatus = insertUpdateCheck(agolReader, item.getItemElements());

                        //create AgolItem and update/insert the item
                        switch (transactionStatus.getTransactionStatus()){
                            case 1:
                                AgolItem agolItem = null;
                                try {
                                    agolItem = agolService.createAgolItem(item.getItemElements());
                                    agolService.insertItem(agolItem, AccessType.valueOf(accessType));
                                } catch (AgolItemInvalidException e) {
                                    sLogger.error("Creating an Agol Item failed", e);
                                } catch (IOException e) {
                                    sLogger.error("The AgolItem could not be inserted", e);
                                }
                                break;
                            case 2:
                                AgolItem newAgolItem = null;
                                try {
                                    newAgolItem = agolService.createAgolItem(item.getItemElements());
                                    AgolItem updateAgolItem = agolService.mergeAgolItems(agolReader.getAgolItemById(transactionStatus.getAgolItentifier()), newAgolItem);

                                    agolService.updateItem(updateAgolItem, AccessType.valueOf(accessType), overwriteAccessType);
                                    //Update Operation aufrufen
                                } catch (AgolItemInvalidException e) {
                                    sLogger.error("Merging an Agol Item failed", e);
                                } catch (IOException e) {
                                    sLogger.error("The AgolItem could not be updated", e);
                                }
                                break;
                            default:
                                sLogger.warn("Wrong status number: " + transactionStatus.getTransactionStatus());
                        }

                    } catch (InvalidObjectException e) {
                        sLogger.error("Invalid Object found", e);
                    }
                }


            } catch (ReaderException e) {
                sLogger.error("Exception during Catalog Search", e);
                throw new TransformerException("Exception during Catalog Search", e);
            }
        }

        if(deleteStrategy){
            List<String>  untouchedIds = agolReader.getUntouchedAgolItems();
            if(untouchedIds != null){
                int untoucheIdsSize = untouchedIds.size();
                for(int i=0; i<untoucheIdsSize; i++){
                    try {
                        agolService.deleteItem(untouchedIds.get(i));
                    } catch (IOException e) {
                        sLogger.error(" The AgolItem could not be deleted", e);
                    }
                }
            }
        }


    }

    private ItemTransaction insertUpdateCheck(IAgolItemReader agolReader, HashMap<String, String> elements){
        String titleName = "agol.title";
        String foreignIdName = "agol.foreignkey";
        String urlname = "agol.url";

        if(sLogger.isDebugEnabled()){
            sLogger.debug("Check transaction state. Element title: " + elements.get(titleName));
            sLogger.debug("Check transaction state. Element url: " + elements.get(urlname));
            sLogger.debug("Check transaction state. Foreignid url: " + elements.get(foreignIdName));
        }

        //Check the foreign id
        ItemTransaction itemTrans = new ItemTransaction();
        String idByForeignId = agolReader.getAgolItemIdByCatalogId(elements.get(foreignIdName));
        if(idByForeignId != null && idByForeignId.trim().length() > 0){
            //update
            itemTrans.setTransactionStatus(2);
            itemTrans.setAgolItentifier(idByForeignId);
            agolReader.touchItem(idByForeignId);
            return itemTrans;
        }

        //Check the url
        List<String> idByUrl = agolReader.getAgolItemIdByUrl(elements.get(urlname));
        if(idByUrl == null || idByUrl.size() == 0){
            //insert
            itemTrans.setTransactionStatus(1);
            return itemTrans;
        }
        if(idByUrl.size() == 1){
            //update
            itemTrans.setTransactionStatus(2);
            itemTrans.setAgolItentifier(idByUrl.get(0));
            agolReader.touchItem(idByForeignId);
            return itemTrans;
        }

        //Check the url
        List<String> matchingUrlAndTitle = new ArrayList<String>();
        List<String> idByTitle = agolReader.getAgolItemIdByTitle(elements.get(titleName));
        if(idByTitle == null){
            //insert
            itemTrans.setTransactionStatus(1);
            return itemTrans;
        }
        boolean urlAndTitleMatched = false;
        for(int i=0; i<idByUrl.size(); i++){
            String actualId = idByUrl.get(i);
            for(int j=0; j<idByTitle.size(); j++){
                String actualId2 = idByTitle.get(j);
                if(actualId.equals(actualId2)){
                    matchingUrlAndTitle.add(actualId);
                    urlAndTitleMatched = true;
                    break;
                }
            }
            if(urlAndTitleMatched){
                break;
            }
        }


        itemTrans.setTransactionStatus(2);
        itemTrans.setAgolItentifier(matchingUrlAndTitle.get(0));
        if(matchingUrlAndTitle.size() >= 1){
            for(int k=0; k<matchingUrlAndTitle.size(); k++){
                agolReader.touchItem(matchingUrlAndTitle.get(k));
            }
        }

        return itemTrans;


    }

}
