package main.java.service;

import java.util.Map;

/**
 * Created by island on 2017/10/17.
 */
public interface DefectRecognitionService {
    /**
     * get result of defect recognition
     * @return Map<String, String>
     */
    Map<String, String> getAnalyseRes(int start, int end, double tv1, double tv2);
}
