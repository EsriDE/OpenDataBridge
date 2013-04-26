package esride.opendatabridge.reader.ckan;

import esride.opendatabridge.reader.ReaderException;
import esride.opendatabridge.reader.TransformedItem;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 26.04.13
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-reader-config.xml"})
public class IntegrationTestCkanReader extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CkanReader ckanReader;
    
    @Test
    public void testGetItemsFormCatalog(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("ckan.url", "http://www.govdata.de/ckan/api/search/dataset");
        processProperties.put("ckan_request_search_param_q", "res_format:WMS");
        processProperties.put("ckan_request_search_param_all_fields", "1");
        processProperties.put("ckan_request_search_param_offset", "0");
        processProperties.put("ckan_request_search_param_limit", "20");

        try {
            ckanReader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            List<TransformedItem> list = ckanReader.getItemsFromCatalog();
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }
}
