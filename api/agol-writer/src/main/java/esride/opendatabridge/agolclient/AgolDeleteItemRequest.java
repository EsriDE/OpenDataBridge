package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.httptransport.HTTPRequest;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 02.02.14
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public class AgolDeleteItemRequest {

    private static Logger sLogger = Logger.getLogger(AgolDeleteItemRequest.class);

    private String url;

    private String tokenValue;
    private static final String tokenParam = "token";


    private static final String formatParam = "f";
    private static final String jsonFormatValueDefault = "json";

    public AgolDeleteItemRequest(String pUrl, String pTokenValue) {
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("Sharetem Request");
            sLogger.debug(tokenParam + ": " + pTokenValue.substring(0,5) + "...");
            sLogger.debug("URL: " + pUrl);
        }

        if(pUrl == null){
            throw new IllegalArgumentException("missing url for share operation");
        }
        url = pUrl;

        if(pTokenValue == null || pTokenValue.trim().length()==0){
            throw new IllegalArgumentException("missing token for addItem operation");
        }
        tokenValue = pTokenValue;
    }

    public AgolDeleteItemResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper) throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put(formatParam, jsonFormatValueDefault);
        agolAttributes.put(tokenParam, tokenValue);


        InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);

        if (entities != null)
        {

            JsonNode rootNode = pObjectMapper.readTree(entities);

            //First check for error Node
            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null) {
                throw new IOException("Deleting item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }

            JsonNode successNode = rootNode.get("success");
            boolean sucess;
            if(successNode != null){
                sucess = successNode.asBoolean();
            }else{
                throw new IOException("No success information available");
            }

            JsonNode itemIdNode = rootNode.get("itemId");
            String itemId;
            if(itemIdNode != null){
                itemId = itemIdNode.asText();
            }else{
                throw new IOException("No item was deleted");
            }

            return new AgolDeleteItemResponse(sucess, itemId);

        }

        return null;
    }
}
