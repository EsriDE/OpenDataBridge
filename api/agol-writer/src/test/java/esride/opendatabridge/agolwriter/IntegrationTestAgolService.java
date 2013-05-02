package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 12.03.13
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestAgolService extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AgolService agolService;
    @Autowired
    private AgolItemFactory agolItemFactory;
    @Resource
    private HashMap<String,String> jsonMap;

    @Test
    public void testGetAllItems() {
        Map<String, ArrayList<AgolItem>> agolItems = agolService.getAllItems("WMS");
    }

    @Test
    public void testAddItem(){
        // ToDo: How to generate a UID for the test data (id and item) and use a real username in the unit tests?
        try {
            agolService.addItem(agolItemFactory.createAgolItem(jsonMap.get("test01")));
        } catch (AgolItemTransactionFailedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
