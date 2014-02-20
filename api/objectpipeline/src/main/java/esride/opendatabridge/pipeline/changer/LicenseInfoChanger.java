package esride.opendatabridge.pipeline.changer;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 19.02.14
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public class LicenseInfoChanger implements IPipeline {

    private static Logger sLogger = Logger.getLogger(LicenseInfoChanger.class);

    private String licenseName;

    public HashMap<String, String> licenseMap;

    public void setLicenseMap(HashMap<String, String> licenseMap) {
        this.licenseMap = licenseMap;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String licenseValue = elements.get(licenseName);
        if(sLogger.isDebugEnabled()){
            sLogger.debug("LicenseInfo-Changer: " + licenseValue);
        }
        if(licenseValue != null){
            StringTokenizer tokenizer = new StringTokenizer(licenseValue, ",");
            StringBuffer buffer = new StringBuffer();
            while(tokenizer.hasMoreTokens()){
                String licenseInfoValue = tokenizer.nextToken().trim();
                if(licenseMap.containsKey(licenseValue)){
                    buffer.append(licenseMap.get(licenseInfoValue));
                }else{
                    buffer.append(licenseInfoValue);
                }

                if(tokenizer.hasMoreTokens()){
                    buffer.append(",");
                }

            }
            elements.put(licenseName, buffer.toString());

        }
    }
}
