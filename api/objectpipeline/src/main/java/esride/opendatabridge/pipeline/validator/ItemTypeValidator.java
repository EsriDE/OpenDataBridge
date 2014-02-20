package esride.opendatabridge.pipeline.validator;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 19.02.14
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class ItemTypeValidator implements IPipeline {

    private String typeName;

    private List<String> allowedTypeNames = new ArrayList<String>();

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setAllowedTypeNames(String[] allowedTypeNames) {
        if(allowedTypeNames != null){
            for(int i=0; i<allowedTypeNames.length; i++){
                this.allowedTypeNames.add(allowedTypeNames[i]);
            }
        }
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        String typeValue = elements.get(typeName);

        if(!allowedTypeNames.contains(typeValue)){
            throw new InvalidObjectException("ItemType-Validator: The item with the type " + typeValue + " is not allowed. Allowed values are: " + allowedTypeNames);
        }
    }
}
