package esride.opendatabridge.reader;

import esride.opendatabridge.processinfo.ProcessProperty;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 21.03.13
 * Time: 07:56
 * To change this template use File | Settings | File Templates.
 */
public interface IReaderFactory {

    public void setProperties(HashMap<String, String> properties, String processId);
}
