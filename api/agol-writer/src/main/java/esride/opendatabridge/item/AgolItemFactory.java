package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.FileHandler;
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
    FileHandler handler;
    Boolean _propertiesToStrings;

    public AgolItemFactory(String logPath, Boolean propertiesToStrings) {
        _propertiesToStrings = propertiesToStrings;
        objectMapper = new ObjectMapper();

        // Log Settings
        try {
            handler = new FileHandler(logPath, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        log.addHandler(handler);
        log.setLevel(Log.BRIEF);
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

        if (_propertiesToStrings) {
            agolItemProperties = allAgolItemPropertiesToString(agolItemProperties);
        }

        return agolItemProperties;
    }

    private HashMap allAgolItemPropertiesToString(HashMap agolItemProperties) {
        HashMap deleteAgolItemProperties = new HashMap();
        HashMap updateAgolItemProperties = new HashMap();

        // Altering objects in a HashMap while iterating through it is dangerous - so we do everything in single loops.
        Iterator findNullItemsIterator = agolItemProperties.entrySet().iterator();
        int iteCounter = 0;
        long startTime = System.currentTimeMillis();
        while (findNullItemsIterator.hasNext()) {
            Map.Entry property = (Map.Entry) findNullItemsIterator.next();
            Object propertyValue = property.getValue();
            if (propertyValue == null) {
                deleteAgolItemProperties.put(property.getKey(), propertyValue);
            }
            iteCounter++;
        }
        Iterator removeNullItemsIterator = deleteAgolItemProperties.entrySet().iterator();
        while (removeNullItemsIterator.hasNext()) {
            Map.Entry property = (Map.Entry) removeNullItemsIterator.next();
            agolItemProperties.remove(property.getKey());
            log.info("Entry \"" + property.getKey() + "\" with null value removed.");
            iteCounter++;
        }
        Iterator findUpdatePropertiesIterator = agolItemProperties.entrySet().iterator();
        while (findUpdatePropertiesIterator.hasNext()) {
            Map.Entry property = (Map.Entry) findUpdatePropertiesIterator.next();
            Object propertyValue = property.getValue();
            if (propertyValue!=null) {
                Class propertyValueClass = propertyValue.getClass();
                if (!propertyValueClass.equals(String.class)) {
                    String stringPropertyValue = propertyValue.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    updateAgolItemProperties.put(property.getKey(), stringPropertyValue);
                    log.info(propertyValueClass.toString() + " value of key \"" + property.getKey() + "\" transformed to String value \"" + stringPropertyValue + "\"");
                }
            }
            iteCounter++;
        }
        Iterator updatePropertiesIterator = updateAgolItemProperties.entrySet().iterator();
        while (updatePropertiesIterator.hasNext()) {
            Map.Entry updateProperty = (Map.Entry) updatePropertiesIterator.next();
            agolItemProperties.remove(updateProperty.getKey());
            agolItemProperties.put(updateProperty.getKey(), updateProperty.getValue());
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