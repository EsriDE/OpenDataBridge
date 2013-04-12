package esride.opendatabridge.reader.csw;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class CSWRequestObj {

    public CSWRequestObj(String cswUrl, HashMap<String, String> parameters, HashMap<String, String> header) {
        this.cswUrl = cswUrl;
        this.parameters = parameters;
        this.header = header;
    }

    /**
     * the csw url for the request
     */
    private String cswUrl;

    /**
     * the parameters (templateItems for post template, get request parameter)
     */
    private HashMap<String, String> parameters;

    /**
     * HTTP Header parameter for the request
     */
    private HashMap<String, String> header;

    public String getCswUrl() {
        return cswUrl;
    }

    public void setCswUrl(String cswUrl) {
        this.cswUrl = cswUrl;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }
}
