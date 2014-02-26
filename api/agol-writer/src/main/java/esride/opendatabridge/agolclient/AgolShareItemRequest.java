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
 * Class which implements the Share Item Request (as item owner).
 * See API details for a better parameter understanding here: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#//02r30000007s000000
 * User: Markus Stecker, con terra GmbH
 * Date: 16.12.13
 * Time: 08:21
 */
public class AgolShareItemRequest {

    private static Logger sLogger = Logger.getLogger(AgolShareItemRequest.class);

    private String url;

    private String tokenValue;
    private static final String tokenParam = "token";


    private static final String formatParam = "f";
    private static final String jsonFormatValueDefault = "json";

    private static final String everyoneParam = "everyone";
    private boolean everyoneValue;

    private static final String orgParam = "org";
    private boolean orgValue;

    private static final String groupsParam = "groups";
    private List<String> groupsValue;

    public AgolShareItemRequest(String pUrl, String pTokenValue, boolean pEveryoneValue, boolean pOrgValue, List<String> pGroupsValue) {
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("ShareItem Request");
            sLogger.debug(tokenParam + ": " + pTokenValue.substring(0,5) + "...");
            sLogger.debug(everyoneParam + ": " + pEveryoneValue);
            sLogger.debug(orgParam + ": " + pOrgValue);
            sLogger.debug(groupsParam + ": " + pGroupsValue);
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

        everyoneValue = pEveryoneValue;
        orgValue = pOrgValue;
        groupsValue = pGroupsValue;
    }

    public AgolShareItemResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper) throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put(formatParam, jsonFormatValueDefault);
        agolAttributes.put(tokenParam, tokenValue);

        agolAttributes.put(everyoneParam, String.valueOf(everyoneValue));
        agolAttributes.put(orgParam, String.valueOf(orgValue));

        if(groupsValue != null){
            StringBuilder buf = new StringBuilder();
            for(int i=0; i<groupsValue.size(); i++){
                buf.append(groupsValue.get(i));
                if(groupsValue.size() -1 != i){
                    buf.append(",");
                }
            }
            agolAttributes.put(groupsParam, buf.toString());
        }


        InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);

        if (entities != null)
        {
            JsonNode rootNode = pObjectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null) {
                throw new IOException("Share item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }

            JsonNode notSharedWithNode = rootNode.withArray("notSharedWith");
            List<String> notSharedWithList = null;
            if (notSharedWithNode != null) {
                Iterator<JsonNode> notSharedWithGroupsArray =  notSharedWithNode.iterator();
                while(notSharedWithGroupsArray.hasNext()){
                    sLogger.debug("Not shared with goup: " + notSharedWithGroupsArray.next().asText());
                }
            }
            JsonNode itemIdNode = rootNode.get("itemId");
            String itemId;
            if(itemIdNode != null){
                itemId = itemIdNode.asText();
            }else{
                throw new IOException("No item id was updated");
            }

            return new AgolShareItemResponse(notSharedWithList, itemId);

    }

    return null;
    }
}
