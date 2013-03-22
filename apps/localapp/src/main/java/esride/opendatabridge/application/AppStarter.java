package esride.opendatabridge.application;

import esride.opendatabridge.processinfo.IProcessInfo;
import esride.opendatabridge.processinfo.ProcessProperty;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.factory.CatalogReaderFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 19.03.13
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class AppStarter {
    private static Logger sLogger = Logger.getLogger(AppStarter.class);

    private static boolean pinfoAvailabel = false;
    private static final String pinfoArgs = "-pinfo";
    private static String pinfoValue;
    private static boolean readeridAvailable = false;
    private static final String readeridArgs = "-readerid";
    private static String readerValue;

    public static void main(String[] args){

        if(sLogger.isInfoEnabled()){
            sLogger.info("---------- Start Application OpenDataBridge -----------------");
            sLogger.info("start initialize application....");
        }        
        //Spring initialisieren
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"appconfig/transformerConfig.xml"});

        //Programm Parameter auswerten
        if(args.length != 2){
            sLogger.error("Please check the program parameters. " + pinfoArgs + " and " + readeridArgs);
            sLogger.error("Terminate application");
            System.exit(2);    
        }
        
        for(int i=0; i<args.length; i++){
            String argument = args[i];
            if(sLogger.isInfoEnabled()){
                sLogger.info("Argument: " + argument);
            }
            int delim = argument.indexOf("=");
            String key = argument.substring(0, delim);
            String value = argument.substring(delim + 1);
            if(key == null || value == null || key.trim().length() == 0 || value.trim().length() == 0){
                sLogger.error("Please check the program parameters. " + pinfoArgs + " and " + readeridArgs);
                sLogger.error("Terminate application");
                System.exit(2);    
            }
            if(key.equals(pinfoArgs)){
                pinfoValue = value;
                pinfoAvailabel = true;
            }
            if(key.equals(readeridArgs)){
                readerValue = value;
                readeridAvailable = true;
            }
        }
        
        if(!pinfoAvailabel || !readeridAvailable){
            sLogger.error("Please check the program parameters. " + pinfoArgs + " and " + readeridArgs);
            sLogger.error("Terminate application");
            System.exit(2);        
        }
                
        //Auslesen der Process Informationen
        IProcessInfo processInfo = context.getBean("processinfo", IProcessInfo.class);
        List<ProcessProperty> properties = processInfo.getProperties(pinfoValue);

        //ReaderFactory initialisieren
        CatalogReaderFactory factory = context.getBean("readerfactory", CatalogReaderFactory.class);
        IReader reader = factory.newReaderInstance(readerValue, properties);

        //ToDo: Transformer holen und Reader setzen
        //ITransformer lTransformer = context.getBean("transformer");
        //lTransformer.setReader(mReader);

        if(sLogger.isInfoEnabled()){
            sLogger.info("initialize application successfull....");
        }

        //ToDo: Transform durchfuehren


    }
}
