package esride.opendatabridge.item;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 14.05.13
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class AgolItem {
    private HashMap<String,String> _itemAttributes;

    // Constructors
    public AgolItem(HashMap<String,String> itemAttributes)
    {
        _itemAttributes = itemAttributes;
    }

    public HashMap<String,String> getAttributes()
    {
        return _itemAttributes;
    }

    public String getUrl() {
        if (_itemAttributes.get("url")!=null) {
            return _itemAttributes.get("url");
        }
        return "";
    }

    public String getId() {
        if (_itemAttributes.get("id")!=null) {
            return _itemAttributes.get("id");
        }
        return "";
    }

    public String getTitle() {
        return _itemAttributes.get("title");
    }

    public String getType() {
        return _itemAttributes.get("type");
    }

    public String getForeignKey(String keyPrefix){
        //todo: aus den tags muss noch der richtige Wert herausgeholt werden
        String tags = _itemAttributes.get("tags");
        if(tags != null && tags.trim().length() > 0){
            if(!tags.contains(keyPrefix)){
                return null;
            }
            StringTokenizer tokenizer = new StringTokenizer(tags, ",");
            String foreinKey = null;
            while (tokenizer.hasMoreTokens()){
                String actualToken = tokenizer.nextToken().trim();
                if(actualToken.startsWith(keyPrefix)){
                    foreinKey = actualToken;
                    break;
                }
            }
            return foreinKey;
        }
        return null;

    }

    public void setId(String id) {
        updateAttribute("id", id);
    }

    public void updateAttribute(String key, String value) {
        removeAttribute(key);
        _itemAttributes.put(key, value);
    }

    public void removeAttribute(String key) {
        if (_itemAttributes.containsKey(key)) {
            _itemAttributes.remove(key);
        }
    }
}

