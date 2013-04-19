package esride.opendatabridge.item;

/**
 * Created with IntelliJ IDEA.
 * User: nik
 * Date: 19.04.13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

public interface IItem {
    public void fromJson(String jsonItem);
    public String toJson();
}
