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
    Properties _xpathValue;
    private ArrayList<String> _validAgolItemProperties;
    private ArrayList<String> _requiredAgolItemProperties;
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

//        _xpathValue = new Properties();
//        _xpathValue.load(this.getClass().getResourceAsStream("/agolservice.properties"));

        _requiredAgolItemProperties = new ArrayList<String>();
        _requiredAgolItemProperties.add("id");
        _validAgolItemProperties = new ArrayList<String>();
        // ToDo: Not working. Why not working?
//        _validAgolItemProperties = _objectMapper.readValue(_xpathValue.getProperty("validAgolItemProperties.xpath"), ArrayList.class);
//        _validAgolItemProperties = _objectMapper.readValue("[\"access\",\"title\",\"thumbnail\",\"thumbnailURL\",\"metadata\",\"type\",\"typeKeywords\",\"description\",\"tags\",\"snippet\",\"extent\",\"spatialReference\",\"accessInformation\",\"licenseInfo\",\"culture\",\"serviceUsername\",\"servicePassword\",\"file\",\"url\",\"text\",\"relationshipType\",\"originItemId\",\"destinationItemId\",\"async\",\"multipart\",\"filename\"]", ArrayList.class);
        _validAgolItemProperties.addAll(_requiredAgolItemProperties);
    }

    /**
     * Create ArcGIS Online item from Esri JSON
     * @param agolJsonItem
     * @return
     */
    public AgolItem createAgolItem(String agolJsonItem) {
        HashMap agolItemProperties = cleanAgolItemProperties(agolJsonToHashMap(agolJsonItem));
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
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
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

    // ToDo:
    // - Define keys that are obligatory to prevent cases like this AgolItem: error={code=400, messageCode=CONT_0001, message=Item '2C2cc78b3b57e64967aae845b937e92637' does not exist or is inaccessible., details=}
    /**
     * Clean a HashMap of ArcGIS Online Item from properties that are not accepted when uploading an item, from properties with null values and transform all values to Strings
     * @param agolItemProperties
     * @return
     */
    private HashMap cleanAgolItemProperties(HashMap agolItemProperties) {
        HashMap deleteAgolItemProperties = new HashMap();
        HashMap updateAgolItemProperties = new HashMap();

        // Altering objects in a HashMap while iterating through it leads to null pointer errors - so we do everything in single loops.
        Iterator findNullItemsIterator = agolItemProperties.entrySet().iterator();
        while (findNullItemsIterator.hasNext()) {
            Map.Entry property = (Map.Entry) findNullItemsIterator.next();
            Object propertyKey = property.getKey();
            Object propertyValue = property.getValue();
            if ((propertyValue == null) || !_validAgolItemProperties.contains(propertyKey)) {
                deleteAgolItemProperties.put(propertyKey, propertyValue);
            }
        }
        Iterator removeNullItemsIterator = deleteAgolItemProperties.entrySet().iterator();
        while (removeNullItemsIterator.hasNext()) {
            Map.Entry property = (Map.Entry) removeNullItemsIterator.next();
            agolItemProperties.remove(property.getKey());
            if (_log.isInfoEnabled()) {
                _log.info("Entry \"" + property.getKey() + "\" with null value removed.");
            }
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
                    if (_log.isInfoEnabled()) {
                        _log.info(propertyValueClass.toString() + " value of key \"" + property.getKey() + "\" transformed to String value \"" + stringPropertyValue + "\"");
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

        return agolItemProperties;
    }
}