package esride.opendatabridge.application;

import esride.opendatabridge.processinfo.IProcessInfo;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;
import esride.opendatabridge.reader.factory.CatalogReaderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 03.05.13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class AppInitializer {
    
    private IReader reader;

    public AppInitializer(StartParameter startParam) throws ReaderException {

        //Spring initialisieren
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"appconfig/transformerConfig.xml"});

        //Auslesen der Process Informationen
        IProcessInfo processInfo = context.getBean("processinfo", IProcessInfo.class);
        HashMap<String, String> properties = processInfo.getProperties(startParam.getPidValue());

        //ReaderFactory initialisieren
        CatalogReaderFactory factory = context.getBean("readerfactory", CatalogReaderFactory.class);        
        reader = factory.newReaderInstance(startParam.getReaderValue(), properties, startParam.getPidValue());
    }

    public IReader getReader() {
        return reader;
    }
}
