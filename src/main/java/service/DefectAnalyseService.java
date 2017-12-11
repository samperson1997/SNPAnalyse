package main.java.service;

import main.java.model.AnalyseResult;

import java.util.Map;

public interface DefectAnalyseService {

    /**
     *
     * @return
     */
    Map<String, AnalyseResult> getAnalyseResult();
}
