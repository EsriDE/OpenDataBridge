package esride.opendatabridge.reader.mock;

import esride.opendatabridge.httptransport.IHTTPRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class MockHTTPRequest implements IHTTPRequest {

    private InputStream stream01to10 = this.getClass().getResourceAsStream("/mockup/GetRecords01-10.xml");
    private InputStream stream11to20 = this.getClass().getResourceAsStream("/mockup/GetRecords11-20.xml");
    private InputStream stream21to26 = this.getClass().getResourceAsStream("/mockup/GetRecords21-26.xml");

    private InputStream stream01to10from11 = this.getClass().getResourceAsStream("/mockup/GetRecords01-10-11.xml");
    private InputStream stream11from11 = this.getClass().getResourceAsStream("/mockup/GetRecords11-11.xml");

    private InputStream stream01to10from10 = this.getClass().getResourceAsStream("/mockup/GetRecords01-10-10.xml");
    private InputStream stream01to09from09 = this.getClass().getResourceAsStream("/mockup/GetRecords01-09-09.xml");

    private InputStream stream01to09from08 = this.getClass().getResourceAsStream("/mockup/GetRecords01-09-08.xml");

    private HashMap<String, String> capabilitiesMap = new HashMap<String, String>();
    //private InputStream cap01 = this.getClass().getResourceAsStream("/mockup/Capabilities01.xml");
    //private InputStream cap02 = this.getClass().getResourceAsStream("/mockup/Capabilities02.xml");
    //private InputStream cap03 = this.getClass().getResourceAsStream("/mockup/Capabilities03.xml");
    //capabilities Docs

    public MockHTTPRequest(){
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DIRK150.aspx", "/mockup/Capabilities01.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Brueckenbauwerke.aspx", "/mockup/Capabilities01.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/DE_HH_WMS_INSPIRE_A1_6_Flurstueck.aspx", "/mockup/Capabilities03.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_BRW_2008E_Firmen.aspx", "/mockup/Capabilities04.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/DE_HH_WMS_INSPIRE_A3_1_StatistischeEinheiten.aspx", "/mockup/Capabilities05.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Stadtteile.aspx", "/mockup/Capabilities06.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_REGIO.aspx", "/mockup/Capabilities07.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DOP20.aspx", "/mockup/Capabilities08.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Strassenverkehr.aspx", "/mockup/Capabilities09.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Geobasisdaten.aspx", "/mockup/Capabilities10.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/BSU_WMS_APRO.aspx", "/mockup/Capabilities11.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_BBW_07_2011E_Firmen.aspx", "/mockup/Capabilities12.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/BSU_WMS_Bodendaten.aspx", "/mockup/Capabilities13.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DOP20_belaubt.aspx", "/mockup/Capabilities14.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DISK20.aspx", "/mockup/Capabilities15.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_ATKIS_SW.aspx", "/mockup/Capabilities16.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Schwimmbaeder.aspx", "/mockup/Capabilities17.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_ATKIS.aspx", "/mockup/Capabilities18.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DIRK600.aspx", "/mockup/Capabilities19.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Metrokarte.aspx", "/mockup/Capabilities20.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_BOP_Raster.aspx", "/mockup/Capabilities21.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Statistische_Gebiete.aspx", "/mockup/Capabilities22.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Kombi_DISK_GB.aspx", "/mockup/Capabilities23.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DOP40.aspx", "/mockup/Capabilities24.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Verwaltungsgrenzen.aspx", "/mockup/Capabilities25.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Gewaesserbauwerke.aspx", "/mockup/Capabilities26.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/failureService?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", null);
    }

    public InputStream executeGetRequest(String url, HashMap<String, String> header) throws IOException {
        int charPos = url.indexOf("?");
        String lCuttedUrl =  url.substring(0, charPos);
        if(lCuttedUrl.equals("http://gateway.hamburg.de/OGCFassade/failureService.aspx")){
            throw new IOException("Service is not available");
        }
        String path = capabilitiesMap.get(lCuttedUrl);
        return this.getClass().getResourceAsStream(path);
    }

    public InputStream executeGetRequest(String baseUrl, String requestParam, HashMap<String, String> header) throws IOException {

        return null;
    }

    public InputStream executeGetRequest(String baseUrl, HashMap<String, String> requestParamMap, HashMap<String, String> header) throws IOException {
        return null;
    }

    public InputStream executePostRequest(String url, String reqBody, String reqBodyChar, HashMap<String, String> header) throws IOException {
        if(url.equals("http://hmdk.de/csw/26items")){
            if(reqBody.contains("startPosition=\"1\"")){
                return stream01to10;
            }
            if(reqBody.contains("startPosition=\"11\"")){
                return stream11to20;
            }
            if(reqBody.contains("startPosition=\"21\"")){
                return stream21to26;
            }
        }

        if(url.equals("http://hmdk.de/csw/11items")){
            if(reqBody.contains("startPosition=\"1\"")){
                return stream01to10from11;
            }
            if(reqBody.contains("startPosition=\"11\"")){
                return stream11from11;
            }
        }

        if(url.equals("http://hmdk.de/csw/10items")){
            if(reqBody.contains("startPosition=\"1\"")){
                return stream01to10from10;
            }
        }

        if(url.equals("http://hmdk.de/csw/9items")){
            if(reqBody.contains("startPosition=\"1\"")){
                return stream01to09from09;
            }
        }

        if(url.equals("http://hmdk.de/csw/8items")){
            if(reqBody.contains("startPosition=\"1\"")){
                return stream01to09from08;
            }
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
