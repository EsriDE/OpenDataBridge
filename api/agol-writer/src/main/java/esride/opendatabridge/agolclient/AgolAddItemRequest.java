package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 06.12.13
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
public class AgolAddItemRequest {

    private static Logger sLogger = Logger.getLogger(AgolAddItemRequest.class);

    private AgolItem agolItem;

    private String url;

    public AgolAddItemRequest(AgolItem pAgolItem, String pUrl){
        if(pAgolItem == null){
            throw new IllegalArgumentException("missing AgolItem for addItem operation");
        }
        agolItem = pAgolItem;

        if(pUrl == null){
            throw new IllegalArgumentException("missing url for addItem operation");
        }
        url = pUrl;
    }

    public void executeRequest(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper){


    }
}
