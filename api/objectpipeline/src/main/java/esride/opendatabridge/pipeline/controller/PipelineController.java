package esride.opendatabridge.pipeline.controller;

import esride.opendatabridge.pipeline.IPipeline;
import esride.opendatabridge.pipeline.InvalidObjectException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: sma
 * Date: 11.02.14
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class PipelineController implements IPipeline{

    private ArrayList<IPipeline> pipelineList;

    public void setPipelineList(ArrayList<IPipeline> pPipelineList) {
        pipelineList = pPipelineList;
    }

    public void examineAndChangeObject(HashMap<String, String> elements) throws InvalidObjectException {
        if(pipelineList != null){
            for(int i=0; i<pipelineList.size(); i++){
                pipelineList.get(i).examineAndChangeObject(elements);
            }
        }
    }
}
