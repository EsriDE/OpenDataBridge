package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.agolclient.AgolGenerateTokenRequest;
import esride.opendatabridge.agolclient.AgolGenerateTokenResponse;
import esride.opendatabridge.httptransport.HTTPRequest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;



import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 04.12.13
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */

@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestGenerateToken extends AbstractJUnit4SpringContextTests {

    @Autowired
    private HTTPRequest httpRequest;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private Map<String,String> successParamMap;

    @Resource
    private Map<String,String> wrongParamMap;

    @Test
    public void testMissingUsernameReq(){
        try{
            AgolGenerateTokenRequest req = new AgolGenerateTokenRequest(null, "pwd", "https://www.esri.de", "json", "https://www.esri.com");
            req.exceuteRequest(httpRequest, objectMapper);
            Assert.fail("Should throw Illegal Argument Exception");
        }catch(IllegalArgumentException ex){
            Assert.assertNotNull(ex);
        } catch (IOException e) {
            Assert.fail("Should throw Illegal Argument Exception");
        }
    }

    @Test
    public void testSuccessReq(){
        try{
            AgolGenerateTokenRequest req = new AgolGenerateTokenRequest(successParamMap.get("username"), successParamMap.get("password"), successParamMap.get("referer"), "json", successParamMap.get("url"));
            AgolGenerateTokenResponse resp =  req.exceuteRequest(httpRequest, objectMapper);
            Assert.assertNotNull(resp.getToken());
        }catch(IllegalArgumentException ex){
            Assert.fail(ex.getMessage());
        } catch (IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void testWrongCredentialsReq(){
        try{
            AgolGenerateTokenRequest req = new AgolGenerateTokenRequest(wrongParamMap.get("username"), wrongParamMap.get("password"), wrongParamMap.get("referer"), "json", wrongParamMap.get("url"));
            req.exceuteRequest(httpRequest, objectMapper);
            Assert.fail("Should throw IOException");
        }catch(IllegalArgumentException ex){
            Assert.fail(ex.getMessage());
        } catch (IOException ex) {
            Assert.assertNotNull(ex);
        }
    }
}
