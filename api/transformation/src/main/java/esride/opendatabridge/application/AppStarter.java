package esride.opendatabridge.application;


import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;
import org.apache.log4j.Logger;



/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 19.03.13
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class AppStarter {
    private static Logger sLogger = Logger.getLogger(AppStarter.class);    

    public static void main(String[] args){

        if(sLogger.isInfoEnabled()){
            sLogger.info("---------- Start Application OpenDataBridge -----------------");
            sLogger.info("start initialize application....");
        }

        //Parameter auslesen
        StartParameter startParam = null;
        try {
            startParam = new StartParameter(args);
        } catch (StartParameterException e) {
            sLogger.error("Terminate OpenDataBridge. Cause: " + e.getMessage());
            System.exit(2);
        }

        //Objekt Initialisierung
        AppInitializer initializer = null;
        try {
            initializer = new AppInitializer(startParam);
            if(sLogger.isInfoEnabled()){
                sLogger.info("initialize application successfull....");
            }
        } catch (ReaderException e) {
            sLogger.error("Terminate OpenDataBridge. Cause: " + e.getMessage());
            System.exit(2);
        }


        IReader reader = initializer.getReader();

        //ToDo: Transformer holen und Reader setzen
        //ITransformer lTransformer = context.getBean("transformer");
        //lTransformer.setReader(mReader);



        //ToDo: Transform durchfuehren


    }


}
