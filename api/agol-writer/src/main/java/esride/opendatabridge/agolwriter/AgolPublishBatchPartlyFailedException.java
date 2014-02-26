package esride.opendatabridge.agolwriter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sma
 * Date: 08.11.13
 * Time: 08:33
 */
public class AgolPublishBatchPartlyFailedException extends Exception {
    List<String> failureList;

    public AgolPublishBatchPartlyFailedException() {
        super();
    }

    public AgolPublishBatchPartlyFailedException(String message) {
        super(message);
    }

    public AgolPublishBatchPartlyFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgolPublishBatchPartlyFailedException(Throwable cause) {
        super(cause);
    }

    public void setFailureList(List<String> agolItemList){
        this.failureList = agolItemList;
    }

    public void addFailureItem(String failureMessage){
        if(failureList == null){
            failureList = new ArrayList<String>();
        }
        failureList.add(failureMessage);
    }

    public List<String> getFailureList(){
        return failureList;
    }
}
