package esride.opendatabridge.reader.request;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 10.04.13
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class CatalogRequestObj {

    public CatalogRequestObj(String catalogUrl, HashMap<String, String> parameters, HashMap<String, String> header) {
        this.catalogUrl = catalogUrl;
        this.parameters = parameters;
        this.header = header;
    }

    public CatalogRequestObj(String catalogUrl, HashMap<String, String> parameters) {
        this.catalogUrl = catalogUrl;
        this.parameters = parameters;
    }

    /**
     * the csw url for the request
     */
    private String catalogUrl;

    /**
     * the parameters (templateItems for post template, get request parameter)
     */
    private HashMap<String, String> parameters;

    /**
     * HTTP Header parameter for the request
     */
    private HashMap<String, String> header;

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
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
