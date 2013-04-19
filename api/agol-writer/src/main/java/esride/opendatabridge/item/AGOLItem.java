package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolwriter.IWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;


import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 07.03.13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class AGOLItem implements IItem {
    private HashMap _itemAttributes;

    public AGOLItem()
    {
        _itemAttributes = new HashMap();
    }

    public AGOLItem(String jsonAGOLItem)
    {
        fromJson(jsonAGOLItem);
    }

    public void fromJson(String jsonAGOLItem)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            _itemAttributes = objectMapper.readValue(jsonAGOLItem, HashMap.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String toJson()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter  strWriter = new StringWriter();
        try {
            objectMapper.writeValue(strWriter, getAttributes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return strWriter.toString();
    }

    public HashMap<String, Object>  getAttributes()
    {
        return _itemAttributes;
    }
}

