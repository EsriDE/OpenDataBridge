package esride.opendatabridge.agolreader;

import esride.opendatabridge.item.AgolItem;

import java.util.List;

/**
 * This interface encapsulate some major requests against ArcGIS Online which are necessary for the
 * transformation process
 * User: Markus Stecker, con terra GmbH
 * Date: 29.01.14
 * Time: 08:28
 */
public interface IAgolItemReader {

    public AgolItem getAgolItemById(String agolId);

    public List<String> getAgolItemIdByTitle(String title);

    public List<String> getAgolItemIdByUrl(String url);

    public String getAgolItemIdByCatalogId(String catalogId);

    public void touchItem(String agolId);

    public List<String> getUntouchedAgolItems();

}
