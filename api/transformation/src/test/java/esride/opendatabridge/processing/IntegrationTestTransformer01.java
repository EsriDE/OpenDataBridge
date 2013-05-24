package esride.opendatabridge.processing;

import esride.opendatabridge.agolwriter.AgolItemInvalidException;
import esride.opendatabridge.agolwriter.AgolTransactionFailedException;
import esride.opendatabridge.agolwriter.IAgolService;

import esride.opendatabridge.agolwriter.OwnerType;
import esride.opendatabridge.application.StartParameter;
import esride.opendatabridge.application.StartParameterException;
import esride.opendatabridge.item.AgolItem;
import esride.opendatabridge.processinfo.IProcessInfo;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;

import esride.opendatabridge.reader.factory.CatalogReaderFactory;
import org.apache.log4j.Logger;
import org.junit.After;

import org.junit.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.IOException;
import java.util.*;

/**
 * This class contains testcases against the CSW Adapter. Before testing: Please check the number of Metadata records for each request
 * User: sma
 * Date: 16.05.13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:appconfig/test01Config.xml"})
public class IntegrationTestTransformer01 extends AbstractJUnit4SpringContextTests {
    
    private static Logger sLogger = Logger.getLogger(IntegrationTestTransformer01.class);

    @Autowired
    private CatalogReaderFactory readerFactory;

    @Autowired
    private IProcessInfo processInfo;

    @Autowired
    private IAgolService agolService;

    

    @After
    public void tearDown() throws Exception {
        //ToDo: alle Daten loeschen
    }


    @Test
    public void testInsert(){
        StartParameter param = null;
        String[] paramArray = new String[5];
        paramArray[0] = "-pid=Test01";
        paramArray[1] = "-readerid=csw";
        paramArray[2] = "-searchstring=";
        paramArray[3] = "-accesstype=PRIVATE";
        paramArray[4] = "-ownertype=USER";
        try {
            param = new StartParameter(paramArray);
        } catch (StartParameterException e) {
            Assert.fail(e.getMessage());
        }
             
        IReader reader = null;
        try {
            reader = readerFactory.newReaderInstance(param.getReaderValue(), processInfo.getProperties(param.getPidValue()), param.getPidValue());
        } catch (ReaderException e) {
            Assert.fail(e.getMessage()); 
        }
        Transformer transform = new Transformer();
        transform.executeProcessTransformation(reader, agolService, param.isDeleteValue(), param.isOverwriteAccessTypeValue(),param.getSearchStringValue(), param.getAccessTypeValue(), param.getOwnerTypeValue());
    }

    //@Test
    public void testInsertUpdateWithDeleteOption(){

        //first Time
        this.executeProcess("Test01", "type:WMS" , "true",  56);
        //second Time
        Map<String, ArrayList<AgolItem>> agolItemMap = this.executeProcess("Test02", "type:WMS", "true", 3);
        
        
        ArrayList<AgolItem> deleteList = new ArrayList<AgolItem>();
        Set<String> agolItemKeySet = agolItemMap.keySet();
        Iterator<String> agolItemIter = agolItemKeySet.iterator();
        while(agolItemIter.hasNext()){
            ArrayList<AgolItem> agolItemArray = agolItemMap.get(agolItemIter.next());
            for(int i=0; i<agolItemArray.size(); i++){
                deleteList.add(agolItemArray.get(i));
            }
            
        }
        
        //clean up
        deleteAllItems(deleteList);

    }

    private Map<String, ArrayList<AgolItem>> executeProcess(String processId, String searchString, String deleteStrategy, int numberOfExpectedCatalogRecords){
        //Do Insert
        StartParameter insertParam = null;
        String[] insertParamArray = new String[6];
        insertParamArray[0] = "-pid=" + processId;
        insertParamArray[1] = "-readerid=csw";
        insertParamArray[2] = "-searchstring=" + searchString;
        insertParamArray[3] = "-accesstype=PRIVATE";
        insertParamArray[4] = "-ownertype=ORG";
        insertParamArray[5] = "-deleteobj=" + deleteStrategy;
        try {
            insertParam = new StartParameter(insertParamArray);
        } catch (StartParameterException e) {
            Assert.fail(e.getMessage());
        }


        IReader reader = null;
        try {
            reader = readerFactory.newReaderInstance(insertParam.getReaderValue(), processInfo.getProperties(insertParam.getPidValue()), insertParam.getPidValue());
        } catch (ReaderException e) {
            Assert.fail(e.getMessage());
        }
        Transformer transform = new Transformer();
        transform.executeProcessTransformation(reader, agolService, insertParam.isDeleteValue(), insertParam.isOverwriteAccessTypeValue(),insertParam.getSearchStringValue(), insertParam.getAccessTypeValue(), insertParam.getOwnerTypeValue());

        //Just for test purpose (count the number of items)
        synchronized(this){
            try {
                sLogger.info("Wait 50 seconds after delete");
                this.wait(50000);
                sLogger.info("Gon on");
            } catch (InterruptedException e) {
                //ToDo: Exception Handling
            }
        }
        Map<String, ArrayList<AgolItem>> agolItemMap = null;
        try {
            agolItemMap =  agolService.searchItems(searchString, OwnerType.ORG);
            int agolSize = agolItemMap.size();
            Assert.assertTrue(agolSize == numberOfExpectedCatalogRecords);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AgolItemInvalidException e) {
            e.printStackTrace();  //ToDo: Exception Handling
        }
        return agolItemMap;
    }

    private void deleteAllItems(List<AgolItem> agolItem){
        try {
            agolService.deleteItems(agolItem);
            synchronized(this){
                try {
                    sLogger.info("Wait 60 seconds after delete");
                    this.wait(60000);
                    sLogger.info("Gon on");
                } catch (InterruptedException e) {
                    //ToDo: Exception Handling
                }
            }
            Map<String, ArrayList<AgolItem>> agolItemMap = null;
            try {
                agolItemMap = agolService.searchItems("type:WMS", OwnerType.ORG);
            } catch (AgolItemInvalidException e) {
                e.printStackTrace();  //ToDo: Exception Handling
            }
            Assert.assertTrue(agolItemMap.size() == 0);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (AgolTransactionFailedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
