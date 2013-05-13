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
    private String _baseUrl, _rootUrl, _userContentUrl, _selfUrl;
    private Long _tokenExpires;
    private static final Logger log = Logger.getLogger(AgolService.class);
    private AgolItemFactory _agolItemFactory;
    private Map<String, ArrayList<AgolItem>> _agolItems = new HashMap<String, ArrayList<AgolItem>>();
    private HTTPRequest _httpRequest;
    private ObjectMapper _objectMapper;
    private String _userGroupIds;
    private String _orgId;

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

        _rootUrl = _baseUrl + "/sharing";
        _userContentUrl = _rootUrl + "/content/users/" + _userName;
        _selfUrl = _rootUrl + "/community/self";
//        _userCommunityUrl = _rootUrl + "/community/users/" + _userName;
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
     * Fill user details
     * @throws IOException
     */
    private void fillUserDetails() throws IOException {
        _userGroupIds = "";
        _orgId = "";

        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

        InputStream entities = _httpRequest.executePostRequest(_selfUrl, agolAttributes, null);
        if (entities != null)
        {
            JsonNode rootNode = _objectMapper.readTree(entities);

            JsonNode groupsNode = rootNode.get("groups");
            if (groupsNode!=null) {
                Iterator groupsIterator = groupsNode.elements();

                while (groupsIterator.hasNext()) {
                    JsonNode group = (JsonNode) groupsIterator.next();
                    if (_userGroupIds.length()>0) {
                        _userGroupIds += ",";
                    }
                    JsonNode idNode = group.get("id");
                    if (idNode != null) {
                        _userGroupIds += idNode.toString().replace("\"", "");
                    }
                }
            }

            JsonNode accountIdNode = rootNode.get("accountId");
            if (accountIdNode != null) {
                _orgId = accountIdNode.toString().replace("\"", "");
            }
        }
    }
    /**
     * Get IDs of the groups the logged-in user is a member of
     * @return Comma-separated userGroupIds
     */
    public String getUserGroupIds() throws IOException {
        if (_userGroupIds == null) {    // first call
            fillUserDetails();
        }
        return _userGroupIds;
    }
    /**
     * Get account ID of the logged-in user
     * @return accountId
     * @throws IOException
     */
    public String getAccountId() throws IOException {
        if (_orgId == null) {    // first call
            fillUserDetails();
        }
        return _orgId;
    }

    /**
     * Get a specific item, for example to check it's properties
     * @param itemId
     * @return
     */
    public AgolItem getItem(String itemId) {
        // ToDo: implement
        return _agolItemFactory.createAgolItem("");
    }

    /* ToDo:
        - Wie sieht's aus mit einer Schlagwortsuche auf Title oder Tags?
      */

    /**
     * Get all items owned by logged-in user. If logged-in user is not an admin, he has only write permission to his own items.
     * @param itemTypes: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#//02r3000000ms000000
     * @return
     * @throws IOException
     */
    public Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes) throws IOException {
        return getAllItems(itemTypes, AccessType.PRIVATE);
    }
    /**
     * Get all items with selectable access type
     * @param itemTypes
     * @param accessType
     * @return
     * @throws IOException
     */
    public Map<String, ArrayList<AgolItem>> getAllItems(List<String> itemTypes, AccessType accessType) throws IOException {
        String searchString = createSearchString(itemTypes, accessType);
        fillAgolItems(searchString, 0, 0);
        return _agolItems;
    }
    /**
     * Get all items that match the search string
     * @param searchString
     * @return
     * @throws IOException
     */
    public Map<String, ArrayList<AgolItem>> getAllItems(String searchString) throws IOException {
        fillAgolItems(searchString, 0, 0);
        return _agolItems;
    }
    /**
     * Concatenate search string for items request
     * @param itemTypes
     * @param accessType
     * @return
     * @throws IOException
     */
    private String createSearchString(List<String> itemTypes, AccessType accessType) throws IOException {
        String searchString =  "(";

        if (accessType.equals(AccessType.PRIVATE)) {
            // if logged-in user is not an admin, he has only write permission to his own items
            searchString +=  "owner:" + _userName;
        }
        else if (accessType.equals(AccessType.ORG)) {
            searchString += "accountid:" + getAccountId();
        }

        String searchItemTypes = "";
        for (String itemType : itemTypes) {
            if (searchItemTypes.length()>0) {
                searchItemTypes += " OR ";
            }
            searchItemTypes += "type:\"" + itemType + "\"";
        }
        searchItemTypes = "(" + searchItemTypes + ")";
        if (searchItemTypes.length()>0 ) {
            searchString += " AND " + searchItemTypes;
        }

        searchString += ")";

        return searchString;
    }
    /**
     * Get items request
     * @param searchString
     * @param startWithItemNumber: Integer value for recursive calls - 100 items are the maximum return number for one HTTP request
     * @throws IOException
     */
    private void fillAgolItems(String searchString, int startWithItemNumber, int alreadyRetrieved) throws IOException {
        int agolItemsPaginationNextStart;
        int totalItemsCount;
        int retrievedItemsCount;
        int duplicateUrlsCount = 0;
        String searchUrl = _rootUrl + "/search";
        HashMap<String, String> agolAttributes = getStandardAgolAttributes();

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
                retrievedItemsCount = agolItemsPaginationNextStart-1 + alreadyRetrieved;
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
                fillAgolItems(searchString, agolItemsPaginationNextStart, retrievedItemsCount);
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
        agolAttributes.put("groups", groupIds);
        InputStream entities = _httpRequest.executePostRequest(publishItemUrl, agolAttributes, null);

        handleResultListThatMightContainErrors(entities);
    }

    /**
     * Manually unshare items
     * @param itemIds: Comma-separated list of items to be unshared
     * @param groupIds: Comma-separated list of group IDs that the items will be unshared with.
     */
    public void unshareItems(String itemIds, String groupIds) throws IOException, AgolItemTransactionFailedException {
        String deleteItemsUrl = _userContentUrl + "/unshareItems";
        String errorItems = simpleHttpWithItemIdList(itemIds, deleteItemsUrl);
        if (!errorItems.isEmpty()) {
            throw new AgolItemTransactionFailedException("Unshare items failed for the following items: \n" + errorItems);
        }
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
     * Delete ArcgGIS Online items
     * @param agolItems
     * @throws IOException
     * @throws AgolItemTransactionFailedException
     */
    public void deleteItems(List<AgolItem> agolItems) throws IOException, AgolItemTransactionFailedException {
        String itemIds = "";

        for (AgolItem agolItem : agolItems) {
            if (itemIds.length()>0) {
                itemIds += ",";
            }
            itemIds += agolItem.getId();
        }
        deleteItems(itemIds);
    }
    /**
     * Delete ArcgGIS Online items
     * @param itemIds as comma-separated list
     * @throws IOException
     * @throws AgolItemTransactionFailedException
     */
    public void deleteItems(String itemIds) throws IOException, AgolItemTransactionFailedException {
        String deleteItemsUrl = _userContentUrl + "/deleteItems";
        String errorItems = simpleHttpWithItemIdList(itemIds, deleteItemsUrl);
        if (!errorItems.isEmpty()) {
            throw new AgolItemTransactionFailedException("Delete items failed for the following items: \n" + errorItems);
        }
    }

    /**
     * Handle a HTTP request to a function that only expects an itemId list
     * @param itemIds
     * @param actionUrl
     * @return String with a list of items that returned error messages
     * @throws IOException
     */
    private String simpleHttpWithItemIdList(String itemIds, String actionUrl) throws IOException {
        HashMap<String, String> agolAttributes = getStandardAgolAttributes();
        agolAttributes.put("items", itemIds);
        InputStream entities = _httpRequest.executePostRequest(actionUrl, agolAttributes, null);
        return handleResultListThatMightContainErrors(entities);
    }

    /**
     * Handle a result list that might contain errors
     * @param entities
     * @return String with a list of items that returned error messages
     * @throws IOException
     */
    private String handleResultListThatMightContainErrors(InputStream entities) throws IOException {
        String errorItems = "";
        if (entities != null)
        {
            JsonNode rootNode = _objectMapper.readTree(entities);

            JsonNode resultsNode = rootNode.get("results");
            if (resultsNode != null)
            {
                Iterator resultsIterator = resultsNode.iterator();
                while (resultsIterator.hasNext()) {
                    JsonNode resultNode = (JsonNode) resultsIterator.next();
                    JsonNode errorNode = resultNode.get("error");
                    if (errorNode!=null) {
                        if (!errorItems.isEmpty()) {
                            errorItems += "\n";
                        }
                        errorItems += resultNode.get("itemId") + ": " + errorNode.get("message") + " (Error code " + errorNode.get("code") + ")";
                    }
                }
            }
        }
        return errorItems;
    }

    /**
     * Sets the format and token attributes needed in every HTTP interaction with ArcGIS Online
     * @return the standard ArcGIS Online attributes
     * @throws IOException
     */
    private HashMap<String, String> getStandardAgolAttributes() throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put("f", "json");

        if ((_token == null) || (System.currentTimeMillis() >= _tokenExpires)) {
            if (log.isInfoEnabled()) {
                log.info("Creating new token.");
            }
            createToken();
        }

        agolAttributes.put("token", _token);
        return agolAttributes;
    }
}
