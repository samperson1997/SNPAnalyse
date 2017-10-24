package main.serviceImpl;

import main.service.DefectAnalyseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefectAnalyse implements DefectAnalyseService {

    private Map<String, String> dataMap;
    private DefectRecognition defectRecognition;

    public DefectAnalyse(String path, int start, int end, double tv1, double tv2) {
        defectRecognition = new DefectRecognition(path);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
    }

    /**
     * 异常在完整DNA片段上的真实位置
     */
    @Override
    public Map<String, String> getRealPosition() {
        return null;
    }


    /**
     * 异常所在DNA片段区域
     */
    @Override
    public Map<String, String> getTotalAnalysis() {
        return null;
    }


    /**
     * key: 点位
     * value: 变化信息，正常基因-->异常基因
     * @return
     */
    @Override
    public Map<String, String> getChangedInfo() {
        Map<String, String> changedMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();
        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");
            changedMap.put(changedInfo[0], changedInfo[1].charAt(0) + "-->" + changedInfo[1].charAt(1));
        }

        return changedMap;
    }
}
