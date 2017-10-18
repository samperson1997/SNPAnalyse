package main.serviceImpl;

import main.service.DefectRecognitionService;

import java.util.Map;

/**
 * Created by island on 2017/10/17.
 */
public class DefectRecognition implements DefectRecognitionService{
    private Map<String, String> dataMap;

    @Override
    public Map<String, String> checkSingleDefect() {
        return null;
    }

    @Override
    public Map<String, String> checkExtraDefect() {
        return null;
    }

    @Override
    public Map<String, String> checkMissDefect() {
        return null;
    }

    private int getMaxOfRange(int start, int end) {
        return 0;
    }

    private Map<Integer, Integer> getSortedMap() {
        return null;
    }

    private Map<String, String> getRes(){
        return null;
    }

}
