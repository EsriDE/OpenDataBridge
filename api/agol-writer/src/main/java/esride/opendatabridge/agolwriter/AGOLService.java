package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: gvs
 * Date: 06.03.13
 * Time: 13:22
 */
public class AgolService implements IAgolService {

    private String _userName, _password,_referer, _baseUrl;
    private String _token, _accountId;
    private Long _tokenExpires;
    private static final Logger log = Logger.getLogger(AgolService.class.getName());
    private AgolItemFactory _agolItemFactory;
    FileHandler handler;
    private Map<String, ArrayList<AgolItem>> agolItems = new HashMap<String, ArrayList<AgolItem>>();

    public void setAgolItemFactory(AgolItemFactory agolItemFactory) {
        this._agolItemFactory = agolItemFactory;
    }

    public AgolService(String baseUrl, String userName, String password, String referer, String logPath)
    {
        _baseUrl = baseUrl;
        _userName = userName;
        _password = password;
        _referer = referer;

        // Log Settings
        try {
            handler = new FileHandler(logPath, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        log.addHandler(handler);
        Handler[] handlers = log.getHandlers();
    }

    private void createToken() {
        HttpClient httpclient = new DefaultHttpClient();

        try {
            String generateTokenBaseUrl = "https://www.arcgis.com/sharing/generateToken";

            HttpPost httppost = new HttpPost(generateTokenBaseUrl);

            List <NameValuePair> agolAttributes = new ArrayList <NameValuePair>();
            agolAttributes.add(new BasicNameValuePair("f", "json"));
            agolAttributes.add(new BasicNameValuePair("username", _userName));
            agolAttributes.add(new BasicNameValuePair("password", _password));
            agolAttributes.add(new BasicNameValuePair("referer", _referer));

            // TODO: Encoding funktioniert nicht richtig
            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes, Charset.forName("UTF-8")));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(entities);
                JsonNode tokenNode = rootNode.get("token");
                _token = tokenNode.asText();
                String tokenExpires = rootNode.get("expires").toString();
                _tokenExpires = Long.valueOf(tokenExpires);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();  // Deallocation of all system resources
        }
    }

