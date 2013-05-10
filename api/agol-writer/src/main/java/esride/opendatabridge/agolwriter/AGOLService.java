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
    private String _baseUrl, _rootUrl, _userContentUrl, _userCommunityUrl;
    private Long _tokenExpires;
    private static final Logger log = Logger.getLogger(AgolService.class);
    private AgolItemFactory _agolItemFactory;
    private Map<String, ArrayList<AgolItem>> _agolItems = new HashMap<String, ArrayList<AgolItem>>();
    private HTTPRequest _httpRequest;
    private ObjectMapper _objectMapper;

    /**
     * Setter for _agolItemFactory
     * @param agolItemFactory
     */
    public void set_AgolItemFactory(AgolItemFactory agolItemFactory) {
        this._agolItemFactory = agolItemFactory;
    }

    /**
     * Setter for _httpRequest
     * @param httpRequest
     */
    public void set_httpRequest(HTTPRequest httpRequest) {
        this._httpRequest = httpRequest;
    }

    /**
     * Setter for _objectMapper
     * @param objectMapper
     */
    public void set_objectMapper(ObjectMapper objectMapper) {
        this._objectMapper = objectMapper;
    }

    /**
     * Constructor
     * @param baseUrl
     * @param userName
     * @param password
     * @param referer
     */
    public AgolService(String baseUrl, String userName, String password, String referer)
    {
        _baseUrl = baseUrl;
        _userName = userName;
        _password = password;
        _referer = referer;

        _rootUrl = _baseUrl + "/sharing/";
        _userContentUrl = _rootUrl + "/content/users/" + _userName;
        _userCommunityUrl = _rootUrl + "/community/users/" + _userName;
    }

    /**
     * Create ArcGIS Online token for user credentials
     * @throws IOException
     */
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
            JsonNode rootNode = _objectMapper.readTree(entities);
            JsonNode tokenNode = rootNode.get("token");
            _token = tokenNode.asText();
            String tokenExpires = rootNode.get("expires").toString();
            _tokenExpires = Long.valueOf(tokenExpires);
        }
    }

    /**
     * Get IDs of the groups the logged-in user is a member of
     * @return Comma-separated userGroupIds
     * @throws IOException
     */
    public String getUserGroupIds() throws IOException {
        String userGroupIds = "";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        InputStream entities = _httpRequest.executePostRequest(_userCommunityUrl, agolAttributes, null);
        if (entities != null)
        {
            JsonNode rootNode = _objectMapper.readTree(entities);

            JsonNode groupsNode = rootNode.get("groups");
            Iterator groupsIterator = groupsNode.elements();

            while (groupsIterator.hasNext()) {
                JsonNode group = (JsonNode) groupsIterator.next();
                if (userGroupIds.length()>0) {
                    userGroupIds += ",";
                }
                userGroupIds += group.get("id").toString().replace("\"", "");
            }
        }
         return userGroupIds;
    }




    /* ToDo:
        - itemType und accessType als String-Array o.ä., über (... OR ...) abfragen.
        - überschreiben mit Suchbeschränkung auf owner=_userName, Gruppen=groupId(s) und Organisation=accountid
        - separat: public Getter für Gruppen anbieten, denen der eingeloggte User angehört
      */

    /**
     * Get all public items
     * @param itemType: WMS | CSV | ...
     * @return
     * @throws IOException
     */
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType) throws IOException {
        fillAgolItems(itemType, "public", 0);
        return _agolItems;
    }
    /**
     * Get all items with selectable access type
     * @param itemType: WMS | CSV | ...
     * @param accessType
     * @return
     * @throws IOException
     */
    public Map<String, ArrayList<AgolItem>> getAllItems(String itemType, String accessType) throws IOException {
        fillAgolItems(itemType, accessType, 0);
        return _agolItems;
    }
    /**
     * Get items request
     * @param itemType: WMS | CSV | ...
     * @param accessType
     * @param startWithItemNumber: Integer value for recursive calls - 100 items are the maximum return number for one HTTP request
     * @throws IOException
     */
    private void fillAgolItems(String itemType, String accessType, int startWithItemNumber) throws IOException {
        int agolItemsPaginationNextStart;
        int totalItemsCount;
        int retrievedItemsCount;
        int duplicateUrlsCount = 0;

        String searchUrl = _rootUrl + "/search";
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
            JsonNode rootNode = _objectMapper.readTree(entities);

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

                boolean contains = _agolItems.containsKey(strUrl);
                if(contains){
                    List<AgolItem> agolItemArrayList = _agolItems.get(strUrl);
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
                    _agolItems.put(strUrl, agolItemArrayList);
                }
            }
            if (log.isInfoEnabled()) {
                log.info(retrievedItemsCount + "/" + totalItemsCount + " retrieved. " + duplicateUrlsCount + " duplicate URLs found. " + _agolItems.size() + " _agolItems with different URLs found.");
            }

            // Recursive call: Items are limited to 100 - if more than that are available, call again
            if (agolItemsPaginationNextStart!=-1) {
                fillAgolItems(itemType, "public", agolItemsPaginationNextStart);
            }
        }
    }

    /**
     * Add a list of items and share publically
     * @param agolItems
     * @return Comma-separated list of added itemIDs
     * @throws AgolItemTransactionFailedException
     * @throws IOException
     */
    public String addItems(List<AgolItem> agolItems) throws AgolItemTransactionFailedException, IOException {
        return addItems(agolItems, AccessType.PUBLIC);
    }
    /**
     * Add a list of items and share with selectable access type
     * @param agolItems
     * @param accessType
     * @return Comma-separated list of added itemIDs
     * @throws AgolItemTransactionFailedException
     * @throws IOException
     */
    public String addItems(List<AgolItem> agolItems, AccessType accessType) throws AgolItemTransactionFailedException, IOException {
        return addItems(agolItems, accessType, "");
    }
    /**
     * Add a list of items and share with selectable access type and groups
     * @param agolItems
     * @param accessType
     * @param groupIds: Comma-separated list of groupIDs the items shall be shared with
     * @return Comma-separated list of added itemIDs
     * @throws AgolItemTransactionFailedException
     * @throws IOException
     */
    public String addItems(List<AgolItem> agolItems, AccessType accessType, String groupIds) throws AgolItemTransactionFailedException, IOException {
        String itemIds = "";
        for (AgolItem agolItem : agolItems) {
            if (itemIds.length()>0) {
                itemIds += ",";
            }
            itemIds += createItem(agolItem);
        }
        if (itemIds!=null && !accessType.equals(AccessType.PRIVATE)) {
            shareItems(itemIds, accessType, groupIds);
        }
        return itemIds;
    }

    /**
     * Create item
     * @param agolItem
     * @return the ID of the created item
     * @throws AgolItemTransactionFailedException
     * @throws IOException
     */
    private String createItem(AgolItem agolItem) throws AgolItemTransactionFailedException, IOException {
        String addItemUrl = _userContentUrl + "/addItem";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();
        agolAttributes.putAll(agolItem.getAttributes());

        InputStream entities = _httpRequest.executePostRequest(addItemUrl, agolAttributes, null);

        if (entities != null)
        {
            JsonNode rootNode = _objectMapper.readTree(entities);

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
     * Share items to everyone, to your organization or to specific groups
     * @param itemIds
     * @param accessType
     * @param groupIds
     * @throws IOException
     */
    private void shareItems(String itemIds, AccessType accessType, String groupIds) throws IOException {
        String publishItemUrl = _userContentUrl + "/shareItems";
        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        agolAttributes.put("items", itemIds);
        if (accessType.equals(AccessType.PUBLIC)) {
            agolAttributes.put("everyone", "true");
        }
        else if (accessType.equals(AccessType.ORG)) {
            agolAttributes.put("account", "true");
        }
//        if (accessType.equals(AccessType.PUBLIC)) {
//            agolAttributes.put("everyone", "true");
//            agolAttributes.put("account", "false");
//        }
//        else if (accessType.equals(AccessType.ORG)) {
//            agolAttributes.put("everyone", "false");
//            agolAttributes.put("account", "true");
//        }
//        else {
//            agolAttributes.put("everyone", "false");
//            agolAttributes.put("account", "false");
//        }
        agolAttributes.put("groups", groupIds);
        InputStream entities = _httpRequest.executePostRequest(publishItemUrl, agolAttributes, null);
    }

    /**
     * Update a specific item
     * @param agolItem
     * @throws IOException
     * @throws AgolItemTransactionFailedException
     */
    public void updateItem(AgolItem agolItem) throws IOException, AgolItemTransactionFailedException {
        String userItemUrl = _userContentUrl + "/items/" + agolItem.getId();
        String updateItemUrl = userItemUrl + "/update";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();
        agolAttributes.putAll(agolItem.getAttributes());

        InputStream entities = _httpRequest.executePostRequest(updateItemUrl, agolAttributes, null);
        if (entities != null)
        {
            JsonNode rootNode = _objectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null)
            {
                throw new AgolItemTransactionFailedException("Update Item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }
        }
    }

    /**
     * Delete a specific item
     * @param agolItem
     * @throws IOException
     * @throws AgolItemTransactionFailedException
     */
    public void deleteItem(AgolItem agolItem) throws IOException, AgolItemTransactionFailedException {
        String userItemUrl = _userContentUrl + "/items/" + agolItem.getId();
        String deleteItemUrl = userItemUrl + "/delete";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        InputStream entities = _httpRequest.executePostRequest(deleteItemUrl, agolAttributes, null);
        if (entities != null)
        {
            JsonNode rootNode = _objectMapper.readTree(entities);

            JsonNode errorNode = rootNode.get("error");
            if (errorNode != null)
            {
                throw new AgolItemTransactionFailedException("Delete Item failed with error " + errorNode.get("code") + ". " + errorNode.get("message"));
            }
        }
    }

    /**
     * Sets the format and token attributes needed in every HTTP interaction with ArcGIS Online
     * @return the standard ArcGIS Online attributes
     * @throws IOException
     */
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
