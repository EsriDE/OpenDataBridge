package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolwriter.AgolTransactionFailedException;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Class which implements the /addItem Request.
 * See API details for a better parameter understanding here: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#/Add_Item/02r30000008q000000/
 * User: Markus Stecker, con terra GmbH
 * Date: 06.12.13
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
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

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null) {
                throw new IOException("Adding item \"" + agolItem.getTitle() + "\" with ID " + agolItem.getId() + "failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }

            String itemId = rootNode.get("id").asText();
            String folderId = null;
            if(!rootNode.get("folder").isNull()){
                folderId = rootNode.get("folder").asText();
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
