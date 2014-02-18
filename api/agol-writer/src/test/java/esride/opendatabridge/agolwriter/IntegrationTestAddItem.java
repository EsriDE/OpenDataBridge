package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolclient.AgolAddItemRequest;
import esride.opendatabridge.agolclient.AgolAddItemResponse;
import esride.opendatabridge.agolclient.AgolGenerateTokenRequest;
import esride.opendatabridge.agolclient.AgolGenerateTokenResponse;
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
 * Date: 13.12.13
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestAddItem extends AbstractJUnit4SpringContextTests {

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
    public void testSuccessReq(){
        try{
            AgolGenerateTokenRequest req = new AgolGenerateTokenRequest(successParamMap.get("username"), successParamMap.get("password"), successParamMap.get("referer"), successParamMap.get("url"));
            AgolGenerateTokenResponse resp =  req.excReqWithJsonResp(httpRequest, objectMapper);
            String token = resp.getToken();
            Assert.assertNotNull(token);

            try {
                AgolItem testItem1 = agolItemFactory.createAgolItem(jsonMap.get("test01"));
                AgolAddItemRequest request = new AgolAddItemRequest(urlMap.get("addItemUrl"), token, testItem1);
                AgolAddItemResponse response = request.excReqWithJsonResp(httpRequest, objectMapper);
                Assert.assertTrue(response.isSuccess());
                response.getFolderId();
                String id = response.getId();

                //delete the item
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
