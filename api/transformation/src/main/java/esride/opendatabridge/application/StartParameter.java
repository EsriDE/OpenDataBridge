package esride.opendatabridge.application;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the start parameter for the transformation process
 * User: sma
 * Date: 03.05.13
 * Time: 14:20
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
    private boolean testValue = false;

    /**
     * parameter which definies if the process should delete agol item objects
     */
    private static final String deleteArgs = "-deleteobj";
    private boolean deleteValue = false;

    /**
     * parameter which definies if the process overwrite the access type in an update situation
     */
    private static final String overwriteAccessTypeArgs = "-overwriteaccesstype";
    private boolean overwriteAccessTypeValue = false;

    /**
     * parameter which definies the search string
     */
    private static final String searchStringArgs = "-searchstring";
    private String searchStringValue;

    /**
     * parameter which definies the accesstype of the items
     */
    private static final String accessTypeArgs = "-accesstype";
    private String accessTypeValue;

    /**
     * parameter which definies the ownertype of the items
     */
    private static final String ownerTypeArgs = "-ownertype";
    private String ownerTypeValue;

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
                testValue = Boolean.parseBoolean(value);
            }

            if(key.equals(deleteArgs)){
                deleteValue = Boolean.parseBoolean(value);
            }

            if(key.equals(overwriteAccessTypeArgs)){
                overwriteAccessTypeValue = Boolean.parseBoolean(value);
            }
            
            if(key.equals(searchStringArgs)){
                searchStringValue = value;
            }

            if(key.equals(accessTypeArgs)){
                accessTypeValue = value;
            }

            if(key.equals(ownerTypeArgs)){
                ownerTypeValue = value;
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
    
    public String getSearchStringValue(){
        return searchStringValue;
    }
    
    public String getAccessTypeValue(){
        return accessTypeValue;
    }
    
    public String getOwnerTypeValue(){
        return ownerTypeValue;
    }

    public boolean isTestValue() {
        return testValue;
    }

    public boolean isDeleteValue() {
        return deleteValue;
    }

    public boolean isOverwriteAccessTypeValue(){
        return overwriteAccessTypeValue;
    }
}
