package esride.opendatabridge.reader.csw;



import esride.opendatabridge.httptransport.IHTTPRequest;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 08.04.13
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class CSWGetRecordsRequest {

    private static Logger sLogger = Logger.getLogger(CSWGetRecordsRequest.class);


    private CSWGetRecordsResponse getRecordsResponse;

    private GetRecordsRequestTemplate requestTemplate;
    private IHTTPRequest httpRequest;


    private DocumentBuilder builder;

    public void setGetRecordsResponse(CSWGetRecordsResponse getRecordsResponse) {
        this.getRecordsResponse = getRecordsResponse;
    }

    public void setRequestTemplate(GetRecordsRequestTemplate requestTemplate) {
        this.requestTemplate = requestTemplate;
    }

    public CSWGetRecordsResponse getGetRecordsResponse() {
        return getRecordsResponse;
    }

    public GetRecordsRequestTemplate getRequestTemplate() {
        return requestTemplate;
    }

    public void setHttpRequest(IHTTPRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public CSWGetRecordsRequest() throws ParserConfigurationException{
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        builderFactory.setIgnoringElementContentWhitespace(false);
        builder = builderFactory.newDocumentBuilder();

    }

    public CSWResponseObj executeGetRecordsRequest(CSWRequestObj requestObj) throws IOException {

        String getRecordsRequest = requestTemplate.generateGetRecordsTemplate(requestObj.getParameters());
        InputStream stream = httpRequest.executePostRequest(requestObj.getCswUrl(), getRecordsRequest, "UTF-8", requestObj.getHeader());

        Document responseDoc = this.createDocumentFromInputStream(stream);
        return getRecordsResponse.createCSWResponse(responseDoc);
    }
    
    private Document createDocumentFromInputStream(InputStream inputStream){
        Document document = null;
        try {
            document = builder.parse(inputStream);
        } catch (SAXException e) {
            String message = "Could not build Document";
            sLogger.error(message, e);

        } catch (IOException e) {
            String message = "Could not build Document";
            sLogger.error(message, e);

        }
        return document;
    }
    
}
