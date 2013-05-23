package esride.opendatabridge.processing;

import esride.opendatabridge.agolwriter.IAgolService;
import esride.opendatabridge.application.StartParameter;
import esride.opendatabridge.application.StartParameterException;
import esride.opendatabridge.processinfo.IProcessInfo;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;
import esride.opendatabridge.reader.factory.CatalogReaderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 23.05.13
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:appconfig/test02Config.xml"})
public class IntegrationTestTransformer02 extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CatalogReaderFactory readerFactory;

    @Autowired
    private IProcessInfo processInfo;

    @Autowired
    private IAgolService agolService;

    @Test
    public void testInsert(){
        StartParameter param = null;
        String[] paramArray = new String[5];
        paramArray[0] = "-pid=Test01";
        paramArray[1] = "-readerid=ckan";
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
}
