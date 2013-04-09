package esride.opendatabridge.reader.csw;


import esride.opendatabridge.processinfo.ProcessProperty;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.IReaderFactory;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 05.04.13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class CSWReader implements IReader, IReaderFactory {
    private static Logger sLogger = Logger.getLogger(CSWReader.class);
    
    public void getItemsFromCatalog() {
        sLogger.info("------------------------------------------------ ");
        sLogger.info("CSW-Modul: Start Requesting Metadata from catalog");

        //start with position = 1
        //create MD-Resources


        //Second Request over the n elements
        //Template Mechanismus: http://www.antlr.org/wiki/display/ST/Five+minute+Introduction

        //compute every metadataset, if wms or view get capabilities
    }

    public void setProperties(List<ProcessProperty> properties) {

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
