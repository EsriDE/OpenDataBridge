package esride.opendatabridge.pipeline.validator;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 20.02.14
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class UrlNotEmptyValidator implements IPipeline {

    private static Logger sLogger = Logger.getLogger(UrlNotEmptyValidator.class);

    public List<String> typeList = new ArrayList<String>();

    public String typeName;

    public String urlName;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public void setTypeList(String[] typeList){
        if(typeList != null){
            for(int i=0; i<typeList.length; i++){
                this.typeList.add(typeList[i]);
            }
        }
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String typeValue = elements.get(typeName);
        if(typeList.contains(typeValue)){
            String urlValue = elements.get(urlName);
            if(urlValue == null || urlValue.trim().length() == 0 || !urlValue.startsWith("http")){
                sLogger.warn("UrlNotEmpty-Validator: invalid url value");
                throw new InvalidObjectException("UrlNotEmpty-Validator: invalid url value");
            }
        }
    }
}
