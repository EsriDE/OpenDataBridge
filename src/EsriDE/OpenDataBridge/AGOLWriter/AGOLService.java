package AGOLWriter;

import Util.AGOLItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 06.03.13
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public class AGOLService implements IWriter{

    private String _userName, _password,_referer, _baseUrl;
    private String _token, _accountId;

    public AGOLService(String baseUrl, String userName, String password, String referer)
    {
        _baseUrl = baseUrl;
        _userName = userName;
        _password = password;
        _referer = referer;
    }

    private String createToken() {
        HttpClient httpclient = new DefaultHttpClient();

        try {
            String generateTokenBaseUrl = "https://www.arcgis.com/sharing/generateToken";

            HttpPost httppost = new HttpPost(generateTokenBaseUrl);

            List <NameValuePair> agolAttributes = new ArrayList <NameValuePair>();
            agolAttributes.add(new BasicNameValuePair("f", "json"));
            agolAttributes.add(new BasicNameValuePair("username", _userName));
            agolAttributes.add(new BasicNameValuePair("password", _password));
            agolAttributes.add(new BasicNameValuePair("referer", _referer));

            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(entities);
                JsonNode tokenNode = rootNode.get("token");
                return tokenNode.asText();
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

    private String getAccountId() {
        String selfUrl = _baseUrl + "/sharing/accounts/self";

        List<NameValuePair> agolAttributes = getStandardAGOLAttributes();
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

    @Override
    public List <String> getResourceUrls() {
        _token = createToken();
        System.out.println(_token);
        _accountId = getAccountId();
        System.out.println(_accountId);

        List <String> urlStrings = new ArrayList <String>();

        HttpClient httpclient = new DefaultHttpClient();
        String searchUrl = _baseUrl + "/sharing/search";

        try {
            HttpPost httppost = new HttpPost(searchUrl);

            List<NameValuePair> agolAttributes = getStandardAGOLAttributes();

            String searchString =  "(accountid:" + _accountId + " AND " + "access:public"+ " AND "+ "type:\"WMS\")";
            agolAttributes.add(new BasicNameValuePair("q", searchString));
            agolAttributes.add(new BasicNameValuePair("num", "100"));


            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                String entities = EntityUtils.toString(entity);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(entities);
                List<JsonNode> urlNodes = rootNode.findValues("url");

                for ( JsonNode node : urlNodes )
                {
                    urlStrings.add(node.asText());
                    System.out.println(node.asText());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();  // Deallocation of all system resources
        }

        return urlStrings;
    }



    @Override
    public void addItem(AGOLItem agolItem) {

        HttpClient httpclient = new DefaultHttpClient();
        String userContentUrl = _baseUrl + "/sharing/content/users/" + _userName +"/addItem";

        try {
            HttpPost httppost = new HttpPost(userContentUrl);

            List<NameValuePair> agolAttributes = getStandardAGOLAttributes();
            for (String key : agolItem.getAttributes().keySet() )
            {
                String agolKey = key;
                if (key.startsWith("agol."))
                {
                    agolKey = key.substring(5);
                }

                agolAttributes.add(new BasicNameValuePair(agolKey, agolItem.getAttributes().get(key)));
            }

            httppost.setEntity(new UrlEncodedFormEntity(agolAttributes));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            httpclient.getConnectionManager().shutdown();  // Deallocation of all system resources
        }
    }

    private List<NameValuePair> getStandardAGOLAttributes() {
        List <NameValuePair> agolAttributes = new ArrayList<NameValuePair>();
        agolAttributes.add(new BasicNameValuePair("f", "json"));
        agolAttributes.add(new BasicNameValuePair("token", _token));
        return agolAttributes;
    }
}
