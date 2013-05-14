package esride.opendatabridge.reader.csw;


import esride.opendatabridge.reader.*;


import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.*;

/**
 * THe CSWReader extends the CatalogReader class. The CSWReader class harvests metadata from CSW metadata catalogues
 * and transforms the metadata into an AGOL Item compatible data model. For running an CSWReader component the following resources must be
 * available:
 * - the <p>csw.url</p> (HTTP POST endpoint for the CSW GetRecords endpoint)
 * - 0..* <p>csw_request_getrecords_header_{*}</p> properties for overwriting the HTTP Header
 * - 0..* <p>csw_request_getrecords_template_{*}</p> placeholder for overwriting the GetRecords POST request
 * - 0..* <p>csw_response_xpath_{*}</p>XPath values for using a different domain model (like Dublin Core)
 *
 * The GetRecords request is build by an XML Template. The template could be injected from a classpath path.
 *
 * For the AGOL Item transformation an <p>itemelement_{*}.properties</p> files must be available. For each CSW Metadata resource
 * (like WMS, Shapefile, CSV file) one property file must be created.
 *
 * User: sma
 * Date: 05.04.13
 * Time: 15:02
 */
public class CSWReader extends CatalogReader implements IReaderFactory {
    
    private static Logger sLogger = Logger.getLogger(CSWReader.class);    
    private Properties propertyItems = new Properties();
    
    private static final String MAX_RECORDS_ID = "csw_request_getrecords_template_maxRecords";
    private static final String START_POSITION_ID = "csw_request_getrecords_template_startPosition";
    private static final String CATALOG_TYPE = "csw";
    

    public void setProperties(HashMap<String, String> properties, String processId) throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CSW-Modul: Prepare CSW Module. Set Module properties");

        maxRecordsId = MAX_RECORDS_ID;
        startPositionId = START_POSITION_ID;
        catalogType = CATALOG_TYPE;
        
        catalogUrl = properties.get("csw.url");
        if(catalogUrl == null || catalogUrl.trim().length() == 0){
            throw new ReaderException("The property csw.url is missing");
        }
        sLogger.info("Module property: csw.url=" + catalogUrl);
        /*httpMethod = properties.get("csw_request_method");
        sLogger.info("Module property: csw_request_method=" + httpMethod);*/
        
        Set<String> keySet = properties.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            String value = properties.get(key);

            sLogger.info("Module property:" + key + "=" + value);
            if(key.startsWith("csw_request_getrecords_header_")){
                headerItems.put(key, value);
            }
            if(key.startsWith("csw_request_getrecords_template_")){
                templateItems.put(key, value);
            }
            if(key.startsWith("csw.reader.")){
                propertyItems.put(key, value);
            }
        }

        if(propertyItems.size() > 0){
            sLogger.info("CSW-Modul: Prepare CSW Module (CSWGetRecordsResponse). Overwrite XPath Values");
            ((CSWGetRecordsRequest)searchRequest).getGetRecordsResponse().setXpathValue(propertyItems);
        }

        sLogger.info("CSW-Modul: Prepare CSW Module (GetRecordsRequestTemplate). Set XPath Values");
        try {
            ((CSWGetRecordsRequest)searchRequest).getRequestTemplate().setGetRecordsTemplate(processId);
        } catch (IOException e) {
            throw new ReaderException("Cannot load template for the getRecords request", e);
        }

        this.processId = processId;

    }
}
