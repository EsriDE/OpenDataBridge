package esride.opendatabridge.reader.csw;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 14.04.13
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public interface IGetRecordsTemplateLocation {
    
    public InputStream getTemplateFile(String processId);

}
