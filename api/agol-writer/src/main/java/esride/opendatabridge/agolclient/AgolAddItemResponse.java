package esride.opendatabridge.agolclient;

/**
 * The attributes for the Add Item Response
 * User: Markus Stecker, con terra GmbH
 * Date: 13.12.13
 * Time: 17:54
 */
public class AgolAddItemResponse {

    private String id;

    private String folderId;

    private boolean success;

    public AgolAddItemResponse(String pId, String pFolderId, boolean pSuccess) {
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
