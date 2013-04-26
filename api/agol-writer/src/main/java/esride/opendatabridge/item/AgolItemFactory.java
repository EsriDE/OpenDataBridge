package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 23.04.13
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemFactory {

    ObjectMapper objectMapper;
    private static final Logger log = Logger.getLogger(AgolItemFactory.class.getName());

    public AgolItemFactory() {
        objectMapper = new ObjectMapper();
    }

    public AgolItem createAgolItem(String agolJsonItem)
    {
        HashMap agolItemProperties = agolJsonToKeyValuePairs(agolJsonItem);
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }
    public AgolItem createAgolItem(HashMap agolItemProperties)
    {
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }

    private HashMap<String,String> agolJsonToKeyValuePairs(String agolJsonItem)
    {
        log.info("Transforming ArcGIS Online Json to <String,String>-Pairs.");
        HashMap agolItemProperties = new HashMap();
        try {
            agolItemProperties = objectMapper.readValue(agolJsonItem, HashMap.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        HashMap updateAgolItemProperties = new HashMap();

        Iterator removeNullItemsIterator = agolItemProperties.entrySet().iterator();
        int iteCounter = 0;
        long startTime = System.currentTimeMillis();
        while (removeNullItemsIterator.hasNext()) {
            Map.Entry property = (Map.Entry) removeNullItemsIterator.next();
            Object propertyValue = property.getValue();
            if (propertyValue == null) {
                agolItemProperties.remove(property.getKey());
                log.info("Entry \"" + property.getKey() + "\" with null value removed.");
                // Altering objects in a HashMap while iterating through it is dangerous,
                // so we get a fresh iterator (restart the loop) after each change.
                removeNullItemsIterator = agolItemProperties.entrySet().iterator();
            }
            else if (!propertyValue.getClass().equals(String.class)) {
                String stringPropertyValue = propertyValue.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                agolItemProperties.remove(property.getKey());
                agolItemProperties.put(property.getKey(), stringPropertyValue);
                log.info(propertyValue.getClass().toString() + " value of key \"" + property.getKey() + "\" transformed to String value \"" + stringPropertyValue + "\"");
                // Altering objects in a HashMap while iterating through it is dangerous,
                // so we get a fresh iterator (restart the loop) after each change.
                removeNullItemsIterator = agolItemProperties.entrySet().iterator();
            }
            iteCounter++;
        }
        log.info(iteCounter + " iterations performed in " + (System.currentTimeMillis()-startTime) + " ms.");
        return agolItemProperties;
    }

    public String agolItemToJson(AgolItem agolItem)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter strWriter = new StringWriter();
        try {
            objectMapper.writeValue(strWriter, agolItem.getAttributes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strWriter.toString();
    }
}