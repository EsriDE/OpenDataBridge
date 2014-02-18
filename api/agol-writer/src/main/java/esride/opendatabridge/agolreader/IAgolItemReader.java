package esride.opendatabridge.agolreader;

import esride.opendatabridge.item.AgolItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 29.01.14
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public interface IAgolItemReader {

    public AgolItem getAgolItemById(String agolId);

    public List<String> getAgolItemIdByTitle(String title);

    public List<String> getAgolItemIdByUrl(String url);

    public String getAgolItemIdByCatalogId(String catalogId);

    public void touchItem(String agolId);

    public List<String> getUntouchedAgolItems();

}
