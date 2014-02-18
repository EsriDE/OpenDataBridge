package esride.opendatabridge.pipeline.changer;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 11.02.14
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class UrlChanger implements IPipeline{


    private static Logger sLogger = Logger.getLogger(UrlChanger.class);

    private String urlElementName;

    public void setUrlElementName(String pUrlElementName) {
        urlElementName = pUrlElementName;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String url1 = elements.get(urlElementName);
        sLogger.debug("Change URL: " + url1);
        if(url1 != null && url1.trim().length() > 0){
            String url2;
            if(url1.length() > 1 && url1.charAt(url1.length() -1) == '?'){
                url2 =  url1.substring(0, url1.length() -1);
                elements.put(urlElementName, url2);
                sLogger.debug("Change URL from " + url1 + " to " + url2);
            }
        }
    }
}
