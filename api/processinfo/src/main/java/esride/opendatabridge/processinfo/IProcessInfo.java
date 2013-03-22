package esride.opendatabridge.processinfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 20.03.13
 * Time: 07:55
 * To change this template use File | Settings | File Templates.
 */
public interface IProcessInfo {

    /**
     * gets the properties for the transformation source (csw, ckan, etc... properties)
     * @param processId the id for the process
     * @return
     */
    public List<ProcessProperty> getProperties(String processId);
}
