package esride.opendatabridge.httptransport;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.message.BasicHeader;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 08.04.13
 * Time: 18:47
 * To change this template use File | Settings | File Templates.
 */
public class HTTPRequest implements IHTTPRequest{
    private static Logger sLogger = Logger.getLogger(HTTPRequest.class);
    
    private HttpClient client;

    public HTTPRequest() {
        client = new SystemDefaultHttpClient();
    }

    /**
     *
     *
     * @param url the url (with optional request parameters)
     * @return the response body as a input stream
     * @throws IOException  if request fails or response answers with an error code
     */
    public InputStream executeGetRequest(String url, HashMap<String, String> header) throws IOException {
        if(sLogger.isInfoEnabled()){
            sLogger.info("HTTP GET Request: " + url);
        }
        HttpGet getRequest = new HttpGet(url);
        if(header != null){
            getRequest.setHeaders(generateHeader(header));
        }

        HttpResponse response = client.execute(getRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if(sLogger.isInfoEnabled()){
            sLogger.info("Status Code: " + statusCode);
        }

        HttpEntity respEntity = response.getEntity();
        if(respEntity != null){
            return respEntity.getContent();
        } else{
            throw new HttpResponseException(statusCode, "Response is null");
        }


    }
    
    public InputStream executeGetRequest(String baseUrl, String requestParam, HashMap<String, String> header) throws IOException {
        String url = baseUrl + "?" + requestParam;
        return this.executeGetRequest(url, header);
    }
    
    public InputStream executeGetRequest(String baseUrl, HashMap<String, String> requestParamMap, HashMap<String, String> header) throws IOException {
        if(sLogger.isInfoEnabled()){
            sLogger.info("HTTP GET Request: " + baseUrl);
        }

        StringBuilder paramBuffer = new StringBuilder();
        paramBuffer.append(baseUrl);
        if(requestParamMap != null){
            paramBuffer.append("?");
            Set<String> keys = requestParamMap.keySet();
            Iterator<String> keyIter = keys.iterator();
            while (keyIter.hasNext()){
                String key = keyIter.next();
                paramBuffer.append(key);
                paramBuffer.append("=");
                paramBuffer.append(requestParamMap.get(key));
                if(keyIter.hasNext()){
                    paramBuffer.append("&");
                }
            }

        }
        
        HttpGet getRequest = new HttpGet(paramBuffer.toString());
        
        


        if(header != null){
            getRequest.setHeaders(generateHeader(header));
        }

        HttpResponse response = client.execute(getRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if(sLogger.isInfoEnabled()){
            sLogger.info("Status Code: " + statusCode);
        }

        HttpEntity respEntity = response.getEntity();
        if(respEntity != null){
            return respEntity.getContent();
        } else{
            throw new HttpResponseException(statusCode, "Response is null");
        }
    }

    public InputStream executePostRequest(String url, String reqBody, String reqBodyChar, HashMap<String, String> header) throws IOException {
        if(sLogger.isInfoEnabled()){
            sLogger.info("HTTP POST Request: " + url);
        }
        HttpPost postRequest = new HttpPost(url);
        if(header != null){
            postRequest.setHeaders(generateHeader(header));
        }
        HttpEntity entity = new StringEntity(reqBody, reqBodyChar);
        postRequest.setEntity(entity);

        HttpResponse response = client.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if(sLogger.isInfoEnabled()){
            sLogger.info("Status Code: " + statusCode);
        }

        HttpEntity respEntity = response.getEntity();
        if(respEntity != null){
            return respEntity.getContent();
        } else{
            throw new HttpResponseException(statusCode, "Response is null");
        }
    }
    
    private Header[] generateHeader(HashMap<String, String> header){
        Header[] headerArray = new Header[header.size()];
        Set<String> headerKeySet = header.keySet();
        Iterator<String> iter = headerKeySet.iterator();
        int counter = 0;
        while(iter.hasNext()){
            String key = iter.next();
            Header newHeader = new BasicHeader(key, header.get(key));
            headerArray[counter] = newHeader;
            counter++;
        }
        return headerArray;
    }

    public void shutdown(){
        client.getConnectionManager().shutdown();
    }
}
