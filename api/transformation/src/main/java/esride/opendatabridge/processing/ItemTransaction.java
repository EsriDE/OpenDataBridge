package esride.opendatabridge.processing;

import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.reader.TransformedItem;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 14.05.13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class ItemTransaction {

    /**
     * 1=Insert, 2=Update, 3=Delete
     */
    private int transactionStatus;
    
    private AgolItem agolItem;

    private TransformedItem transformedItem;

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public AgolItem getAgolItem() {
        return agolItem;
    }

    public void setAgolItem(AgolItem agolItem) {
        this.agolItem = agolItem;
    }

    public TransformedItem getTransformedItem() {
        return transformedItem;
    }

    public void setTransformedItem(TransformedItem transformedItem) {
        this.transformedItem = transformedItem;
    }
}
