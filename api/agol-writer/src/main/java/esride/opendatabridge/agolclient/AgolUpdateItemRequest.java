package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Class which implements the Update Item Request.
 * See API details for a better parameter understanding here: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#/Update_Item/02r30000009s000000/
 * User: Markus Stecker, con terra GmbH
 * Date: 31.01.14
 * Time: 17:10
 */
public class AgolUpdateItemRequest {
    private static Logger sLogger = Logger.getLogger(AgolAddItemRequest.class);

    private AgolItem agolItem;

    private String url;

    private String tokenValue;
    private static final String tokenParam = "token";


    private static final String formatParam = "f";
    private static final String jsonFormatValueDefault = "json";

    public AgolUpdateItemRequest(String pUrl, String pTokenValue, AgolItem pAgolItem){
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("UpdateItem Request");
            sLogger.debug(tokenParam + ": " + pTokenValue.substring(0,5) + "...");
            sLogger.debug("URL: " + pUrl);
        }

        if(pAgolItem == null){
            throw new IllegalArgumentException("missing AgolItem for updateItem operation");
        }
        agolItem = pAgolItem;


        //URL Schema: "{baseurl}/sharing/rest/content/users/{username}/items/{itemid}/update"
        if(pUrl == null){
            throw new IllegalArgumentException("missing url for updateItem operation");
        }
        url = pUrl;

        if(pTokenValue == null || pTokenValue.trim().length()==0){
            throw new IllegalArgumentException("missing token for updateItem operation");
        }
        tokenValue = pTokenValue;


    }

    public AgolUpdateItemResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper) throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.putAll(agolItem.getAttributes());
        agolAttributes.put(formatParam, jsonFormatValueDefault);
        agolAttributes.put(tokenParam, tokenValue);

        InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);

        if (entities != null)
        {
            JsonNode rootNode = pObjectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null) {
                throw new IOException("Update item \"" + agolItem.getTitle() + "\" with ID " + agolItem.getId() + "failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }

            String itemId = rootNode.get("id").asText();
            String folderId = null;
            if(rootNode.get("folder") != null){
                folderId = rootNode.get("folder").asText();
            }

            boolean success = rootNode.get("success").asBoolean();

            if (sLogger.isInfoEnabled()) {
                sLogger.info("Item \"" + agolItem.getTitle() +"\" (" + agolItem.getType() + ") was updated in your ArcGIS Online account " + itemId + ".");
            }

            return new AgolUpdateItemResponse(itemId, folderId, success);

        }else{
            throw new IOException("No response available");
        }

    }
}
