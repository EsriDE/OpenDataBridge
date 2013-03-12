package esride.opendatabridge.item;

import java.util.HashMap;


import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 07.03.13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class AGOLItem {
    private HashMap<String,String> _itemAttributes;

    public AGOLItem ()
    {
        _itemAttributes = new HashMap<String, String>();
    }

    public HashMap<String,String>  getAttributes()
    {
        return _itemAttributes;
    }
}

