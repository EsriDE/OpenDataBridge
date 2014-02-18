package esride.opendatabridge.agolclient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 31.01.14
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
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
