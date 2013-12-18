package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolwriter.AgolTransactionFailedException;
import esride.opendatabridge.httptransport.HTTPRequest;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class which implements the /share Request (as item owner and as group admin).
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
    private String[] groupsValue;

    public AgolShareItemRequest(String pUrl, String pTokenValue, boolean pEveryoneValue, boolean pOrgValue, String[] pGroupsValue) {
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("Sharetem Request");
            sLogger.debug(tokenParam + ": " + pTokenValue.substring(0,5) + "...");
            sLogger.debug(everyoneParam + ": " + pEveryoneValue);
            sLogger.debug(orgParam + ": " + pOrgValue);
            sLogger.debug(groupsParam + ": " + pGroupsValue.toString());
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

    public AgolAddItemResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper) throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put(formatParam, jsonFormatValueDefault);
        agolAttributes.put(tokenParam, tokenValue);

        agolAttributes.put(everyoneParam, String.valueOf(everyoneValue));
        agolAttributes.put(orgParam, String.valueOf(orgValue));

        if(groupsValue != null){
            StringBuilder buf = new StringBuilder();
            for(int i=0; i<groupsValue.length; i++){
                buf.append(groupsValue[i]);
                if(groupsValue.length -1 != i){
                    buf.append(",");
                }
            }
            agolAttributes.put(groupsParam, buf.toString());
        }


        InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);

        /*if (entities != null)
        {
            JsonNode rootNode = pObjectMapper.readTree(entities);
            JsonNode notSharedWithNode = rootNode.withArray("notSharedWith");//.get("notSharedWith");
            if (notSharedWithNode != null) {
                notSharedWithNode.
            }



                Iterator resultsIterator = resultsNode.iterator();
                while (resultsIterator.hasNext()) {
                    JsonNode resultNode = (JsonNode) resultsIterator.next();
                    JsonNode errorNode = resultNode.get("error");
                    if (errorNode!=null) {
                        if (!errorItems.isEmpty()) {
                            errorItems += "\n";
                        }
                        errorItems += resultNode.get("itemId").toString().replaceAll("\"", "") + ": " + errorNode.get("message") + " (Error code " + errorNode.get("code") + ")";
                    }
                    else {
                        if (!successItems.isEmpty()) {
                            successItems += ", ";
                        }
                        successItems += resultNode.get("itemId").toString().replaceAll("\"", "");
                    }
                }
            }
        }
        if (!errorItems.isEmpty()) {
            throw new AgolTransactionFailedException(errorItems);
        } */
        return null;
    }
}
