package esride.opendatabridge.agolwriter;

import esride.opendatabridge.item.AGOLItem;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: gvs
 * Date: 06.03.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public interface IWriter {
    public List<String> getResourceUrls();
    public void addItem(AGOLItem agolItem);
}
