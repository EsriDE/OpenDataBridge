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
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DIRK150.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities01.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Brueckenbauwerke.aspx?service=WMS&request=GetCapabilities&version=1.1.1", "/mockup/Capabilities01.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/DE_HH_WMS_INSPIRE_A1_6_Flurstueck.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.3.0", "/mockup/Capabilities03.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_BRW_2008E_Firmen.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities04.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/DE_HH_WMS_INSPIRE_A3_1_StatistischeEinheiten.aspx?Service=WMS&Version=1.3.0&Request=GetCapabilities", "/mockup/Capabilities05.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Stadtteile.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities06.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_REGIO.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities07.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DOP20.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities08.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Strassenverkehr.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.3.0", "/mockup/Capabilities09.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Geobasisdaten.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities10.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/BSU_WMS_APRO.aspx?SERVICE=WMS&REQUEST=GetCapabilities", "/mockup/Capabilities11.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_BBW_07_2011E_Firmen.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities12.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/BSU_WMS_Bodendaten.aspx?SERVICE=WMS&REQUEST=GetCapabilities&VERSION=1.3.0", "/mockup/Capabilities13.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DOP20_belaubt.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities14.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DISK20.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities15.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_ATKIS_SW.aspx?SERVICE=WMS&REQUEST=GetCapabilities", "/mockup/Capabilities16.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Schwimmbaeder.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities17.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_ATKIS.aspx?SERVICE=WMS&REQUEST=GetCapabilities", "/mockup/Capabilities18.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DIRK600.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities19.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Metrokarte.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities20.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_BOP_Raster.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities21.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Statistische_Gebiete.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities22.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Kombi_DISK_GB.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities23.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_DOP40.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities24.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Verwaltungsgrenzen.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities25.xml");
        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/HH_WMS_Gewaesserbauwerke.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", "/mockup/Capabilities26.xml");

        capabilitiesMap.put("http://gateway.hamburg.de/OGCFassade/failureService?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1", null);
    }

    public InputStream executeGetRequest(String url, HashMap<String, String> header) throws IOException {
        if(url.equals("http://gateway.hamburg.de/OGCFassade/failureService.aspx?SERVICE=WMS&REQUEST=GetCapabilities&version=1.1.1")){
            throw new IOException("Service is not available");
        }
        String path = capabilitiesMap.get(url);
        return this.getClass().getResourceAsStream(path);
    }

    public InputStream executeGetRequest(String baseUrl, String requestParam, HashMap<String, String> header) throws IOException {

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
