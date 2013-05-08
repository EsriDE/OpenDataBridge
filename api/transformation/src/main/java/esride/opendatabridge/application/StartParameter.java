package esride.opendatabridge.application;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 03.05.13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class StartParameter {
    
    private static Logger sLogger = Logger.getLogger(StartParameter.class);
    
    private String[] startArgs;

    /**
     *  process Id (pid)  which identifies the process information
     */
    private boolean pidAvailabel = false;
    private static final String pidArgs = "-pid";
    private String pidValue;

    /**
     * reader Id which identifier the adapter type (ckan, csw)
     */
    private boolean readeridAvailable = false;
    private static final String readeridArgs = "-readerid";
    private String readerValue;

    /**
     * parameter which definies if the process starts in a test mode
     */
    private static final String testArgs = "-test";
    private boolean testAvailableValue = false;

    public StartParameter(String[] startArgs) throws StartParameterException {
        this.startArgs = startArgs;
        this.extractParameters();
    }

    private void extractParameters() throws StartParameterException{

        for(int i=0; i<startArgs.length; i++){
            String argument = startArgs[i];
            
            if(sLogger.isInfoEnabled()){
                sLogger.info("Start Parameter: " + argument);
            }
            int delim = argument.indexOf("=");
            String key = argument.substring(0, delim);
            String value = argument.substring(delim + 1);
            
            
            
            if(key == null || value == null || key.trim().length() == 0 || value.trim().length() == 0){
                throw new StartParameterException("Please check the program parameters. " + pidArgs + " and " + readeridArgs);
            }
            if(key.equals(pidArgs)){
                pidValue = value;
                pidAvailabel = true;
            }
            if(key.equals(readeridArgs)){
                readerValue = value;
                readeridAvailable = true;
            }
            if(key.equals(testArgs)){
                testAvailableValue = Boolean.parseBoolean(value);
            }

        }

        if(!pidAvailabel || !readeridAvailable){
            throw new StartParameterException("Please check the program parameters. " + pidArgs + " and " + readeridArgs);
        }
    }

    public String getPidValue() {
        return pidValue;
    }

    public String getReaderValue() {
        return readerValue;
    }

    public boolean isTestAvailableValue() {
        return testAvailableValue;
    }
}
