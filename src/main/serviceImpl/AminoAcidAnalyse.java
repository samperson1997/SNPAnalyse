package main.serviceImpl;

import main.daoImpl.AnalyseDao;
import main.service.AminoAcidAnalyseService;

import java.util.Map;

public class AminoAcidAnalyse implements AminoAcidAnalyseService {

    private Map<String, String> dataMap;
    private Map<String, String> changedMap;
    private DefectRecognition defectRecognition;
    private DefectAnalyse defectAnalyse;
    private AnalyseDao analyseDao;

    public AminoAcidAnalyse(String path, int start, int end, double tv1, double tv2) {
        defectRecognition = new DefectRecognition(path);
        defectAnalyse = new DefectAnalyse(path, start, end, tv1, tv2);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
        changedMap = defectAnalyse.getChangedInfo();
        analyseDao = new AnalyseDao();
    }

    /**
     * 氨基酸分析
     */
    @Override
    public Map<String, String> getAcidAnalysis() {

        String[] ycArray = dataMap.get("yc").split(";");
        for(String st: ycArray) {
            int Io = Integer.valueOf(changedMap.get(st));

        }
        return null;
    }
}
