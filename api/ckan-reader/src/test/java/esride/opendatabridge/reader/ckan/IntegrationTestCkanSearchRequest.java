package esride.opendatabridge.reader.ckan;

import esride.opendatabridge.reader.request.CatalogRequestObj;
import esride.opendatabridge.reader.request.CatalogResponseObj;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 24.04.13
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestCkanSearchRequest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CkanSearchRequest searchRequest;

    @Resource(name="requestObj1")
    private CatalogRequestObj reqObj1;

    @Test
    public void testCkanWMS(){
        //CatalogRequestObj reqObj = new CatalogRequestObj();
        try {
            CatalogResponseObj responseObj = searchRequest.executeRequest(reqObj1);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
