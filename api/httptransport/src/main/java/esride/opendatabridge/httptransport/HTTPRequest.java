package esride.opendatabridge.httptransport;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.message.BasicHeader;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
    }

    /**
     *
     *
     * @param url the url (with optional request parameters)
     * @return the response body as a input stream
     * @throws IOException  if request fails or response answers with an error code
     */
    public InputStream executeGetRequest(String url, HashMap<String, String> header) throws IOException {

        HttpGet getRequest = new HttpGet(url);
        if(header != null){
            getRequest.setHeaders(generateHeader(header));
        }

        if(sLogger.isInfoEnabled()){
            sLogger.info("HTTP GET Request: " + url);
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

        if(sLogger.isInfoEnabled()){
            sLogger.info("HTTP GET Request: " + paramBuffer.toString());
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

    public InputStream executePostRequest(String url, HashMap<String, String> content, HashMap<String, String> header) throws IOException {
        if(sLogger.isInfoEnabled()){
            sLogger.info("HTTP POST Request: " + url);
        }
        HttpPost postRequest = new HttpPost(url);
        if(header != null){
            postRequest.setHeaders(generateHeader(header));
        }

        UrlEncodedFormEntity contentEntity = contentMapToUrlEncodedFormEntity(content);
        postRequest.setEntity(contentEntity);

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

    /**
     * Transform HashMap of Strings to UrlEncodedFormEntity
     * @param content
     * @return content as UrlEncodedFormEntity
     * @throws IOException
     */
    private UrlEncodedFormEntity contentMapToUrlEncodedFormEntity(HashMap<String, String> content) throws IOException {
        List<NameValuePair> contentList = new ArrayList<NameValuePair>();
        Iterator iter = content.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry property = (Map.Entry) iter.next();
            String propertyKey = property.getKey().toString();
            String propertyValue = property.getValue().toString();
            contentList.add(new BasicNameValuePair(propertyKey, propertyValue));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(contentList, "UTF-8");
        return urlEncodedFormEntity;
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
