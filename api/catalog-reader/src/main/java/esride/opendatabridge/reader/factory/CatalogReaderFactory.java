package esride.opendatabridge.reader.factory;

import esride.opendatabridge.processinfo.ProcessProperty;
import esride.opendatabridge.reader.IReader;
import esride.opendatabridge.reader.IReaderFactory;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: sma
 * Date: 21.03.13
 * Time: 07:47
 * To change this template use File | Settings | File Templates.
 */
public class CatalogReaderFactory {
    
    private static Logger sLogger = Logger.getLogger(CatalogReaderFactory.class);

    private HashMap<String, IReader> mReaderPool;

    public void setReaderPool(HashMap<String,IReader> pReaderPool) {
        mReaderPool = pReaderPool;
    }

    public IReader newReaderInstance(String readerId, HashMap<String, String> properties, String processId) throws ReaderFactoryException{
        if(sLogger.isDebugEnabled()){
            sLogger.debug("Get Reader Instance with Id: " + readerId);
        }
        IReader reader = mReaderPool.get(readerId);
        if(reader == null){
            sLogger.error("No Reader with ID: " + readerId + " is available");
            throw new ReaderFactoryException("No Reader with ID: " + readerId + " is available");
        }
        //Initialize reader with the correct properties information
        ((IReaderFactory)reader).setProperties(properties, processId);
        return reader;
    }
}
