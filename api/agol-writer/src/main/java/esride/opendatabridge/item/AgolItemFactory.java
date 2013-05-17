package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 23.04.13
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemFactory {

    private static final Logger _log = Logger.getLogger(AgolItemFactory.class.getName());
    private Boolean _propertiesToStrings;
    Properties _properties;
    private ArrayList<String> _validAgolItemPropertyKeys;
    private ArrayList<String> _requiredAgolItemPropertyKeys;
    private ArrayList<String> _textAgolItemPropertyKeys;
    private ObjectMapper _objectMapper;

    /**
     * Setter for _objectMapper
     * @param objectMapper
     */
    public void set_objectMapper(ObjectMapper objectMapper) {
        this._objectMapper = objectMapper;
    }

    /**
     * Constructor
     * @param propertiesToStrings
     */
    public AgolItemFactory(Boolean propertiesToStrings) throws IOException {
        _propertiesToStrings = propertiesToStrings;

        _properties = new Properties();
        _properties.load(this.getClass().getResourceAsStream("/validAgolItemProperties.properties"));

        Collection vaiProperties = _properties.values();
        _validAgolItemPropertyKeys = new ArrayList<String>();
        _validAgolItemPropertyKeys.addAll(vaiProperties);

        // without an id, no update is possible - can not be thrown out
        _validAgolItemPropertyKeys.add("id");

        // properties that come in from the catalogues, but don't match Agol fields: to be combined to JSON "text" field
        _textAgolItemPropertyKeys = new ArrayList<String>();
        _textAgolItemPropertyKeys.add("serviceversion");
        _textAgolItemPropertyKeys.add("maxheight");
        _textAgolItemPropertyKeys.add("maxwidth");
        _textAgolItemPropertyKeys.add("layerids");
        _textAgolItemPropertyKeys.add("layertitles");
    }

    /**
     * Create ArcGIS Online item from Esri JSON
     * @param agolJsonItem
     * @return
     */
    public AgolItem createAgolItem(String agolJsonItem) {
        HashMap agolItemProperties = cleanAgolItemProperties(agolJsonToHashMap(agolJsonItem));
        if (!validateAgolItem(agolItemProperties)) {
            //ToDo: define error?
             _log.warn("Not a valid ArcGIS Online item: " + agolItemProperties.toString());
        }
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }
    /**
     * Create ArcGIS Online Item from HashMap
     * @param agolItemProperties
     * @return
     */
    public AgolItem createAgolItem(HashMap agolItemProperties) {
        if (_propertiesToStrings) {
            agolItemProperties = cleanAgolItemProperties(agolItemProperties);
        }
        if (!validateAgolItem(agolItemProperties)) {
            _log.warn("Not a valid ArcGIS Online item: " + agolItemProperties.toString());
        }
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }

    /**
     * Check if the ArcGIS Online item contains the required parameters to prevent cases like this AgolItem: error={code=400, messageCode=CONT_0001, message=Item '2C2cc78b3b57e64967aae845b937e92637' does not exist or is inaccessible., details=}
     * @param agolItemProperties
     * @return
     */
    private Boolean validateAgolItem(HashMap agolItemProperties) {
        if (agolItemProperties.containsKey("error")) {
            return false;
        }
        return true;
    }
    /**
     * Transform Esri JSON to Hash Map
     * @param agolJsonItem
     * @return
     */
    private HashMap<String,String> agolJsonToHashMap(String agolJsonItem) {
        HashMap agolItemProperties = new HashMap();
        try {
            agolItemProperties = _objectMapper.readValue(agolJsonItem, HashMap.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (_propertiesToStrings) {
            agolItemProperties = cleanAgolItemProperties(agolItemProperties);
        }

        return agolItemProperties;
    }

    /**
     * Clean a HashMap of ArcGIS Online Item from properties that are not accepted when uploading an item, from properties with null values and transform all values to Strings
     * @param agolItemProperties
     * @return
     */
    private HashMap cleanAgolItemProperties(HashMap agolItemProperties) {
        HashMap deleteAgolItemProperties = new HashMap();
        HashMap updateAgolItemProperties = new HashMap();
        HashMap textAgolItemProperties = new HashMap();

        // Altering objects in a HashMap while iterating through it leads to null pointer errors - so we do everything in single loops.
        Iterator findRemovePropertiesIterator = agolItemProperties.entrySet().iterator();
        while (findRemovePropertiesIterator.hasNext()) {
            Map.Entry property = (Map.Entry) findRemovePropertiesIterator.next();
            Object propertyKey = property.getKey();
            Object propertyValue = property.getValue();
            // Remove null and invalid values. Combine special values to "text" property.
            if ((propertyValue == null)
                    || !_validAgolItemPropertyKeys.contains(propertyKey)
                    || propertyKey.equals("text")
                    || _textAgolItemPropertyKeys.contains(propertyKey)) {
                if (_textAgolItemPropertyKeys.contains(propertyKey)) {
                    textAgolItemProperties.put(propertyKey, propertyValue);
                }
                deleteAgolItemProperties.put(propertyKey, propertyValue);
            }
        }
        Iterator removePropertiesIterator = deleteAgolItemProperties.entrySet().iterator();
        while (removePropertiesIterator.hasNext()) {
            Map.Entry property = (Map.Entry) removePropertiesIterator.next();
            agolItemProperties.remove(property.getKey());
            if (_log.isInfoEnabled()) {
                _log.info("Entry \"" + property.getKey() + "\" removed.");
            }
        }
        Iterator findUpdatePropertiesIterator = agolItemProperties.entrySet().iterator();
        while (findUpdatePropertiesIterator.hasNext()) {
            Map.Entry property = (Map.Entry) findUpdatePropertiesIterator.next();
            Object propertyValue = property.getValue();
            if (propertyValue!=null) {
                Class propertyValueClass = propertyValue.getClass();
                // Transform all values to Strings and remove "agol." prefix from keys
                if (!propertyValueClass.equals(String.class) || (property.getKey().toString().startsWith("agol."))) {
                    String stringPropertyValue = propertyValue.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    String agolKey = property.getKey().toString().replace("agol.", "");
                    updateAgolItemProperties.put(agolKey, stringPropertyValue);
                    if (_log.isInfoEnabled()) {
                        _log.info("Entry \"" + agolKey + "\" updated.");
                    }
                }
            }
        }
        Iterator updatePropertiesIterator = updateAgolItemProperties.entrySet().iterator();
        while (updatePropertiesIterator.hasNext()) {
            Map.Entry updateProperty = (Map.Entry) updatePropertiesIterator.next();
            agolItemProperties.remove(updateProperty.getKey());
            agolItemProperties.put(updateProperty.getKey(), updateProperty.getValue());
        }

        agolItemProperties.put("text", createTextAgolItemProperty(textAgolItemProperties));

        return agolItemProperties;
    }

    /**
     * Create JSON String from HashMap
     * @param textAgolItemProperties
     * @return
     */
    private String createTextAgolItemProperty(HashMap textAgolItemProperties) {
        String layerids = "";
        String layertitles = "";
        String jsonText = "{";

        Iterator textAgolItemPropertiesIterator = textAgolItemProperties.entrySet().iterator();
        while (textAgolItemPropertiesIterator.hasNext()) {
            if (!jsonText.equals("")) {
                jsonText += ",";
            }
            Map.Entry textProperty = (Map.Entry) textAgolItemPropertiesIterator.next();
            if (textProperty.getKey().equals("serviceversion")) {
                jsonText += "\"version\":\"" + textProperty.getValue() + "\"";
            }
            else if (textProperty.getKey().equals("maxheight")) {
                jsonText += "\"maxHeight\":\"" + textProperty.getValue() + "\"";
            }
            else if (textProperty.getKey().equals("maxwidth")) {
                jsonText += "\"maxWidth\":\"" + textProperty.getValue() + "\"";
            }
            else if (textProperty.getKey().equals("maxwidth")) {
                jsonText += "\"maxWidth\":\"" + textProperty.getValue() + "\"";
            }
            else if (textProperty.getKey().equals("layerids")) {
                layerids = textProperty.getValue().toString();
            }
            else if (textProperty.getKey().equals("layertitles")) {
                layertitles = textProperty.getValue().toString();
            }
        }

        if (!layerids.equals("")) {
            if (!jsonText.equals("")) {
                jsonText += ",";
            }
            jsonText += "\"layers\":[";
            String jsonLayers = "";
            String[] layerIdsArray = layerids.split(",");
            String[] layerTitlesArray = layertitles.split(",");
            for (int i=0; i<layerIdsArray.length; i++) {
                if (!jsonLayers.equals("")) {
                    jsonLayers += ",";
                }
                jsonLayers += "{\"name\":\"" + layerIdsArray[i] + "\",\"title\":\"";
                if (i<layerTitlesArray.length) {
                    jsonLayers += layerTitlesArray[i];
                }
                jsonLayers += "\"}";
            }
            jsonText += jsonLayers + "]";
        }

        return jsonText;
    }
}