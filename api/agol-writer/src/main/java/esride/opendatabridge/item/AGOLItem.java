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
}

