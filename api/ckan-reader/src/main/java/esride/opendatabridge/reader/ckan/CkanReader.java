package esride.opendatabridge.reader.ckan;


import esride.opendatabridge.reader.*;

import org.apache.log4j.Logger;
import java.util.*;

/**
 * THe CkanReader extends the CatalogReader. The CkanReader class harvests metadata from Ckan Open Data catalogues
 * and transforms the metadata into an AGOL Item compatible data model. For running an CkanReader component the following resources must be
 * available:
 * - the <p>ckan.url</p> (HTTP GET endpoint for the Ckan catalogue endpoint)
 * - 0..* <p>ckan_request_search_header__{*}</p> properties for overwriting the HTTP Header
 * - 0..* <p>ckan_request_search_param__{*}</p> the request parameters for the select statement
 * - 0..* <p>csw_response_xpath_{*}</p> XPath values for using a different domain model
 *
 * For the AGOL Item transformation an <p>itemelement_{*}.properties</p> files must be available. For each Ckan Metadata resource
 * (like WMS, Shapefile, CSV file) one property file must be created.
 *
 * User: sma
 * Date: 23.04.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class CkanReader extends CatalogReader implements IReaderFactory {
    private static Logger sLogger = Logger.getLogger(CkanReader.class);


    private Properties propertyItems = new Properties();

    private static final String MAX_RECORDS_ID = "limit";
    private static final String START_POSITION_ID = "offset";
    private static final String CATALOG_TYPE = "ckan";

    public void setProperties(HashMap<String, String> properties, String processId) throws ReaderException {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("Ckan-Modul: Prepare Ckan Module. Set Module properties");

        maxRecordsId = MAX_RECORDS_ID;
        startPositionId = START_POSITION_ID;
        catalogType = CATALOG_TYPE;

        catalogUrl = properties.get("ckan.url");
        if(catalogUrl == null || catalogUrl.trim().length() == 0){
            throw new ReaderException("The property ckan.url is missing");
        }
        sLogger.info("Module property: ckan.url=" + catalogUrl);

        Set<String> keySet = properties.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            String value = properties.get(key);

            sLogger.info("Module property:" + key + "=" + value);
            if(key.startsWith("ckan_request_search_header_")){
                headerItems.put(key, value);
            }
            if(key.startsWith("ckan_request_search_param_")){
                templateItems.put(key.substring(26), value);
            }
            if(key.startsWith("csw_response_xpath_")){
                propertyItems.put(key, value);
            }
        }

        if(propertyItems.size() > 0){
            sLogger.info("Ckan-Modul: Prepare Ckan Module (SearchRequest). Overwrite XPath Values");
            ((CkanSearchRequest)searchRequest).getSearchResponse().setXpathValue(propertyItems);
        }

        this.processId = processId;
    }
}
