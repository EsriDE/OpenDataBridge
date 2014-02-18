package esride.opendatabridge.reader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 07.02.14
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class MetadataObjectResult {

    private List<MetadataObject> metadataObjectList;

    private int actualStartPosition;
    private int nextStartPosition;

    private boolean hasNextStartPosition;

    public List<MetadataObject> getMetadataObjectList() {
        return metadataObjectList;
    }

    public void setMetadataObjectList(List<MetadataObject> pMetadataObjectList) {
        metadataObjectList = pMetadataObjectList;
    }

    public int getActualStartPosition() {
        return actualStartPosition;
    }

    public void setActualStartPosition(int pActualStartPosition) {
        actualStartPosition = pActualStartPosition;
    }

    public int getNextStartPosition() {
        return nextStartPosition;
    }

    public void setNextStartPosition(int pNextStartPosition) {
        nextStartPosition = pNextStartPosition;
    }

    public boolean isHasNextStartPosition() {
        return hasNextStartPosition;
    }

    public void setHasNextStartPosition(boolean pHasNextStartPosition) {
        hasNextStartPosition = pHasNextStartPosition;
    }
}
