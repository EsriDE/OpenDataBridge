package esride.opendatabridge.pipeline.changer;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 19.02.14
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
public class SrsChanger implements IPipeline{

    public HashMap<String, String> srsMap;

    private String srsElementName;

    public void setSrsMap(HashMap<String, String> srsMap) {
        this.srsMap = srsMap;
    }

    public void setSrsElementName(String srsElementName) {
        this.srsElementName = srsElementName;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String srsValue =  elements.get(srsElementName);
        if(srsValue != null && srsValue.trim().length() > 0){
            StringBuffer buffer = new StringBuffer();
            StringTokenizer srsTokenizer = new StringTokenizer(srsValue, ",");
            while(srsTokenizer.hasMoreTokens()){
                String srsElem = srsTokenizer.nextToken().trim();
                String value = "";
                if(!srsMap.containsKey(srsElem)){
                    if(srsElem.startsWith("CRS:")){
                        value = srsElem.substring(4);
                    }
                    if(srsElem.startsWith("AUTO2:")){
                        value = srsElem.substring(6);
                    }
                    if(srsElem.startsWith("EPSG:")){
                        value = srsElem.substring(5);
                    }

                }else{
                    value = srsMap.get(srsElem);
                }

                buffer.append(value);
                if(srsTokenizer.hasMoreTokens()){
                    buffer.append(",");
                }
            }

            elements.put(srsElementName, buffer.toString());
        }
    }
}
