package esride.opendatabridge.processing;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 20.11.13
 * Time: 19:31
 * To change this template use File | Settings | File Templates.
 */
public class DeleteItem{

    private String id;

    private String thumbnailUrl;

    private String url;

    private String title;

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String pThumbnailUrl) {
        thumbnailUrl = pThumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String pUrl) {
        url = pUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }
}
