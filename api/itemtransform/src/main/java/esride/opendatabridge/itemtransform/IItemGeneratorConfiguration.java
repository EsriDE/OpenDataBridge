package esride.opendatabridge.itemtransform;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 05.04.13
 * Time: 10:06
 * To change this template use File | Settings | File Templates.
 */
public interface IItemGeneratorConfiguration {
    
    public Properties getItemGeneratorConfiguration(String processId, String resourceType);
}
