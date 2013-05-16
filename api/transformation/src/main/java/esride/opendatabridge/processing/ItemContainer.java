package esride.opendatabridge.processing;

import esride.opendatabridge.agolwriter.IAgolService;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.reader.TransformedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * An ItemContainer contains
 * Transaction Strategy:
 * 0 agolItem, 1-N catalogItem (=Insert one new agolItem)
 * 1 agolItem, 1 catalogItem (=Update agolItem with catalogItem elements)
 * 1-N agolItem, N catalogItem (=title equal=Update, other Delete and Insert)
 *
 * 1-N agolItem, 0 catalogItem (=?Delete agolItem)
 *
 * User: sma
 * Date: 03.05.13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
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
    
    public List<ItemTransaction> getItemsForPublishing(IAgolService agolService){
        int catalogItemListSize = catalogItemList.size();
        int agolItemListSize = agolItemList.size();

        List<ItemTransaction> itemTransactionList = new ArrayList<ItemTransaction>();
        if(agolItemListSize == 0 && catalogItemListSize >= 1){
            for (TransformedItem transItem : catalogItemList) {
                ItemTransaction newItem = new ItemTransaction();                
                AgolItem agolItem = agolService.createAgolItem(transItem.getItemElements());
                newItem.setAgolItem(agolItem);
                newItem.setTransactionStatus(1);
                itemTransactionList.add(newItem);
            }
            return itemTransactionList;
        }

        if(agolItemListSize == 1 && catalogItemListSize == 1) {
            ItemTransaction updateItem = new ItemTransaction();
            //ToDo: updateAgolItem aus CatalogItem
            AgolItem agolItem = agolService.createAgolItem(catalogItemList.get(0).getItemElements());
            AgolItem updateAgolItem = agolService.mergeAgolItems(agolItemList.get(0), agolItem);
            updateItem.setAgolItem(updateAgolItem);
            updateItem.setTransactionStatus(2);
            return itemTransactionList;
        }

        if(agolItemListSize >= 1 && catalogItemListSize > 1){

            for (TransformedItem transItem : catalogItemList) {
                String catalogTitle = transItem.getItemElements().get("agol.title");

                for(AgolItem agolItem : agolItemList){
                    String agolTitle = agolItem.getAttributes().get("title");
                    if(agolTitle.equalsIgnoreCase(catalogTitle)){
                        ItemTransaction updateItem = new ItemTransaction();
                        AgolItem newAgolItem = agolService.createAgolItem(transItem.getItemElements());
                        AgolItem updateAgolItem = agolService.mergeAgolItems(agolItem, newAgolItem);
                        updateItem.setAgolItem(updateAgolItem);
                        updateItem.setTransactionStatus(2);
                        itemTransactionList.add(updateItem);
                        catalogItemList.remove(transItem);
                        agolItemList.remove(agolItem);

                        break;
                    }
                }
            }

            for(TransformedItem transItem : catalogItemList){
                ItemTransaction insertItem = new ItemTransaction();
                AgolItem agolItem = agolService.createAgolItem(transItem.getItemElements());
                insertItem.setAgolItem(agolItem);
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
