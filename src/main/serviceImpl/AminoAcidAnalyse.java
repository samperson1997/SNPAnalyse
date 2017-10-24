package main.serviceImpl;

import main.daoImpl.AnalyseDao;
import main.service.AminoAcidAnalyseService;

import java.util.Map;

public class AminoAcidAnalyse implements AminoAcidAnalyseService {

    private Map<String, String> dataMap;
    private DefectRecognition defectRecognition;
    private AnalyseDao analyseDao;

    public AminoAcidAnalyse(String path) {
        defectRecognition = new DefectRecognition(path);
        analyseDao = new AnalyseDao();
    }

    /**
     * 氨基酸分析
     */
    @Override
    public Map<String, String> getAcidAnalysis(int start, int end, double tv1, double tv2) {
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);

        String[] ycArray = dataMap.get("yc").split(";");
        for(String st: ycArray) {

        }
        return null;
    }
}
