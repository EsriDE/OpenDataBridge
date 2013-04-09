package esride.opendatabridge.reader.csw;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

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
    private GetRecordsRequestGenerator reqGenerator;
    
    @Test
    public void testGenerator(){
        InputStream stream = this.getClass().getResourceAsStream("/getRecordTemplates/Template01.xml");
        reqGenerator.setGetRecordsTemplate(stream);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("TYPE_NAMES", "gmd:MD_Metadata");
        String lRequest = reqGenerator.generateGetRecordsTemplate(map);
        System.out.print(lRequest);
    }

}
