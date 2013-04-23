package esride.opendatabridge.item;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 23.04.13
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class AgolItemFactory {

    ObjectMapper objectMapper;

    public AgolItemFactory() {
        objectMapper = new ObjectMapper();
    }

    public AgolItem createAgolItem(String jsonAGOLItem)
    {
        HashMap agolItemProperties = new HashMap();
        try {
            agolItemProperties = objectMapper.readValue(jsonAGOLItem, HashMap.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // ToDo: jsonAgolItem enthält NICHT <String, AgolJsonString>, sondern die simplen Key-Value-Pairs => manuell zusammenbauen!
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }
    public AgolItem createAgolItem(HashMap agolItemProperties)
    {
        // ToDo: HashMap enthält NICHT <String, AgolJsonString>, sondern die simplen Key-Value-Pairs => manuell zusammenbauen!
        AgolItem agolItem = new AgolItem(agolItemProperties);
        return agolItem;
    }

    public String agolItemToJson(AgolItem agolItem)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter strWriter = new StringWriter();
        try {
            objectMapper.writeValue(strWriter, agolItem.getAttributes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strWriter.toString();
    }
}