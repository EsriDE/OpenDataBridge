package esride.opendatabridge.pipeline.validator;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 18.02.14
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
 */
public class SRSValidator implements IPipeline {

    private static Logger sLogger = Logger.getLogger(SRSValidator.class);

    private String srsElementName;

    public List<String> allowedSrsList = new ArrayList<String>();

    private String typeName;

    public List<String> matchedServiceTypes;


    public void setAllowedSrsList(String[] allowedSrsList) {
        if(allowedSrsList != null){
            for(int i=0; i<allowedSrsList.length; i++){
                this.allowedSrsList.add(allowedSrsList[i]);
            }
        }
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setMatchedServiceTypes(List<String> matchedServiceTypes) {
        this.matchedServiceTypes = matchedServiceTypes;
    }

    public void setSrsElementName(String srsElementName) {
        this.srsElementName = srsElementName;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String typeValue = elements.get(typeName);
        if(sLogger.isDebugEnabled()){
            sLogger.debug("SRS-Validator (type): " + typeValue);
        }
        if(typeValue != null && matchedServiceTypes.contains(typeValue)){

            String srsList = elements.get(srsElementName);
            if(sLogger.isDebugEnabled()){
                sLogger.debug("SRS-Validator (srs): " + srsList);
            }
            if(srsList != null && srsList.trim().length() > 0){
                boolean hasAtLeastOneAllowedSrs = false;
                StringTokenizer tokenizer = new StringTokenizer(srsList, ",");
                while(tokenizer.hasMoreTokens()){
                    String actualSrs = tokenizer.nextToken().trim();
                    boolean containsSrs = allowedSrsList.contains(actualSrs);
                    if(containsSrs){
                        hasAtLeastOneAllowedSrs = true;
                        break;
                    }
                }
                if(!hasAtLeastOneAllowedSrs){
                    sLogger.info("SRS-Validator: No valid SRS available");
                    throw new InvalidObjectException("SRS-Validator: No valid SRS available. Items SRS values are: " + srsList + ". Allowed SRS values are: "+ allowedSrsList);
                }
            }
        }
    }
}
