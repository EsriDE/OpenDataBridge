package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolclient.*;
import esride.opendatabridge.httptransport.HTTPRequest;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 01.02.14
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestLifecycleItem extends AbstractJUnit4SpringContextTests {

    @Autowired
    private HTTPRequest httpRequest;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private Map<String,String> successParamMap;

    @Resource
    private HashMap<String,String> jsonMap;

    @Resource
    private HashMap<String, String> urlMap;

    @Autowired
    private AgolItemFactory agolItemFactory;

    @Test
    public void testPublisherCRUDLifecycle(){
        try{
            //generate Token
            AgolGenerateTokenRequest req = new AgolGenerateTokenRequest(successParamMap.get("username"), successParamMap.get("password"), successParamMap.get("referer"), successParamMap.get("url"));
            AgolGenerateTokenResponse resp =  req.excReqWithJsonResp(httpRequest, objectMapper);

            String token = resp.getToken();
            Assert.assertNotNull(token);

            try {
                //add the Item
                AgolItem testItem1 = agolItemFactory.createAgolItem(jsonMap.get("test01"));
                AgolAddItemRequest request = new AgolAddItemRequest(urlMap.get("addItemUrl"), token, testItem1);
                AgolAddItemResponse response = request.excReqWithJsonResp(httpRequest, objectMapper);
                Assert.assertTrue(response.isSuccess());
                response.getFolderId();
                String id = response.getId();

                //ShareTheItem with everyone
                String shareItem = urlMap.get("shareItemUrl");
                AgolShareItemRequest shareRequest = new AgolShareItemRequest(shareItem.replace("<itemId>", id), token, true, true, null);
                AgolShareItemResponse shareItemResponse = shareRequest.excReqWithJsonResp(httpRequest, objectMapper);
                String sharedItemid = shareItemResponse.getItemId();
                Assert.assertEquals(id, sharedItemid);

                //ShareTheItem with org
                AgolShareItemRequest shareRequest2 = new AgolShareItemRequest(shareItem.replace("<itemId>", id), token, false, true, null);
                AgolShareItemResponse shareItemResponse2 = shareRequest2.excReqWithJsonResp(httpRequest, objectMapper);
                String sharedItemid2 = shareItemResponse2.getItemId();
                Assert.assertEquals(id, sharedItemid2);

                //Request the Items for the special owner
                String qString = "owner:" + successParamMap.get("username");
                AgolSearchRequest searchRequest = new AgolSearchRequest(urlMap.get("searchItemUrl"), token, qString, "10", "1");
                AgolSearchResponse searchResponse = searchRequest.excReqWithJsonResp(httpRequest, objectMapper, agolItemFactory);

                String deleteItem = urlMap.get("deleteItemUrl");
                AgolDeleteItemRequest deleteRequest = new AgolDeleteItemRequest(deleteItem.replace("<itemId>", id), token);
                AgolDeleteItemResponse deleteResponse = deleteRequest.excReqWithJsonResp(httpRequest, objectMapper);
                deleteResponse.isSuccess();
                Assert.assertEquals(id, deleteResponse.getItemId());


            } catch (AgolItemInvalidException e) {
                Assert.fail(e.getMessage());
            }



        }catch(IllegalArgumentException ex){
            Assert.fail(ex.getMessage());
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
