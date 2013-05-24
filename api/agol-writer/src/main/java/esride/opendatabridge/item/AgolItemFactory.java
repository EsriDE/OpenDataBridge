package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolwriter.AgolItemInvalidException;
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
    private ArrayList<String> _exclusiveTextAgolItemPropertyKeys;
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
        _validAgolItemPropertyKeys.add("id");   // without an id, no update is possible - can not be thrown out, but is not required for adding an AgolItem

        _requiredAgolItemPropertyKeys = new ArrayList<String>();
        _requiredAgolItemPropertyKeys.add("type");
        _requiredAgolItemPropertyKeys.add("title");

        // properties that come in from the catalogues, but don't match Agol fields: to be combined to JSON "text" field
        _exclusiveTextAgolItemPropertyKeys = new ArrayList<String>();
        _exclusiveTextAgolItemPropertyKeys.add("serviceversion");
        _exclusiveTextAgolItemPropertyKeys.add("maxheight");
        _exclusiveTextAgolItemPropertyKeys.add("maxwidth");
        _exclusiveTextAgolItemPropertyKeys.add("layerids");
        _exclusiveTextAgolItemPropertyKeys.add("layertitles");

        // properties that come in from the catalogues, that go into the JSON "text" field, but also match Agol fields
        _textAgolItemPropertyKeys = new ArrayList<String>();
        _textAgolItemPropertyKeys.add("copyright");
        _textAgolItemPropertyKeys.add("format");
