package main.serviceImpl;

import main.daoImpl.AnalyseDaoImpl;
import main.service.AminoAcidAnalyseService;

import java.util.Map;

public class AminoAcidAnalyse implements AminoAcidAnalyseService {

    private Map<String, String> dataMap;
    private Map<String, String> changedMap;
    private Map<String, String> areaMap;
    private DefectRecognition defectRecognition;
    private DefectAnalyse defectAnalyse;
    private AnalyseDaoImpl analyseDaoImpl;

    public AminoAcidAnalyse(String path, int start, int end, double tv1, double tv2) {
        defectRecognition = new DefectRecognition(path);
        defectAnalyse = new DefectAnalyse(path, start, end, tv1, tv2);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
        changedMap = defectAnalyse.getChangedInfo();
        areaMap = defectAnalyse.getArea();
        analyseDaoImpl = new AnalyseDaoImpl();
    }

    /**
     * 氨基酸分析
     */
    @Override
    public Map<String, String> getAcidAnalysis() {

        String[] ycArray = dataMap.get("yc").split(";");
        for (String st : ycArray) {
            int Io = Integer.valueOf(changedMap.get(st));

            // 非内显子, 继续分析
            if (!areaMap.get(st).equals("inner")) {

            }

        }
        return null;
    }
}
