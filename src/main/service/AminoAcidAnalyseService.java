package main.service;

import java.util.Map;

public interface AminoAcidAnalyseService {

    /**
     *
     * @return
     */
    Map<String, String> getAcidAnalysis(int start, int end, double tv1, double tv2);
}
