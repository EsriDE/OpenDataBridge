package esride.opendatabridge.itemtransform;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 02.04.13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class UnitTestAgolJsonTransformer extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AgolItemTransformer transformer;
    
    @Test
    public void test01CSW(){
        try {
            //FileOutputStream lOutputStream = new FileOutputStream(new File("C:\\data\\coding\\prj\\OpenDataBridge-spec\\Test.xml"));



            InputStream capabilitiesStream = this.getClass().getResourceAsStream("/itemgenerator/test01/capabilities.xml");
            
            
            MetadataSet container1 = new MetadataSet();
            container1.setEncodingType("xml");
            container1.setMetadataType("capabilities");
            container1.setInputStream(capabilitiesStream);

            InputStream metadataStream = this.getClass().getResourceAsStream("/itemgenerator/test01/metadata.xml");
            MetadataSet container2 = new MetadataSet();
            container2.setEncodingType("xml");
            container2.setMetadataType("csw");
            container2.setInputStream(metadataStream);

            List<MetadataSet> containerList = new ArrayList<MetadataSet>();
            containerList.add(container1);
            containerList.add(container2);

            MetadataResource resource = new MetadataResource();
            resource.setResourceType("WMS");
            resource.setContainer(containerList);
            
            HashMap<String, String> itemMap = transformer.transform2AgolItem(resource,"test01");

            /*ByteArrayOutputStream jsonOutputStream = new ByteArrayOutputStream();

            InputStream jsonStream = this.getClass().getResourceAsStream("/ckanfiles/test1.json");
            transformer.createXmlFromJson(jsonStream, jsonOutputStream);

            //OutputStream in InputStream umwandeln
            ByteArrayInputStream xmlStream = new ByteArrayInputStream(jsonOutputStream.toByteArray());

            DocumentBuilderFactory lBuilderFactory = DocumentBuilderFactory.newInstance();
            lBuilderFactory.setNamespaceAware(false);
            DocumentBuilder builder = lBuilderFactory.newDocumentBuilder();            
            Document document = builder.parse(xmlStream);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            String idvalue = xpath.evaluate("/ObjectNode/result/results/id", document);
            Assert.assertEquals(idvalue, "f6331f99-51f6-44d9-95b9-b20f3b74f360");
            */

        } catch (ItemTransformationException e) {
            Assert.fail(e.getMessage());
        }

    }
}
