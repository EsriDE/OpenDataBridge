package esride.opendatabridge.agolclient;

import java.util.List;

/**
 * The attributes for the Share Item Response
 * User: Markus Stecker, con terra GmbH
 * Date: 31.01.14
 * Time: 17:07
 */
public class AgolShareItemResponse {

    private List<String> notSharedWithGroupIds;

    private String itemId;

    public AgolShareItemResponse(List<String> pNotSharedWithGroupIds, String pItemId) {
        notSharedWithGroupIds = pNotSharedWithGroupIds;
        itemId = pItemId;
    }

    public List<String> getNotSharedWithGroupIds() {
        return notSharedWithGroupIds;
    }

    public String getItemId() {
        return itemId;
    }
}
