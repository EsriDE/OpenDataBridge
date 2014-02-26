package esride.opendatabridge.agolclient;

/**
 * The attributes for the Update Item Response.
 * User: Markus Stecker, con terra GmbH
 * Date: 31.01.14
 * Time: 17:11
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
