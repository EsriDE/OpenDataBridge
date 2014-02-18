package esride.opendatabridge.processing;

import esride.opendatabridge.agolreader.IAgolItemReader;
import esride.opendatabridge.agolwriter.IAgolService;
import esride.opendatabridge.application.StartParameter;
import esride.opendatabridge.application.StartParameterException;
import esride.opendatabridge.pipeline.controller.PipelineController;
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

    @Autowired
    private PipelineController pipelineController;

    @Autowired
    private IAgolItemReader agolReader;

    //@Test
    /*public void testRequestFiles(){
        StartParameter param = null;
        String[] paramArray = new String[5];
        paramArray[0] = "-pid=Test01";
        paramArray[1] = "-readerid=ckan";
        //paramArray[2] = "-searchstring=";
        paramArray[2] = "-accesstype=PUBLIC";
        paramArray[3] = "-ownertype=USER";
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
        try {
            transform.executeProcessDeleteDuplicate(agolService, param.getAccessTypeValue(), param.getOwnerTypeValue());
        } catch (TransformerException e) {
            Assert.fail(e.getMessage());
        }
    } */

    //@Test
    public void testInsertKml(){
        StartParameter param = null;
        String[] paramArray = new String[5];
        paramArray[0] = "-pid=Test01";
        paramArray[1] = "-readerid=ckan";
        //paramArray[2] = "-searchstring=";
        paramArray[2] = "-accesstype=PUBLIC";
        paramArray[3] = "-ownertype=USER";
        paramArray[4] = "-overwriteaccesstype=true";
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
        try {
            transform.executeProcessTransformation(reader, agolService,  pipelineController, agolReader,param.isDeleteValue(), param.isOverwriteAccessTypeValue(), param.getAccessTypeValue(), param.getOwnerTypeValue());
        } catch (TransformerException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testInsertBremen(){
        StartParameter param = null;
        String[] paramArray = new String[4];
        paramArray[0] = "-pid=Test02";
        paramArray[1] = "-readerid=ckan";
        //paramArray[2] = "-searchstring=";
        paramArray[2] = "-accesstype=PRIVATE";
        paramArray[3] = "-ownertype=USER";
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
        try {
            transform.executeProcessTransformation(reader, agolService, pipelineController, agolReader, param.isDeleteValue(), param.isOverwriteAccessTypeValue(), param.getAccessTypeValue(), param.getOwnerTypeValue());
        } catch (TransformerException e) {
            Assert.fail(e.getMessage());
        }
    }
}
