package esride.opendatabridge.httptransport;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 11.04.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public interface IHTTPRequest {

    public InputStream executeGetRequest(String url, HashMap<String, String> header) throws IOException;

    public InputStream executeGetRequest(String baseUrl, String requestParam, HashMap<String, String> header) throws IOException;

    public InputStream executePostRequest(String url, String reqBody, String reqBodyChar, HashMap<String, String> header) throws IOException ;
}
