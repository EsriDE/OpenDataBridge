package esride.opendatabridge.reader.mock;

import esride.opendatabridge.httptransport.IHTTPRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class MockHTTPRequest implements IHTTPRequest {

    private InputStream stream01to10 = this.getClass().getResourceAsStream("/mockup/GetRecords01-10.xml");
    private InputStream stream11to20 = this.getClass().getResourceAsStream("/mockup/GetRecords11-20.xml");
    private InputStream stream21to26 = this.getClass().getResourceAsStream("/mockup/GetRecords21-26.xml");

    private InputStream cap01 = this.getClass().getResourceAsStream("/mockup/Capabilities01.xml");
    private InputStream cap02 = this.getClass().getResourceAsStream("/mockup/Capabilities02.xml");
    private InputStream cap03 = this.getClass().getResourceAsStream("/mockup/Capabilities03.xml");
    //capabilities Docs

    public InputStream executeGetRequest(String url, HashMap<String, String> header) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public InputStream executeGetRequest(String baseUrl, String requestParam, HashMap<String, String> header) throws IOException {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public InputStream executePostRequest(String url, String reqBody, String reqBodyChar, HashMap<String, String> header) throws IOException {
        if(reqBody.contains("startPosition=\"1\"")){
            return stream01to10;
        }
        if(reqBody.contains("startPosition=\"11\"")){
            return stream11to20;
        }
        if(reqBody.contains("startPosition=\"11\"")){
            return stream21to26;
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
