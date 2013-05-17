package esride.opendatabridge.application;


import esride.opendatabridge.agolwriter.IAgolService;
import esride.opendatabridge.processing.Transformer;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.ReaderException;
import org.apache.log4j.Logger;



/**
 * The AppStarter class starts the transformation process with some special program parameters.
 * See {@link esride.opendatabridge.application.StartParameter StartParameter} for details.
 * User: sma
 * Date: 19.03.13
 * Time: 15:53
 */
public class AppStarter {
    private static Logger sLogger = Logger.getLogger(AppStarter.class);    

    public static void main(String[] args) throws InterruptedException {

        if(sLogger.isInfoEnabled()){
            sLogger.info("---------- Start OpenDataBridge Transformation Process -----------------");
        }

        //Parameter auslesen
        StartParameter startParam = null;
        try {
            sLogger.info("Application Start: check program paramaters....");
            startParam = new StartParameter(args);
        } catch (StartParameterException e) {
            sLogger.error("Application Start: Terminate OpenDataBridge. Cause: " + e.getMessage());
            System.exit(2);
        }

        //Objekt Initialisierung
        AppInitializer initializer = null;
        try {
            sLogger.info("Application Start: manage components and perpare catalog adapter...");
            initializer = new AppInitializer(startParam);
            if(sLogger.isInfoEnabled()){
                sLogger.info("Application Start: initialize application successfull....");
            }
        } catch (ReaderException e) {
            sLogger.error("Application Start: Terminate OpenDataBridge. Cause: " + e.getMessage());
            System.exit(2);
        }


        IReader reader = initializer.getReader();
        IAgolService agolService = initializer.getAgolService();

        sLogger.info("Application Start: Start Harvesting, Transformation and Publishing the ArcGIS Online Items");
        Transformer transformer = new Transformer();
        transformer.executeProcessTransformation(reader, agolService, startParam.isDeleteValue(),
                startParam.isOverwriteAccessTypeValue(), startParam.getSearchStringValue(),
                startParam.getAccessTypeValue(), startParam.getOwnerTypeValue());

        sLogger.info("---------- OpenDataBridge Transformation Process finished-----------------");
        sLogger.info("For detailed information please have s look into the log file of this process");
        sLogger.info("The logfile is located in the /logs directory of this application");
        Thread.sleep(20000);
        System.exit(0);

    }


}
