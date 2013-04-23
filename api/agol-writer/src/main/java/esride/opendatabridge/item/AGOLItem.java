package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolwriter.IWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 07.03.13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class AgolItem {
    private HashMap _itemAttributes;

    // Constructors
    public AgolItem(HashMap itemAttributes)
    {
        _itemAttributes = itemAttributes;
    }

    public HashMap<String, Object> getAttributes()
    {
        return _itemAttributes;
    }
}

