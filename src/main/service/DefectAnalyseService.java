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
    Map<String, String > getCDSPosition();

    /**
     *
     * @return
     */
    Map<String, String > getArea();

    /**
     *
     * @return
     */
    Map<String, String > getChangedInfo();

}
