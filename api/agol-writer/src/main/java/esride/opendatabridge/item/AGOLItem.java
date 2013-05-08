package esride.opendatabridge.item;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 07.03.13
 * Time: 14:10
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
        return _itemAttributes.get("url");
    }

    public String getId() {
        return _itemAttributes.get("id");
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

