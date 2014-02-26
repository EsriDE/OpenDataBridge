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
 * Class which implements the Add Item Request.
 * See API details for a better parameter understanding here: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#/Add_Item/02r30000008q000000/
 * User: Markus Stecker, con terra GmbH
 * Date: 06.12.13
 * Time: 18:10
 */
public class AgolAddItemRequest {

    private static Logger sLogger = Logger.getLogger(AgolAddItemRequest.class);

    private AgolItem agolItem;

    private String url;

    private String tokenValue;
    private static final String tokenParam = "token";


    private static final String formatParam = "f";
    private static final String jsonFormatValueDefault = "json";

    public AgolAddItemRequest(String pUrl, String pTokenValue, AgolItem pAgolItem){
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("AddItem Request");
            sLogger.debug(tokenParam + ": " + pTokenValue.substring(0,5) + "...");
            sLogger.debug("URL: " + pUrl);
        }

        if(pAgolItem == null){
            throw new IllegalArgumentException("missing AgolItem for addItem operation");
        }
        agolItem = pAgolItem;

        //${baseurl}/sharing/rest/content/users/${username}/addItem
        if(pUrl == null){
            throw new IllegalArgumentException("missing url for addItem operation");
        }
        url = pUrl;

        if(pTokenValue == null || pTokenValue.trim().length()==0){
            throw new IllegalArgumentException("missing token for addItem operation");
        }
        tokenValue = pTokenValue;


    }

    public AgolAddItemResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper) throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.putAll(agolItem.getAttributes());
        agolAttributes.put(formatParam, jsonFormatValueDefault);
        agolAttributes.put(tokenParam, tokenValue);

        InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);

        if (entities != null)
        {
            JsonNode rootNode = pObjectMapper.readTree(entities);

            //First check for error Node
            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null) {
                throw new IOException("Adding item \"" + agolItem.getTitle() + " failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }

            JsonNode idNode = rootNode.get("id");
            String itemId;
            if(idNode == null){
                throw new IOException("Adding item \"" + agolItem.getTitle() + " failed. No Id was published");
            }
            itemId = idNode.asText();


            JsonNode folderIdNode = rootNode.get("folder");
            String folderId = null;
            if(folderIdNode != null){
                folderId = folderIdNode.asText();
            }

            boolean success = rootNode.get("success").asBoolean();

            if (sLogger.isInfoEnabled()) {
                sLogger.info("Item \"" + agolItem.getTitle() +"\" (" + agolItem.getType() + ") was added to your ArcGIS Online account with ID " + itemId + ".");
            }

            return new AgolAddItemResponse(itemId, folderId, success);

        }else{
            throw new IOException("No response available");
        }

    }
}
