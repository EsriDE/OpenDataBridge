package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolwriter.AgolItemInvalidException;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class which implements the Search Request.
 * See API details for a better parameter understanding here: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#/Search/02r3000000mp000000/
 * User: Markus Stecker, con terra GmbH
 * Date: 29.01.14
 * Time: 17:22
 */
public class AgolSearchRequest {

    private static Logger sLogger = Logger.getLogger(AgolSearchRequest.class);

    private String url;

    private String tokenValue;
    private static final String tokenParam = "token";

    private String searchStringValue;
    private static final String searchStringParam = "q";

    private String maxnumValue;
    private static final String maxnumParam = "num";
    private static final String maxnumValueDefault = "100";

    private String startValue;
    private static final String startParam = "start";
    private static final String startValueDefault = "0";

    private static final String formatParam = "f";
    private static final String jsonFormatValueDefault = "json";

    public AgolSearchRequest(String pUrl, String pTokenValue, String pSearchStringValue, String pMaxnumValue, String pStartValue) {
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("Search Request");
            sLogger.debug(tokenParam + ": " + pTokenValue.substring(0,5) + "...");
            sLogger.debug("URL: " + pUrl);
            sLogger.debug("StartParameter: " + pStartValue);
        }

        if(pUrl == null){
            throw new IllegalArgumentException("missing URL for search operation");
        }
        url = pUrl;

        if(pTokenValue == null || pTokenValue.trim().length()==0){
            throw new IllegalArgumentException("missing token for search operation");
        }
        tokenValue = pTokenValue;
        if(pSearchStringValue == null || pSearchStringValue.trim().equals("")){
            throw new IllegalArgumentException("missing search string for search operation");
        } else{
            searchStringValue = pSearchStringValue;
        }

        if(pMaxnumValue == null){
            maxnumValue = maxnumValueDefault;
        }else{
            maxnumValue = pMaxnumValue;
        }


        if(pStartValue == null){
            startValue = startValueDefault;
        }else{
            startValue = pStartValue;
        }
    }

        public AgolSearchResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper, AgolItemFactory pAgolItemFactory) throws IOException {
            HashMap<String, String> agolAttributes = new HashMap<String, String>();
            agolAttributes.put(formatParam, jsonFormatValueDefault);
            agolAttributes.put(tokenParam, tokenValue);
            agolAttributes.put(searchStringParam, searchStringValue);
            agolAttributes.put(startParam, startValue);
            agolAttributes.put(maxnumParam, maxnumValue);

            InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);
            if (entities != null){
                JsonNode rootNode = pObjectMapper.readTree(entities);

                //First check for error Node
                JsonNode errorNode = rootNode.get("error");
                if (errorNode != null) {
                    throw new IOException("Search item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
                }

                JsonNode nextStartNode = rootNode.get("nextStart");
                if(nextStartNode == null){
                    throw new IOException("No element nextStart was found in search response");
                }
                int nextStart = nextStartNode.asInt();

                JsonNode totalNode = rootNode.get("total");
                if(totalNode == null){
                    throw new IOException("No element total was found in search response");
                }
                int total = totalNode.asInt();
                if (sLogger.isDebugEnabled()) {
                    sLogger.debug("Search Response");
                    sLogger.debug("Total number of hits: " + totalNode);
                    sLogger.debug("Next Start Value: " + nextStart);
                }
                AgolSearchResponse response = new AgolSearchResponse(nextStart, total);
                JsonNode resultsNode = rootNode.get("results");
                Iterator resultsIterator = resultsNode.elements();

                while (resultsIterator.hasNext()) {
                    JsonNode result = (JsonNode) resultsIterator.next();
                    //String strUrl = result.findValue("url").asText();
                    try {
                        AgolItem oneItem = pAgolItemFactory.createAgolItem(result.toString());
                        response.addAgolItemToList(oneItem);
                    } catch (AgolItemInvalidException e) {
                        sLogger.error("create AgolItem failed:  " + e.getMessage());
                        throw new IOException(e);

                    }
                }
                return response;
            } else{
                throw new IOException("No response available");
            }

        }

}
