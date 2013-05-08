package esride.opendatabridge.processing;

import esride.opendatabridge.reader.TransformedItem;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 03.05.13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class ItemContainer {
    
    private TransformedItem catalogItem;
     
    private Object agolItem;

    
    public void setCatalogItem(TransformedItem pCatalogItem) {
        catalogItem = pCatalogItem;
    }

    
    public void setAgolItem(Object pAgolItem) {
        agolItem = pAgolItem;
    }
    
    public int getTransactionStatus(){
        //Update
        return 0;
        //Insert
        //return 1;

        //Delete
        //return 2;
    }
    
    public Object getItemForPublishing(){
        
        //
        return null;
    }
}
