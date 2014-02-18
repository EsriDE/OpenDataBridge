package esride.opendatabridge.agolclient;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 02.02.14
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public class AgolDeleteItemResponse {

    private boolean success;

    private String itemId;

    public AgolDeleteItemResponse(boolean pSuccess, String pItemId) {
        success = pSuccess;
        itemId = pItemId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getItemId() {
        return itemId;
    }
}
