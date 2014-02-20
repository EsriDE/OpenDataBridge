package esride.opendatabridge.agolclient;

/**
 * The attributes for the Delete Item Response
 * User: Markus Stecker, con terra GmbH
 * Date: 02.02.14
 * Time: 17:43
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
