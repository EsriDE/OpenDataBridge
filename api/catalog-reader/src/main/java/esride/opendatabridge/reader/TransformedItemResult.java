package esride.opendatabridge.reader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 11.02.14
 * Time: 08:40
 * To change this template use File | Settings | File Templates.
 */
public class TransformedItemResult {

    private List<TransformedItem> transformedResultList;

    private int actualStartPosition;
    private int nextStartPosition;

    private boolean hasNextStartPosition;

    public List<TransformedItem> getTransformedResultList() {
        return transformedResultList;
    }

    public void setTransformedResultList(List<TransformedItem> pTransformedResultList) {
        transformedResultList = pTransformedResultList;
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
