package esride.opendatabridge.pipeline;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 11.02.14
 * Time: 09:21
 * To change this template use File | Settings | File Templates.
 */
public interface IPipeline {

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException;
}
