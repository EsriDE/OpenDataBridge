package esride.opendatabridge.itemtransform.file;

import esride.opendatabridge.itemtransform.IItemGeneratorConfiguration;
import esride.opendatabridge.itemtransform.ItemGenerationException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 05.04.13
 * Time: 10:40
 * To change this template use File | Settings | File Templates.
 */
public class FileSysItemGeneratorConfiguration implements IItemGeneratorConfiguration{
    private static Logger sLogger = Logger.getLogger(FileSysItemGeneratorConfiguration.class);
    
    public HashMap<String, Properties> cachedProperties = new HashMap<String, Properties>();
    
    public Properties getItemGeneratorConfiguration(String processId, String resourceType) {
        String cacheId = processId + "|" + resourceType;
        if(cachedProperties.containsKey(cacheId)){
            return cachedProperties.get(cacheId); 
        }

        InputStream propertyStream = this.getClass().getResourceAsStream("/itemgenerator/" + processId + "/itemelement_" + resourceType + ".properties");
        Properties properties = new Properties();
        try {
            properties.load(propertyStream);
        } catch (IOException e) {
            String lMessage = "Property file for the processId: " + processId + " and resourceType: " + resourceType + " could not be loaded.";
            sLogger.error(lMessage, e);
            throw new ItemGenerationException(lMessage, e);
        }
        cachedProperties.put(cacheId, properties);
        return properties;
    }
}