    private void fillSelfDetails() {
        String selfUrl = _baseUrl + "/sharing/accounts/self";

        List<NameValuePair> agolAttributes = getStandardAgolAttributes();
        agolAttributes.add(new BasicNameValuePair("culture", "de"));

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(selfUrl);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(entities);
                JsonNode idNode = rootNode.get("id");
                _accountId = idNode.asText();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();  // Deallocation of all system resources
        }
    }

    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType) {
        long startTime = System.currentTimeMillis();
        fillAgolItems(itemType, "public", 0);
        log.info(agolItems.size() + " AgolItem objects created in " + (System.currentTimeMillis() - startTime) + " ms.");
        return agolItems;
    }
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType, String accessType) {
        fillAgolItems(itemType, accessType, 0);
        return agolItems;
    }
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType, String accessType, int startWithItemNumber) {
        fillAgolItems(itemType, accessType, startWithItemNumber);
        return agolItems;
    }
    private void fillAgolItems(String itemType, String accessType, int startWithItemNumber) {
        int agolItemsPaginationNextStart;
        int totalItemsCount;
        int retrievedItemsCount;
        int duplicateUrlsCount = 0;

        HttpClient httpclient = new DefaultHttpClient();

        String searchUrl = _baseUrl + "/sharing/search";

        try {
            HttpPost httppost = new HttpPost(searchUrl);

            List<NameValuePair> agolAttributes = getStandardAgolAttributes();

            // get ALL public WMS items that are owned by logged-in user
            String searchString =  "(owner:" + _userName + " AND " + "access:" + accessType + " AND "+ "type:\"" + itemType + "\")";

            // get ALL public WMS items in the organization (accountId)
//            String searchString =  "(accountid:" + _accountId + " AND " + "access:" + accessType + " AND "+ "type:\"" + itemType + "\")";

            // get ALL public WMS items
//            String searchString =  "(access:" + accessType + " AND "+ "type:\"" + itemType + "\")";

            agolAttributes.add(new BasicNameValuePair("q", searchString));
            agolAttributes.add(new BasicNameValuePair("num", "100"));  // Maximum value: 100
            agolAttributes.add(new BasicNameValuePair("start", String.valueOf(startWithItemNumber)));

            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            
            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);
                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode rootNode = objectMapper.readTree(entities);

                // General information
                agolItemsPaginationNextStart = Integer.valueOf(rootNode.get("nextStart").toString());
                totalItemsCount = Integer.valueOf(rootNode.get("total").toString());
                if (agolItemsPaginationNextStart != -1) {
                    retrievedItemsCount = agolItemsPaginationNextStart-1;
                }
                else
                {
                    retrievedItemsCount = Integer.valueOf(rootNode.get("total").toString());
                }

                // Results
                JsonNode resultsNode = rootNode.get("results");
                Iterator resultsIterator = resultsNode.elements();

                while (resultsIterator.hasNext()) {
                    JsonNode result = (JsonNode) resultsIterator.next();
                    String strUrl = result.findValue("url").toString();

                    AgolItem oneItem = _agolItemFactory.createAgolItem(result.toString()); // fromAgolJson

                    boolean contains = agolItems.containsKey(strUrl);
                    if(contains){
                        ArrayList<AgolItem> agolItemArrayList = agolItems.get(strUrl);
                        agolItemArrayList.add(oneItem);
                        log.info("Duplicate entry in ArcGIS Online detected for URL " + strUrl);
                        duplicateUrlsCount++;
                    }
                    else
                    {
                        ArrayList<AgolItem> agolItemArrayList = new ArrayList<AgolItem>();
                        agolItemArrayList.add(oneItem);
                        agolItems.put(strUrl, agolItemArrayList);
                    }
                }
                log.info(retrievedItemsCount + "/" + totalItemsCount + " retrieved. " + duplicateUrlsCount + " duplicate URLs found. " + agolItems.size() + " agolItems with different URLs found.");

                // Recursive call: Items are limited to 100 - if more than that are available, call again
                // ToDo: remove recursive call, use only 1 HttpClient
                if (agolItemsPaginationNextStart!=-1) {
                    fillAgolItems(itemType, "public", agolItemsPaginationNextStart);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();  // Deallocation of all system resources
        }
    }

    public void addItem(AgolItem agolItem) {
        String itemId = createItem(agolItem);
        if (itemId!=null) {
            publishItem(itemId);
        }
    }

    public void updateItem(AgolItem agolItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteItem(AgolItem agolItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private String createItem(AgolItem agolItem) {
        HttpClient httpclient = new DefaultHttpClient();
        String userContentUrl = _baseUrl + "/sharing/content/users/" + _userName +"/addItem";

        try {
            HttpPost httppost = new HttpPost(userContentUrl);

            List<NameValuePair> agolAttributes = getStandardAgolAttributes();
            agolAttributes.addAll(_agolItemFactory.getAgolItemAttributesAsList(agolItem));

            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(entities);

                String strRN = rootNode.toString();
                JsonNode errorNode = rootNode.get("error");
                if (errorNode != null)
                {
                    log.log(Level.SEVERE, "Error " + errorNode.get("code") + ". Create Item failed. " + errorNode.get("message"));
                    return null;
                }
                JsonNode idNode = rootNode.get("id");
                System.out.println(idNode.asText());
                return idNode.asText();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();  // Deallocation of all system resources
        }
        return null;
    }

    private void publishItem(String itemId) {
        HttpClient httpclient = new DefaultHttpClient();
        String userContentUrl = _baseUrl + "/sharing/content/users/" + _userName +"/shareItems";

        try {
            HttpPost httppost = new HttpPost(userContentUrl);

            List<NameValuePair> agolAttributes = getStandardAgolAttributes();

            agolAttributes.add(new BasicNameValuePair("items", itemId));
            // TODO: Wofür braucht es dieses "account"
            agolAttributes.add(new BasicNameValuePair("account", "false"));
            agolAttributes.add(new BasicNameValuePair("everyone", "true"));

            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);
                System.out.println(entities);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    private List<NameValuePair> getStandardAgolAttributes() {
        List <NameValuePair> agolAttributes = new ArrayList<NameValuePair>();
        agolAttributes.add(new BasicNameValuePair("f", "json"));

//        ToDo: Do we need this? Only gets accountId.
//        fillSelfDetails();

        if ((_token == null) || (System.currentTimeMillis() >= _tokenExpires))
        {
            log.info("Creating new token.");
            createToken();
        }
        agolAttributes.add(new BasicNameValuePair("token", _token));
        return agolAttributes;
    }
}