//        _textAgolItemPropertyKeys.add("mapUrl");
        _textAgolItemPropertyKeys.add("spatialReferences");
        _textAgolItemPropertyKeys.add("title");
        _textAgolItemPropertyKeys.add("url");
    }

    /**
     * Create ArcGIS Online item from Esri JSON
     * @param agolJsonItem
     * @return
     * @throws AgolItemInvalidException
     */
    public AgolItem createAgolItem(String agolJsonItem) throws AgolItemInvalidException {
        HashMap agolItemProperties = cleanAgolItemProperties(agolJsonToHashMap(agolJsonItem));
        validateAgolItem(agolItemProperties);
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }

    /**
     * Create ArcGIS Online Item from HashMap
     * @param agolItemProperties
     * @return
     * @throws AgolItemInvalidException
     */
    public AgolItem createAgolItem(HashMap agolItemProperties) throws AgolItemInvalidException {
        if (_propertiesToStrings) {
            agolItemProperties = cleanAgolItemProperties(agolItemProperties);
        }
        validateAgolItem(agolItemProperties);
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }

    /**
     * Check if the ArcGIS Online item contains the required parameters to prevent cases like this AgolItem: error={code=400, messageCode=CONT_0001, message=Item '2C2cc78b3b57e64967aae845b937e92637' does not exist or is inaccessible., details=}
     * @param agolItemProperties
     * @return
     * @throws AgolItemInvalidException
     */
    private Boolean validateAgolItem(HashMap agolItemProperties) throws AgolItemInvalidException {
        Iterator<String> requiredAgolItemPropertyKeysIterator = _requiredAgolItemPropertyKeys.iterator();
        while (requiredAgolItemPropertyKeysIterator.hasNext()) {
            String propertyKey = requiredAgolItemPropertyKeysIterator.next();
            if (!agolItemProperties.containsKey(propertyKey)) {
                String errorMessage = "";
                if (agolItemProperties.containsKey("title")) {
                    errorMessage = agolItemProperties.get("title").toString() + " is not ";
                }
                else {
                    errorMessage = "Not ";
                }
                throw new AgolItemInvalidException(errorMessage + "a valid ArcGIS Online item: " + propertyKey + " is missing.");
            }
        }
        if (agolItemProperties.containsKey("error")) {
            String errorMessage = "";
            if (agolItemProperties.containsKey("title")) {
                errorMessage = agolItemProperties.get("title").toString() + " is not ";
            }
            else {
                errorMessage = "Not ";
            }
            throw new AgolItemInvalidException(errorMessage + "a valid ArcGIS Online item: Contains key \"error\".");
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
        HashMap textAgolItemProperties = new HashMap();
        HashMap agolItemPropertiesUpdated = new HashMap();

        // Altering objects in a HashMap while iterating through it leads to null pointer errors - so we do everything in single loops.
        Iterator agolPrefixPropertiesIterator = agolItemProperties.entrySet().iterator();
        while (agolPrefixPropertiesIterator.hasNext()) {
            Map.Entry property = (Map.Entry) agolPrefixPropertiesIterator.next();
            // Leave out entries with null values
            if (property.getValue()!=null) {
                String propertyKey = property.getKey().toString();
                String propertyValue = property.getValue().toString();
                // Remove "agol." prefix from keys
                if (propertyKey.startsWith("agol.")) {
                    propertyKey = propertyKey.toString().replace("agol.", "");
                    if (_log.isDebugEnabled()) {
                        _log.debug("\"agol.\" prefix removed from key \"" + propertyKey + "\".");
                    }
                }
                // Transform all values to Strings
                if (!property.getValue().getClass().equals(String.class)) {
                    propertyValue = propertyValue.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    if (_log.isDebugEnabled()) {
                        _log.debug("\"" + propertyKey + "\" value updated: " + propertyValue);
                    }
                }
                if (_exclusiveTextAgolItemPropertyKeys.contains(propertyKey)
                    || _textAgolItemPropertyKeys.contains(propertyKey)) {
                    textAgolItemProperties.put(propertyKey, propertyValue);
                }
                if (_validAgolItemPropertyKeys.contains(propertyKey)
                        && !_exclusiveTextAgolItemPropertyKeys.contains(propertyKey)
                        && !propertyKey.equals("text")) {
                    agolItemPropertiesUpdated.put(propertyKey, propertyValue);
                }
            }
        }

        if (agolItemPropertiesUpdated.get("type").equals("WMS")) {
            agolItemPropertiesUpdated.put("text", createTextAgolItemProperty(textAgolItemProperties));
        }
        return agolItemPropertiesUpdated;
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
            Map.Entry textProperty = (Map.Entry) textAgolItemPropertiesIterator.next();
            if (textProperty.getKey().equals("serviceversion")) {
                jsonText += "\"version\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("maxheight")) {
                jsonText += "\"maxHeight\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("maxwidth")) {
                jsonText += "\"maxWidth\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("maxwidth")) {
                jsonText += "\"maxWidth\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("title")) {
                jsonText += "\"title\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("url")) {
                String url = textProperty.getValue().toString();
                jsonText += "\"url\":\"" + url + "\",";
                if (!url.endsWith("?")) {
                    url = url + "?";
                }
                jsonText += "\"mapUrl\":\"" + url + "\",";
            }
            else if (textProperty.getKey().equals("copyright")) {
                jsonText += "\"copyright\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("format")) {
                jsonText += "\"format\":\"" + textProperty.getValue() + "\",";
            }
            else if (textProperty.getKey().equals("spatialReferences")) {
                jsonText += "\"spatialReferences\":[" + textProperty.getValue() + "],";
            }
            else if (textProperty.getKey().equals("layerids")) {
                layerids = textProperty.getValue().toString();
            }
            else if (textProperty.getKey().equals("layertitles")) {
                layertitles = textProperty.getValue().toString();
            }
        }

        if (!layerids.equals("")) {
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

        if (jsonText.endsWith(",")) {
            jsonText = jsonText.substring(0, jsonText.length()-1);
        }

        jsonText += "}";
        return jsonText;
    }


    /**
     * Merge 2 ArcGIS Online Items by copying metadata from source to target. Overwrites existing values, adds new values and values untouched, that are only in the targetItem.
     * @param sourceItem
     * @param targetItem
     * @return
     */
    public AgolItem mergeAgolItems(AgolItem sourceItem, AgolItem targetItem) {
        Iterator attributesIterator = sourceItem.getAttributes().entrySet().iterator();
        while (attributesIterator.hasNext()) {
            Map.Entry attribute = (Map.Entry) attributesIterator.next();
            if (targetItem.getAttributes().containsKey(attribute.getKey())) {
                targetItem.updateAttribute(attribute.getKey().toString(), attribute.getValue().toString());
            }
            else {
                targetItem.getAttributes().put(attribute.getKey().toString(), attribute.getValue().toString());
            }
        }
        return targetItem;
    }
}