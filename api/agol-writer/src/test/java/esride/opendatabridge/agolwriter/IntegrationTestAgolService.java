package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        try {
            Map<String, ArrayList<AgolItem>> agolItems = agolService.getAllItems("WMS");
            Assert.assertNotNull("List of Agol items is empty.", agolItems);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddPublicUpdateDeleteItem(){
        try {
            AgolItem testItem = agolItemFactory.createAgolItem(jsonMap.get("test01"));
            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            agolItems.add((testItem));
            String itemId = agolService.addItems(agolItems);
            testItem.setId(itemId);
            agolService.updateItem(testItem);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddOrgUpdateDeleteItem(){
        try {
            AgolItem testItem = agolItemFactory.createAgolItem(jsonMap.get("test01"));
            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            agolItems.add((testItem));
            String itemId = agolService.addItems(agolItems, AccessType.ORG);
            testItem.setId(itemId);
            agolService.updateItem(testItem);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddGroupsUpdateDeleteItem(){
        try {
            String userGroupIds = agolService.getUserGroupIds();
            AgolItem testItem = agolItemFactory.createAgolItem(jsonMap.get("test01"));
            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            agolItems.add((testItem));
            String itemId = agolService.addItems(agolItems, AccessType.SHARED, userGroupIds);
            testItem.setId(itemId);
            agolService.updateItem(testItem);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetUserGroupIds() {
        try {
            String userGroupIds = agolService.getUserGroupIds();
            Assert.assertNotNull(userGroupIds);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
