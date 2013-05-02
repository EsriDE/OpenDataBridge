package esride.opendatabridge.reader.csw.templatefile;

import esride.opendatabridge.reader.csw.IGetRecordsTemplateLocation;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 14.04.13
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public class FileSysGetRecordsTemplateLocation implements IGetRecordsTemplateLocation {

    public InputStream getTemplateFile(String processId) {
        return this.getClass().getResourceAsStream("/templates/" + processId + ".xml");
    }
}
