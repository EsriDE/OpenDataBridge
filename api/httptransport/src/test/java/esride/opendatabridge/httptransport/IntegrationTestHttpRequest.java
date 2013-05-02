package esride.opendatabridge.httptransport;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 09.04.13
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class IntegrationTestHttpRequest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IHTTPRequest request;

    @Test
    public void testGetReq(){
        try {
            InputStream stream = request.executeGetRequest("http://gateway.hamburg.de/OGCFassade/DE_HH_WMS_INSPIRE_A3_2_Gebaeude.aspx?SERVICE=WMS&VERSION=1.3.0&REQUEST=GetCapabilities", null);
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testPostReq(){
        InputStream stream = this.getClass().getResourceAsStream("/testPost/GetRecords.xml");
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        StringBuffer xmlStrBuf = new StringBuffer();
        String line;
        try {
            while ((line = rd.readLine()) != null) {
                xmlStrBuf.append(line);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        String reqBody = xmlStrBuf.toString();
        String reqBodyChar = "UTF-8";

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("SOAPAction", "");
        headerMap.put("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
        headerMap.put("Content-Type", "application/soap+xml; charset=UTF-8");

        try {
            InputStream response = request.executePostRequest("http://hmdk.de/csw", reqBody, reqBodyChar, headerMap);
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(response));
            String respline = "";
            while ((respline = responseReader.readLine()) != null) {
                System.out.println(respline);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
