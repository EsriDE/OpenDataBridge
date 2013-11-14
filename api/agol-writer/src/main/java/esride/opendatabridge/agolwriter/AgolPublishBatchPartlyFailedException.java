package esride.opendatabridge.agolwriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 08.11.13
 * Time: 08:33
 * To change this template use File | Settings | File Templates.
 */
public class AgolPublishBatchPartlyFailedException extends Exception {
    List<String> failureList;

    public AgolPublishBatchPartlyFailedException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolPublishBatchPartlyFailedException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolPublishBatchPartlyFailedException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolPublishBatchPartlyFailedException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
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
