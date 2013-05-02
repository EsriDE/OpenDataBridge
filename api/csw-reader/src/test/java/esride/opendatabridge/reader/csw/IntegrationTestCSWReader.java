package esride.opendatabridge.reader.csw;

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
 * Date: 29.04.13
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-integrationtest-config.xml"})
public class IntegrationTestCSWReader extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CSWReader reader;

    @Test
    public void testGetItemsFromCatalog(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("csw.url", "http://hmdk.de/csw");
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");

        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            List<TransformedItem> items = reader.getItemsFromCatalog();
            //int size = items.size();
            //Assert.assertEquals(size, 26);
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }
}
