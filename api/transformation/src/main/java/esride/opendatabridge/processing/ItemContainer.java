package esride.opendatabridge.processing;

import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.reader.TransformedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The ItemContainer is responsible for decide if one {@link esride.opendatabridge.reader.TransformedItem TransformedItem}
 * will be inserted or updated or if one {@link esride.opendatabridge.item.AgolItem AgolItem} will be prepare for delete
 * 
 * The CreateUpdateDelete Strategy:
 * 0 agolItem, 1-N catalogItem (=Insert one new agolItem)
 * 1 agolItem, 1 catalogItem (=Update agolItem with catalogItem elements)
 * 1-N agolItem, N catalogItem (=title equal=Update, other Delete and Insert)
 * 1-N agolItem, 0 catalogItem (=?Delete agolItem)
 *
 * User: sma
 * Date: 03.05.13
 * Time: 15:17 
 */
public class ItemContainer {
    
    private List<TransformedItem> catalogItemList = new ArrayList<TransformedItem>();
     
    private List<AgolItem> agolItemList = new ArrayList<AgolItem>();

    public void addCatalogItem(TransformedItem catalogItem){
        catalogItemList.add(catalogItem);
    }
    
    public void setCatalogItems(List<TransformedItem> catalogItems){
        this.catalogItemList = catalogItems;
    }

    public void addAgolItem(AgolItem agolItem){
        agolItemList.add(agolItem);
    }
    
    public void setAgolItems(List<AgolItem> agolItems){
        this.agolItemList = agolItems;
    }
    
    public List<ItemTransaction> getItemsForPublishing(){
        int catalogItemListSize = catalogItemList.size();
        int agolItemListSize = agolItemList.size();

        List<ItemTransaction> itemTransactionList = new ArrayList<ItemTransaction>();
        if(agolItemListSize == 0 && catalogItemListSize >= 1){
            for (TransformedItem transItem : catalogItemList) {
                ItemTransaction newItem = new ItemTransaction();
                newItem.setTransformedItem(transItem);
                newItem.setTransactionStatus(1);
                itemTransactionList.add(newItem);
            }
            return itemTransactionList;
        }

        if(agolItemListSize == 1 && catalogItemListSize == 1) {
            ItemTransaction updateItem = new ItemTransaction();
            //ToDo: updateAgolItem aus CatalogItem
            updateItem.setAgolItem(agolItemList.get(0));
            updateItem.setTransformedItem(catalogItemList.get(0));
            updateItem.setTransactionStatus(2);
            itemTransactionList.add(updateItem);
            return itemTransactionList;
        }

        if(agolItemListSize >= 1 && catalogItemListSize > 1){

            List<TransformedItem> transformRemoveList = new ArrayList<TransformedItem>();
            List<AgolItem> agolRemoveList = new ArrayList<AgolItem>();
            for (TransformedItem transItem : catalogItemList) {
                String catalogTitle = transItem.getItemElements().get("agol.title");
                if(catalogTitle.contains("\n")){
                    catalogTitle = catalogTitle.replace("\n", " ");
                }
                for(AgolItem agolItem : agolItemList){
                    String agolTitle = agolItem.getAttributes().get("title");
                    if(agolTitle.equalsIgnoreCase(catalogTitle)){
                        ItemTransaction updateItem = new ItemTransaction();
                        updateItem.setAgolItem(agolItem);
                        updateItem.setTransformedItem(transItem);
                        updateItem.setTransactionStatus(2);
                        itemTransactionList.add(updateItem);
                        transformRemoveList.add(transItem);
                        agolRemoveList.add(agolItem);
                        break;
                    }
                }

                if(agolRemoveList.size() > 0){
                    for(AgolItem removeItem : agolRemoveList){
                        agolItemList.remove(removeItem);
                    }
                }
            }

            if(transformRemoveList.size() > 0){
                for(TransformedItem removeItem : transformRemoveList){
                    catalogItemList.remove(removeItem);
                }
            }

            for(TransformedItem transItem : catalogItemList){
                ItemTransaction insertItem = new ItemTransaction();
                insertItem.setTransformedItem(transItem);
                insertItem.setTransactionStatus(1);
                itemTransactionList.add(insertItem);
            }

            for(AgolItem agolItem : agolItemList){
                ItemTransaction deleteItem = new ItemTransaction();
                deleteItem.setAgolItem(agolItem);
                deleteItem.setTransactionStatus(3);
                itemTransactionList.add(deleteItem);
            }
            return itemTransactionList;
        }

        if(agolItemListSize >= 1 && catalogItemListSize == 0){
            for (AgolItem agolItem : agolItemList) {
                ItemTransaction deleteItem = new ItemTransaction();
                deleteItem.setAgolItem(agolItem);
                deleteItem.setTransactionStatus(3);
                itemTransactionList.add(deleteItem);
            }
            return itemTransactionList;
        }
        return itemTransactionList;

    }
}
