package esride.opendatabridge.reader;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 12.04.13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class ReaderItems {
    
    private List<ReaderItem> items = new ArrayList<ReaderItem>();

    public List<ReaderItem> getItems() {
        return items;
    }

    public void setItems(List<ReaderItem> items) {
        this.items = items;
    }

    public void addItem(ReaderItem item){
        items.add(item);
    }
}
