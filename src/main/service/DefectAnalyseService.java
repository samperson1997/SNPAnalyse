package main.service;

import java.util.Map;

public interface DefectAnalyseService {

    /**
     *
     * @return
     */
    Map<String, String > getRealPosition();

    /**
     *
     * @return
     */
    Map<String, String > getTotalAnalysis();

    /**
     *
     * @return
     */
    Map<String, String > getRealChangeInfo();

}
