package esride.opendatabridge.agolclient;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 31.01.14
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class AgolUpdateItemResponse {

    private String id;

    private String folderId;

    private boolean success;

    public AgolUpdateItemResponse(String pId, String pFolderId, boolean pSuccess) {
        id = pId;
        folderId = pFolderId;
        success = pSuccess;
    }

    public String getId() {
        return id;
    }

    public String getFolderId() {
        return folderId;
    }

    public boolean isSuccess() {
        return success;
    }
}
