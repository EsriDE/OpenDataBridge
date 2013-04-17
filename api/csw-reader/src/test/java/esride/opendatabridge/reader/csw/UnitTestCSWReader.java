package esride.opendatabridge.reader.csw;

import esride.opendatabridge.reader.ReaderException;

import esride.opendatabridge.reader.ReaderItems;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 12.04.13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-reader-config.xml"})
public class UnitTestCSWReader extends AbstractJUnit4SpringContextTests {
    
    @Autowired
    private CSWReader reader;
    
    @Test
    public void testGet26ItemsFromCatalog(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("csw.url", "http://hmdk.de/csw/26items");
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");

        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            ReaderItems items = reader.getItemsFromCatalog();
            int size = items.getItems().size();
            Assert.assertEquals(size, 26);
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGet11ItemsFromCatalog(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("csw.url", "http://hmdk.de/csw/11items");
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");

        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            ReaderItems items = reader.getItemsFromCatalog();
            int size = items.getItems().size();
            Assert.assertEquals(size, 11);
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGet10ItemsFromCatalog(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("csw.url", "http://hmdk.de/csw/10items");
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");

        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            ReaderItems items = reader.getItemsFromCatalog();
            int size = items.getItems().size();
            Assert.assertEquals(size, 10);
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGet9ItemsFromCatalog(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("csw.url", "http://hmdk.de/csw/9items");
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");

        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            ReaderItems items = reader.getItemsFromCatalog();
            int size = items.getItems().size();
            Assert.assertEquals(size, 9);
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetItemsWithOneFailure(){
        HashMap<String, String> processProperties = new HashMap<String, String>();
        processProperties.put("csw.url", "http://hmdk.de/csw/8items");
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");

        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }

        try {
            ReaderItems items = reader.getItemsFromCatalog();
            int size = items.getItems().size();
            Assert.assertEquals(size, 8);
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetItemsWithMissingProperties(){
        HashMap<String, String> processProperties = new HashMap<String, String>();        
        processProperties.put("csw_request_getrecords_template_maxRecords", "10");
        processProperties.put("csw_request_getrecords_template_outputschema", "http://www.isotc211.org/2005/gmd");
        processProperties.put("csw_request_getrecords_template_typenames", "gmd:MD_Metadata");
        try {
            reader.setProperties(processProperties, "Test001");
        } catch (ReaderException e) {
            Assert.assertTrue(true);
        }
    }
}
