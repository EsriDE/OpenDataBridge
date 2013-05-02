package esride.opendatabridge.processinfo.file;

import esride.opendatabridge.processinfo.IProcessInfo;
//import esride.opendatabridge.processinfo.ProcessProperty;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Gets the process information from a property file which is stored in the classpath.
 * User: sma
 * Date: 20.03.13
 * Time: 08:03
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemProcessInfo implements IProcessInfo {
    
    private static Logger sLogger = Logger.getLogger(FileSystemProcessInfo.class);

    private String mPathInfo;

    public void setPathInfo(String pPathInfo) {
        mPathInfo = pPathInfo;
    }

    public HashMap<String, String> getProperties(String processId)  {
        
        //Datei holen
        InputStream propertiesStream = this.getClass().getResourceAsStream(mPathInfo + processId + ".properties");
        Properties prop = new Properties();

        HashMap<String, String> propertyMap= new HashMap<String, String>();
        
        try {
            prop.load(propertiesStream);
            Set<Object> lKeySet =  prop.keySet();
            Iterator<Object> iter =  lKeySet.iterator();
            while(iter.hasNext()){
                Object key = iter.next();
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("Property key: " + key);
                }
                //ProcessProperty processProp = new ProcessProperty();
                propertyMap.put((String)key, prop.getProperty((String)key));
               // processProp.setPropertyValue(prop.getProperty((String)key));
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("Property value: " + prop.getProperty((String)key));
                }
                //lPropertyList.add(processProp);
            }
            
        } catch (IOException e) {
            sLogger.error("Could not load properties file: " + processId, e);
            return null;
        }

        return propertyMap;
    }
}
