package esride.opendatabridge.agolwriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.item.AgolItemFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
    @Autowired
    private ObjectMapper _objectMapper;

    /**
     * Setter for _objectMapper
     * @param
     */
    /*public void set_objectMapper(ObjectMapper objectMapper) {
        this._objectMapper = objectMapper;
    } */

    @Test
    public void testGetAllItems() {
        try {
            List<String> itemTypes = new ArrayList<String>();
            itemTypes.add("WMS");
            itemTypes.add("Map Service");
            Map<String, ArrayList<AgolItem>> agolItems = agolService.searchItems(itemTypes, OwnerType.ORG);
            Assert.assertNotNull("List of Agol items is empty.", agolItems);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AgolItemInvalidException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testGetDistillerien() {
        try {
            List<String> itemTypes = new ArrayList<String>();
            itemTypes.add("Web Mapping Application");
            itemTypes.add("Feature Service");
            Map<String, ArrayList<AgolItem>> agolItems = agolService.searchItems(itemTypes, OwnerType.ORG, "Distillerien");
            Assert.assertNotNull("List of ArcGIS Online items is empty.", agolItems);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AgolItemInvalidException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testSearchUser() {
        try {
            Map<String, ArrayList<AgolItem>> agolItems = agolService.searchItems("", OwnerType.USER);
            Assert.assertNotNull("List of ArcGIS Online items is empty.", agolItems);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AgolItemInvalidException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testAddPublicUpdateDeleteItem(){
        try {
            AgolItem testItem1 = agolItemFactory.createAgolItem(jsonMap.get("test01"));
            AgolItem testItem2 = agolItemFactory.createAgolItem(jsonMap.get("test02"));
            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            agolItems.add((testItem1));
            agolService.addItems(agolItems);

            testItem2.setId(testItem1.getId());
            List<AgolItem> updateItems = new ArrayList<AgolItem>();
            updateItems.add(testItem2);
            agolService.updateItems(updateItems);

            List<AgolItem> deleteAgolItems = new ArrayList<AgolItem>();
            deleteAgolItems.add((testItem2));
            agolService.deleteItems(deleteAgolItems);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddOrgUpdateDeleteItem(){
        try {
            AgolItem testItem1 = agolItemFactory.createAgolItem(jsonMap.get("test01"));
            AgolItem testItem2 = agolItemFactory.createAgolItem(jsonMap.get("test02"));
            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            agolItems.add((testItem1));
            agolService.addItems(agolItems, AccessType.ORG);

            testItem2.setId(testItem1.getId());
            List<AgolItem> updateItems = new ArrayList<AgolItem>();
            updateItems.add(testItem2);
            agolService.updateItems(updateItems, AccessType.SHARED, agolService.getUserGroupIds());

            List<AgolItem> deleteAgolItems = new ArrayList<AgolItem>();
            deleteAgolItems.add((testItem2));
            agolService.deleteItems(deleteAgolItems);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddGroupsUpdateDeleteItem(){
        try {
            String userGroupIds = agolService.getUserGroupIds();
            String firstGroup = userGroupIds.substring(0, userGroupIds.indexOf(","));
            String secondGroup = userGroupIds.substring(userGroupIds.indexOf(",")+1, userGroupIds.length());
            AgolItem testItem1 = agolItemFactory.createAgolItem(jsonMap.get("test01"));
            AgolItem testItem2 = agolItemFactory.createAgolItem(jsonMap.get("test02"));

            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            String timestamp = new Long(new Date().getTime()/1000).toString();
            String title = testItem1.getAttributes().get("title") + " " + timestamp;
            testItem1.updateAttribute("title", title);
            agolItems.add((testItem1));
            agolService.addItems(agolItems, AccessType.ORG, firstGroup); //userGroupIds);

            Map<String, ArrayList<AgolItem>> returnTestItems = agolService.searchItems(title, OwnerType.USER);
            ArrayList<AgolItem> returnAddedItems = returnTestItems.get(testItem1.getUrl());
            AgolItem returnedItem = returnAddedItems.get(0);
            testItem2.setId(returnedItem.getId());
            List<AgolItem> updateItems = new ArrayList<AgolItem>();
            updateItems.add(testItem2);
            agolService.updateItems(updateItems, AccessType.PRIVATE, secondGroup);

            List<AgolItem> deleteAgolItems = new ArrayList<AgolItem>();
            deleteAgolItems.add((testItem2));
            agolService.deleteItems(deleteAgolItems);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAddDeleteKmlItem(){
        try {
            String userGroupIds = agolService.getUserGroupIds();
            String firstGroup = userGroupIds.substring(0,userGroupIds.indexOf(","));
            AgolItem kmlTestItem = agolItemFactory.createAgolItem(jsonMap.get("test03"));

            List<AgolItem> agolItems = new ArrayList<AgolItem>();
            String timestamp = new Long(new Date().getTime()/1000).toString();
            String title = kmlTestItem.getAttributes().get("title") + " " + timestamp;
            kmlTestItem.updateAttribute("title", title);
            agolItems.add((kmlTestItem));
            agolService.addItems(agolItems, AccessType.ORG, firstGroup);

            Map<String, ArrayList<AgolItem>> returnTestItems = agolService.searchItems(title, OwnerType.USER);
            ArrayList<AgolItem> returnAddedItems = returnTestItems.get(kmlTestItem.getUrl());
            AgolItem returnedItem = returnAddedItems.get(0);
            kmlTestItem.setId(returnedItem.getId());

            List<AgolItem> deleteAgolItems = new ArrayList<AgolItem>();
            deleteAgolItems.add((kmlTestItem));
            agolService.deleteItems(deleteAgolItems);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteItems(){
        try {
            agolService.deleteItems("37af5e4b787046ab9301255eb15c41bf,2C2cc78b3b57e64967aae845b937e92637");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetItem(){
        try {
            agolService.getItem("e0d01999fce04369b67eeda30d572bd7");
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

    @Test
    public void testCreateItemFromODHashMap() throws IOException, AgolItemInvalidException {
        HashMap<String, String> odHashMap = new HashMap<String, String>();

        String jsonString = jsonMap.get("odHashMap");
        odHashMap = _objectMapper.readValue(jsonString, HashMap.class);
        AgolItem agolItem = agolItemFactory.createAgolItem(odHashMap);
        Assert.assertNotNull("No item", agolItem);
    }
}
