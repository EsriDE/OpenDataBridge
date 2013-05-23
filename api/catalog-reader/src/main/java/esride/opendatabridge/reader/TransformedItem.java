package esride.opendatabridge.reader;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 12.04.13
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class TransformedItem {
    
    private HashMap<String, String> itemElements;
    
    //private String resourceUrl;

    public String getResourceUrl() {
        if(itemElements != null){
            return itemElements.get("agol.url");
        }
        return null;
    }

    /*public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    } */

    public HashMap<String, String> getItemElements() {
        return itemElements;
    }

    public void setItemElements(HashMap<String, String> itemElements) {
        this.itemElements = itemElements;
    }
}
