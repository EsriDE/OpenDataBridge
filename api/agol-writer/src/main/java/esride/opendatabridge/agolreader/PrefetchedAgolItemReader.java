package esride.opendatabridge.agolreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolclient.AgolGenerateTokenRequest;
import esride.opendatabridge.agolclient.AgolGenerateTokenResponse;
import esride.opendatabridge.agolclient.AgolSearchRequest;
import esride.opendatabridge.agolclient.AgolSearchResponse;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import esride.opendatabridge.token.AgolTokenService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 29.01.14
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class PrefetchedAgolItemReader implements IAgolItemReader {

    private static Logger sLogger = Logger.getLogger(PrefetchedAgolItemReader.class);

    private Map<String, AgolItem> agolItemMap = new HashMap<String, AgolItem>();
    //key=title for the agolItem, value=all potential IDs
    private Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
    //key=url for the agolItem, value=all potential IDs
    private Map<String, List<String>> urlMap = new HashMap<String, List<String>>();
    //key=foreignId, value=agolItemId
    private Map<String, String> foreignIdMap = new HashMap<String, String>();

    private List<String> idList = new ArrayList<String>();

    private int maxPrefetchedAgolItems = 10000;
    private String foreignKeyPrefix;

    private int numberOfResults;

    private ObjectMapper objectMapper;
    private HTTPRequest httpRequest;

    private AgolItemFactory agolItemFactory;
    private AgolTokenService agolTokenService;

    public void setForeignKeyPrefix(String pForeignKeyPrefix) {
        foreignKeyPrefix = pForeignKeyPrefix;
    }

    public void setMaxPrefetchedAgolItems(int pMaxPrefetchedAgolItems) {
        maxPrefetchedAgolItems = pMaxPrefetchedAgolItems;
    }

    public void set_AgolItemFactory(AgolItemFactory agolItemFactory) {
        this.agolItemFactory = agolItemFactory;
    }

    public void set_httpRequest(HTTPRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public void set_objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setAgolTokenService(AgolTokenService pAgolTokenService) {
        agolTokenService = pAgolTokenService;
    }

    String searchUrl;
    //String tokenUrl = "https://www.arcgis.com/sharing/generateToken";

    String userName;
    //String password;
    //String referer;

    //public PrefetchedAgolItemReader(String baseUrl, String userName, String password, String referer) throws IOException {
    public PrefetchedAgolItemReader(String baseUrl, String userName) throws IOException {
        searchUrl = baseUrl + "/sharing/rest/search";
        this.userName = userName;
        //this.password = password;
        //this.referer = referer;
    }

    public void prefetchItems() throws IOException {
        //get Token for request
        sLogger.info("Load items from AGOL from user: " + userName);
        //AgolGenerateTokenRequest tokenRequest = new AgolGenerateTokenRequest(userName, password, referer, tokenUrl);
        //AgolGenerateTokenResponse tokenResponse = tokenRequest.excReqWithJsonResp(httpRequest, objectMapper);

        String token = agolTokenService.getToken();
        //get AgolItems
        this.searchItemsFromAgol(token, 1);
        sLogger.info("Finished prefetching items;");
    }

    private void searchItemsFromAgol(String token, int pStartPosition) throws IOException {
        //get AgolItems
        //todo: Hier muss noch eingebaut werden, dass an die query noch das "ownertype" angehangen wird.
        String qString = "owner:" + userName;
        AgolSearchRequest searchRequest = new AgolSearchRequest(searchUrl, token, qString, "100", String.valueOf(pStartPosition));
        AgolSearchResponse searchResponse =  searchRequest.excReqWithJsonResp(httpRequest, objectMapper, agolItemFactory);

        numberOfResults = searchResponse.getTotal();
        if(numberOfResults > 0){
            if(numberOfResults < maxPrefetchedAgolItems){
                this.prepareHashMaps(searchResponse.getAgolItemList(), true);
            }else{
                this.prepareHashMaps(searchResponse.getAgolItemList(), false);
            }

            int nextStart = searchResponse.getNextStart();
            sLogger.info("StartPosition for the next Search: " + nextStart);
            if(nextStart != -1){
                this.searchItemsFromAgol(token, nextStart);
            }
        }

    }

    private void prepareHashMaps(List<AgolItem> pAgolItemList, boolean pStoreAgolItemObj){
        for(int i=0; i<pAgolItemList.size(); i++){
            String id = pAgolItemList.get(i).getId();
            if(pStoreAgolItemObj){
                agolItemMap.put(id, pAgolItemList.get(i));
            }
            idList.add(id);

            String foreignKey = pAgolItemList.get(i).getForeignKey(foreignKeyPrefix);
            if(foreignKey != null){
                if(foreignIdMap.containsKey(foreignKey)){
                    sLogger.info("Double value found: ForeignKey (" + foreignKey + ") and ID (" + id + ")");
                }
                foreignIdMap.put(pAgolItemList.get(i).getForeignKey(foreignKeyPrefix), id);
            }

            String title = pAgolItemList.get(i).getTitle();
            if(titleMap.containsKey(title)){
                titleMap.get(title).add(id);
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("Duplicate title: title " + title + " id " + id);
                }
            }else{
                List<String> idList = new ArrayList<String>();
                idList.add(id);
                titleMap.put(title, idList);
            }
            String url = pAgolItemList.get(i).getUrl();
            if(urlMap.containsKey(url)){
                urlMap.get(url).add(id);
                if(sLogger.isDebugEnabled()){
                    sLogger.debug("Duplicate url: url " + url + " id " + id);
                }
            }else{
                List<String> idList = new ArrayList<String>();
                idList.add(id);
                urlMap.put(url, idList);
            }
        }
    }

    public AgolItem getAgolItemById(String agolId) {
        if(numberOfResults >= maxPrefetchedAgolItems || agolItemMap == null){
            //todo: hole die Daten direkt von AGOL  ueber die Schnittstelle
        }else{
            return agolItemMap.get(agolId);
        }
        return null;
    }

    public List<String> getAgolItemIdByTitle(String title) {
        return titleMap.get(title);
    }

    public List<String> getAgolItemIdByUrl(String url) {
        return urlMap.get(url);
    }

    public String getAgolItemIdByCatalogId(String catalogId) {
        return foreignIdMap.get(catalogId);
    }

    /**
     * removes the item id from the ID-List.
     * @param agolId identifier of the ArcGIS Online item
     */
    public void touchItem(String agolId) {
        idList.remove(agolId);
    }

    /**
     *
     * @return List of IDs which can be deleted
     */
    public List<String> getUntouchedAgolItems() {
        return idList;
    }
}
