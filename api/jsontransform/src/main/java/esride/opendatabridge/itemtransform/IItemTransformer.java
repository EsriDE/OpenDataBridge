package esride.opendatabridge.itemtransform;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 02.04.13
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public interface IItemTransformer {
    
    public HashMap transform2AgolItem(MetadataResource resource, String processId) throws ItemTransformationException, ItemGenerationException;

}
