package esride.opendatabridge.application;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Unit Test for the {@link esride.opendatabridge.application.StartParameter StartParameter} class
 * User: sma
 * Date: 17.05.13
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class UnitTestStartParameter extends TestCase{
    
    public void testWithAllParameters(){
        String[] paramArray = new String[7];
        paramArray[0] = "-pid=Test01";
        paramArray[1] = "-readerid=csw";
        //paramArray[2] = "-searchstring=abc and def";
        paramArray[2] = "-accesstype=PRIVATE";
        paramArray[3] = "-ownertype=USER";
        paramArray[4] = "-test=true";
        paramArray[5] = "-deleteobj=true";
        paramArray[6] = "-overwriteaccesstype=true";
        
                
        try {
            StartParameter param = new StartParameter(paramArray);
            Assert.assertEquals(param.getPidValue(), "Test01");
            Assert.assertEquals(param.getReaderValue(), "csw");
            //Assert.assertEquals(param.getSearchStringValue(), "abc and def");
            Assert.assertEquals(param.getAccessTypeValue(), "PRIVATE");
            Assert.assertEquals(param.getOwnerTypeValue(), "USER");
            Assert.assertTrue(param.isTestValue());
            Assert.assertTrue(param.isDeleteValue());
            Assert.assertTrue(param.isOverwriteAccessTypeValue());
        } catch (StartParameterException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testWithDefaultParameters(){
        String[] paramArray = new String[4];
        paramArray[0] = "-pid=Test02";
        paramArray[1] = "-readerid=csw";
        //paramArray[2] = "-searchstring=abc and def";
        paramArray[2] = "-accesstype=PRIVATE";
        paramArray[3] = "-ownertype=USER";


        try {
            StartParameter param = new StartParameter(paramArray);
            Assert.assertEquals(param.getPidValue(), "Test02");
            Assert.assertEquals(param.getReaderValue(), "csw");
            //Assert.assertEquals(param.getSearchStringValue(), "abc and def");
            Assert.assertEquals(param.getAccessTypeValue(), "PRIVATE");
            Assert.assertEquals(param.getOwnerTypeValue(), "USER");
            Assert.assertFalse(param.isTestValue());
            Assert.assertFalse(param.isDeleteValue());
            Assert.assertFalse(param.isOverwriteAccessTypeValue());
        } catch (StartParameterException e) {
            Assert.fail(e.getMessage());
        }
    }


    public void testWithComplexSearchStringParameters(){
        String[] paramArray = new String[4];
        paramArray[0] = "-pid=Test02";
        paramArray[1] = "-readerid=csw";
        //paramArray[2] = "-searchstring=type=csw";
        paramArray[2] = "-accesstype=PRIVATE";
        paramArray[3] = "-ownertype=USER";


        try {
            StartParameter param = new StartParameter(paramArray);
            Assert.assertEquals(param.getPidValue(), "Test02");
            Assert.assertEquals(param.getReaderValue(), "csw");
            //Assert.assertEquals(param.getSearchStringValue(), "type=csw");
            Assert.assertEquals(param.getAccessTypeValue(), "PRIVATE");
            Assert.assertEquals(param.getOwnerTypeValue(), "USER");
            Assert.assertFalse(param.isTestValue());
            Assert.assertFalse(param.isDeleteValue());
            Assert.assertFalse(param.isOverwriteAccessTypeValue());
        } catch (StartParameterException e) {
            Assert.fail(e.getMessage());
        }
    }
}
