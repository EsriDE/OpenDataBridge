package esride.opendatabridge.pipeline.changer;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * 1) puts the foreignIdentifier from the catalog to the ForeignId: <prefix>-id-<resourceType> anhaengen
 * User: sma
 * Date: 11.02.14
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class ForeignKeyChanger implements IPipeline {

    private static Logger sLogger = Logger.getLogger(ForeignKeyChanger.class);

    private String foreignKeyElementName;

    private String tagsElementName;

    private String resourceTypeName;

    private String prefix;

    public void setForeignKeyElementName(String pForeignKeyElementName) {
        foreignKeyElementName = pForeignKeyElementName;
    }

    public void setTagsElementName(String pTagsElementName) {
        tagsElementName = pTagsElementName;
    }

    public void setResourceTypeName(String pResourceTypeName) {
        resourceTypeName = pResourceTypeName;
    }

    public void setPrefix(String pPrefix) {
        prefix = pPrefix;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String foreignKey = elements.get(foreignKeyElementName);
        sLogger.debug("Foreign Key: " + foreignKey);

        String tags = elements.get(tagsElementName);
        String resourceType = elements.get(resourceTypeName);

        String foreignKeyValue = createForeignKey(prefix, foreignKey, resourceType);
        if(tags == null || tags.trim().length() == 0){
            elements.put(tagsElementName, foreignKeyValue);
            elements.put(foreignKeyElementName, foreignKeyValue);
        }else{
            elements.put(tagsElementName, tags + "," + foreignKeyValue);
            elements.put(foreignKeyElementName, foreignKeyValue);
        }
    }

    private String createForeignKey(String prefix, String foreignKey, String resourceType){
        return prefix + "-" + foreignKey + "-" + resourceType.toLowerCase();
    }


}
