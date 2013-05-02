package esride.opendatabridge.reader.csw;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 09.04.13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class UnitTestCSWGetRecordsRequestGenerator extends AbstractJUnit4SpringContextTests {

    @Autowired
    private GetRecordsRequestTemplate reqTemplate;
    
    @Test
    public void testGenerator(){
        //InputStream stream = this.getClass().getResourceAsStream("/getRecordTemplates/Template01.xml");
        try {
            reqTemplate.setGetRecordsTemplate("Test000");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("TYPE_NAMES", "gmd:MD_Metadata");
        String lRequest = reqTemplate.generateGetRecordsTemplate(map);
        //System.out.print(lRequest);
    }

}
