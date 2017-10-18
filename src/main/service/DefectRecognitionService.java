package main.service;

import java.util.Map;

/**
 * Created by island on 2017/10/17.
 */
public interface DefectRecognitionService {
    /**
     *
     * @return
     */
    Map<String, String> checkSingleDefect();

    /**
     *
     * @return
     */
    Map<String, String> checkExtraDefect();

    /**
     *
     * @return
     */
    Map<String, String> checkMissDefect();
}
