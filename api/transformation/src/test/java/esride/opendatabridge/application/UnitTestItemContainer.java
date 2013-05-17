package esride.opendatabridge.application;

import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.processing.ItemContainer;
import esride.opendatabridge.processing.ItemTransaction;
import esride.opendatabridge.reader.TransformedItem;
import org.junit.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * UnitTest for {@link esride.opendatabridge.processing.ItemContainer ItemContainer}
 * User: sma
 * Date: 17.05.13
 * Time: 10:33
 */
public class UnitTestItemContainer extends TestCase {
    
    public void testInsertUpdateDelete(){
        //create three AgolItems
        HashMap<String, String> item01Map = new HashMap<String, String>();
        item01Map.put("title", "Delete Test 01");
        item01Map.put("id", "DeleteTest01-1");
        AgolItem itemDel01 = new AgolItem(item01Map);

        HashMap<String, String> item02Map = new HashMap<String, String>();
        item02Map.put("title", "Delete Test 02");
        item02Map.put("id", "DeleteTest02-2");
        AgolItem itemDel02 = new AgolItem(item02Map);

        HashMap<String, String> item03Map = new HashMap<String, String>();
        item03Map.put("title", "Update Test");
        item03Map.put("id", "UpdateTest");
        AgolItem itemUp01 = new AgolItem(item03Map);

        List<AgolItem> agolItemList = new ArrayList<AgolItem>(3);
        agolItemList.add(itemDel01);
        agolItemList.add(itemDel02);
        agolItemList.add(itemUp01);

        //create three AgolItems
        TransformedItem insert01 = new TransformedItem();
        HashMap<String, String> item04Map = new HashMap<String, String>();
        item04Map.put("agol.title", "Insert Test 01");
        insert01.setItemElements(item04Map);
        insert01.setResourceUrl("http://test.wms.de");

        TransformedItem insert02 = new TransformedItem();
        HashMap<String, String> item05Map = new HashMap<String, String>();
        item05Map.put("agol.title", "Insert Test 02");
        insert02.setItemElements(item05Map);
        insert02.setResourceUrl("http://test.wms.de");

        TransformedItem update01 = new TransformedItem();
        HashMap<String, String> item06Map = new HashMap<String, String>();
        item06Map.put("agol.title", "Update Test");
        update01.setItemElements(item06Map);
        update01.setResourceUrl("http://test.wms.de");

        List<TransformedItem> transformedIemList = new ArrayList<TransformedItem>(3);
        transformedIemList.add(insert01);
        transformedIemList.add(insert02);
        transformedIemList.add(update01);

        ItemContainer container = new ItemContainer();
        container.setAgolItems(agolItemList);
        container.setCatalogItems(transformedIemList);

        List<ItemTransaction> transactionObjList =  container.getItemsForPublishing();
        Assert.assertEquals(transactionObjList.size(), 5);
        for(ItemTransaction trans : transactionObjList){
            int transStatus = trans.getTransactionStatus();
            switch (transStatus){
                case 1:
                    String title1 = trans.getTransformedItem().getItemElements().get("agol.title");
                    Assert.assertTrue(title1.equals("Insert Test 01") || title1.equals("Insert Test 02"));
                    break;
                case 2:
                    String title2 = trans.getTransformedItem().getItemElements().get("agol.title");
                    Assert.assertTrue(title2.equals("Update Test"));
                    break;
                case 3:
                    String title3 = trans.getAgolItem().getAttributes().get("title");
                    Assert.assertTrue(title3.equals("Delete Test 01") || title3.equals("Delete Test 02"));
                    break;
            }
        }


    }

}
