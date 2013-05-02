package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * User: gvs
 * Date: 06.03.13
 * Time: 13:22
 */
public class AgolService implements IAgolService {

    private String _userName, _password,_referer, _token;
    private String _baseUrl, _userContentUrl;
    private Long _tokenExpires;
    private static final Logger log = Logger.getLogger(AgolService.class);
    private AgolItemFactory _agolItemFactory;
    private Map<String, ArrayList<AgolItem>> agolItems = new HashMap<String, ArrayList<AgolItem>>();
    private HTTPRequest _httpRequest;

    public void setAgolItemFactory(AgolItemFactory agolItemFactory) {
        this._agolItemFactory = agolItemFactory;
    }
    public void set_httpRequest(HTTPRequest _httpRequest) {
        this._httpRequest = _httpRequest;
    }

    public AgolService(String baseUrl, String userName, String password, String referer)
    {
        _baseUrl = baseUrl;
        _userName = userName;
        _password = password;
        _referer = referer;

        _userContentUrl = _baseUrl + "/sharing/content/users/" + _userName;
    }

    private void createToken() throws IOException {
        String generateTokenBaseUrl = "https://www.arcgis.com/sharing/generateToken";

        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put("f", "json");
        agolAttributes.put("username", _userName);
        agolAttributes.put("password", _password);
        agolAttributes.put("referer", _referer);

        InputStream entities = _httpRequest.executePostRequest(generateTokenBaseUrl, agolAttributes, null);

        if (entities != null)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(entities);
            JsonNode tokenNode = rootNode.get("token");
            _token = tokenNode.asText();
            String tokenExpires = rootNode.get("expires").toString();
            _tokenExpires = Long.valueOf(tokenExpires);
        }
    }

    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType) throws IOException {
        long startTime = System.currentTimeMillis();
        fillAgolItems(itemType, "public", 0);
        if (log.isInfoEnabled()) {
            log.info(agolItems.size() + " AgolItem objects created in " + (System.currentTimeMillis() - startTime) + " ms.");
        }
        return agolItems;
    }
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType, String accessType) throws IOException {
        fillAgolItems(itemType, accessType, 0);
        return agolItems;
    }
    private void fillAgolItems(String itemType, String accessType, int startWithItemNumber) throws IOException {
        int agolItemsPaginationNextStart;
        int totalItemsCount;
        int retrievedItemsCount;
        int duplicateUrlsCount = 0;

        String searchUrl = _baseUrl + "/sharing/search";
        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        // get ALL public WMS items that are owned by logged-in user
        String searchString =  "(owner:" + _userName + " AND " + "access:" + accessType + " AND "+ "type:\"" + itemType + "\")";

        // get ALL public WMS items in the organization (accountId)
//            String searchString =  "(accountid:" + _accountId + " AND " + "access:" + accessType + " AND "+ "type:\"" + itemType + "\")";

        // get ALL public WMS items
//            String searchString =  "(access:" + accessType + " AND "+ "type:\"" + itemType + "\")";

        agolAttributes.put("q", searchString);
        agolAttributes.put("num", "100");  // Maximum value: 100
        agolAttributes.put("start", String.valueOf(startWithItemNumber));

        InputStream entities = _httpRequest.executePostRequest(searchUrl, agolAttributes, null);

        if (entities != null)
        {
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
                    if (log.isInfoEnabled()) {
                        log.info("Duplicate entry in ArcGIS Online detected for URL " + strUrl);
                    }
                    duplicateUrlsCount++;
                }
                else
                {
                    ArrayList<AgolItem> agolItemArrayList = new ArrayList<AgolItem>();
                    agolItemArrayList.add(oneItem);
                    agolItems.put(strUrl, agolItemArrayList);
                }
            }
            if (log.isInfoEnabled()) {
                log.info(retrievedItemsCount + "/" + totalItemsCount + " retrieved. " + duplicateUrlsCount + " duplicate URLs found. " + agolItems.size() + " agolItems with different URLs found.");
            }

            // Recursive call: Items are limited to 100 - if more than that are available, call again
            if (agolItemsPaginationNextStart!=-1) {
                fillAgolItems(itemType, "public", agolItemsPaginationNextStart);
            }
        }
    }

    public void updateItem(AgolItem agolItem) throws IOException, AgolItemTransactionFailedException {
        String userItemUrl = _userContentUrl + "/items/" + agolItem.getId();
        String updateItemUrl = userItemUrl + "/update";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();
        agolAttributes.putAll(agolItem.getAttributes());

        InputStream entities = _httpRequest.executePostRequest(updateItemUrl, agolAttributes, null);
        if (entities != null)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null)
            {
                throw new AgolItemTransactionFailedException("Update Item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }
        }
    }

    public void deleteItem(AgolItem agolItem) throws IOException, AgolItemTransactionFailedException {
        String userItemUrl = _userContentUrl + "/items/" + agolItem.getId();
        String deleteItemUrl = userItemUrl + "/delete";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        InputStream entities = _httpRequest.executePostRequest(deleteItemUrl, agolAttributes, null);
        if (entities != null)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null)
            {
                throw new AgolItemTransactionFailedException("Delete Item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }
        }
    }

    // ToDo: "access" Parameter, ob private, public oder an Gruppe geteilt
    public void addItems(List<AgolItem> agolItems) throws AgolItemTransactionFailedException, IOException {
        String itemIds = "";
        for (AgolItem agolItem : agolItems) {
            if (itemIds.length()>0) {
                itemIds += ",";
            }
            itemIds += createItem(agolItem);
        }
        if (itemIds!=null) {
            shareItems(itemIds);
        }
    }
    public String addItem(AgolItem agolItem) throws AgolItemTransactionFailedException, IOException {
        String itemId = createItem(agolItem);
        if (itemId!=null) {
            shareItems(itemId);
        }
        return itemId;
    }

    private String createItem(AgolItem agolItem) throws AgolItemTransactionFailedException, IOException {
        String addItemUrl = _userContentUrl + "/addItem";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();
        agolAttributes.putAll(agolItem.getAttributes());

        InputStream entities = _httpRequest.executePostRequest(addItemUrl, agolAttributes, null);

        if (entities != null)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null)
            {
                throw new AgolItemTransactionFailedException("Create Item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }
            JsonNode idNode = rootNode.get("id");
            return idNode.asText();
        }
        return null;
    }

    /**
     * Share Items
     * @param itemIds: Comma-separated string of items that shall be shared publically.
     */
    private void shareItems(String itemIds) throws IOException {
        String publishItemUrl = _userContentUrl + "/shareItems";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        agolAttributes.put("items", itemIds);
        agolAttributes.put("everyone", "true");

        InputStream entities = _httpRequest.executePostRequest(publishItemUrl, agolAttributes, null);
    }

    private HashMap<String, String> getStandardAgolAttributes() throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put("f", "json");

        if ((_token == null) || (System.currentTimeMillis() >= _tokenExpires))
        {
            if (log.isInfoEnabled()) {
                log.info("Creating new token.");
            }
            createToken();
        }
        agolAttributes.put("token", _token);
        return agolAttributes;
    }
}
