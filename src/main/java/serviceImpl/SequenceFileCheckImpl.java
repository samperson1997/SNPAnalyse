package main.java.serviceImpl;

import main.java.service.SequenceFileCheck;
import main.java.util.Util;

import java.util.*;

public class SequenceFileCheckImpl implements SequenceFileCheck {

    private DataAc dataAc;
    private Util util;
    private int start, end;
    private Map<String, String> dataMap;
    private String[] location;
    private String[] GValues;
    private String[] AValues;
    private String[] TValues;
    private String[] CValues;

    public SequenceFileCheckImpl(String path, int start, int end) {
        dataAc = new DataAc(path);
        util = new Util();
        this.start = start;
        this.end = end;
    }

    @Override
    public String checkGeneFileIsNormal() {
        getGeneInfo();

        if (checkPeakNumsError()) {
            return "fail";
        } else if (checkPeakRangeError().equals("fail")) {
            return "fail";
        } else if (checkPeakRangeError().equals("headFail")) {
            return "headFail";
        } else {
            return "success";
        }

    }

    private void getGeneInfo() {
        dataMap = dataAc.getAllData();
        // 获得序列位置数组（横坐标）
        location = dataMap.get("PLOC 2").split(";");
        // 获得G对应值数组
        GValues = dataMap.get("DATA 9").split(";");
        // 获得A对应值数组
        AValues = dataMap.get("DATA 10").split(";");
        // 获得T对应值数组
        TValues = dataMap.get("DATA 11").split(";");
        // 获得C对应值数组
        CValues = dataMap.get("DATA 12").split(";");

    }

    /**
     * 根据ATCG各个峰值的间距来判断是否测序失败
     *
     * @return
     */
    private String checkPeakRangeError() {

        int round = GValues.length / location.length / 2;
        ArrayList<Integer> peakPositions = new ArrayList<>();

        String[][] ATCGValues = {GValues, AValues, TValues, CValues};

        for (int position = start; position < location.length - end; position++) {
            for (int i = 0; i < ATCGValues.length; i++) {
                int peakPosition = getPeakPositionOfRange(round, position, ATCGValues[i]);
                if (peakPosition != -1) {
                    peakPositions.add(peakPosition);
                }
            }
        }
        Collections.sort(peakPositions);

        // 计算峰之间的间距 因为头部不稳定，从第五个开始取
        ArrayList<Integer> peakRanges = new ArrayList<>();
        ArrayList<Integer> allPeakRanges = new ArrayList<>();

        for (int i = 1; i < peakPositions.size(); i++) {
            allPeakRanges.add(peakPositions.get(i) - peakPositions.get(i - 1));
            if (i > 5) {
                peakRanges.add(peakPositions.get(i) - peakPositions.get(i - 1));
            }
        }


        System.out.println("peakRanges:");
        for (int p :
                peakRanges) {
            System.out.print(p + " ");
        }
        System.out.println();

        System.out.println("all-peakRanges:");
        for (int p :
                allPeakRanges) {
            System.out.print(p + " ");
        }
        System.out.println();

        double variance = util.calVariance(peakRanges);
        double allVariance = util.calVariance(allPeakRanges);

        System.out.println("variance: " + variance);
        System.out.println("allVariance: " + allVariance);

        //TODO 暂且认为去除头部后的峰值的间距的方差>30 判断为测序失败，如果所有峰值间距的方差>30，则为头部混乱
        if (variance > 30) {
            return "fail";
        } else if (variance <= 30 && allVariance > 30) {
            return "headFail";
        }

        return "success";
    }

    /**
     * 获得一段周期内，峰顶的位置
     *
     * @param round    周期长度
     * @param position 当前位置
     * @param values   当前检查的碱基数组
     * @return
     */
    private int getPeakPositionOfRange(int round, int position, String[] values) {

        int halfRound = round / 2 + 1;
        int roundStart = Integer.valueOf(location[position]) - halfRound;
        int roundEnd = Integer.valueOf(location[position]) + halfRound;


        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int n = roundStart; n <= roundEnd; n++) {
            map.put(n, Integer.valueOf(values[n]));
        }

        Map<Integer, Integer> sortedGMap = util.sortMap(map);
        Iterator<Map.Entry<Integer, Integer>> iterator = sortedGMap.entrySet().iterator();

        //TODO 暂且认为峰的判定为峰值-两边值>200
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            int tk = entry.getKey();
            int value = entry.getValue();
            if (tk > roundStart && tk < roundEnd && map.get(tk) >= map.get(tk - 1) && map.get(tk) >= map.get(tk + 1)
                    && value - map.get(roundStart) > 200 && value - map.get(roundEnd) > 200) {
                return tk;
            }
        }

        return -1;
    }


    /**
     * 根据一段周期内的峰值个数情况来判断测序是否失败
     *
     * @return
     */
    private boolean checkPeakNumsError() {
        int abnormalRangeNums = 0;

        int round = GValues.length / location.length / 2;

//        System.out.println("peakNums:");
        for (int position = start; position < location.length - end; position++) {
            int peakNums = getPeakNumsOfRange(round, position);
//            System.out.println("position: " + position + "; peakNums: " + peakNums + " ");
            if (peakNums >= 3) {
                return false;
            }
            if (peakNums > 2) {
                abnormalRangeNums++;
            }
        }
//        System.out.println();

        //暂且认为如果同一段周期内有多个峰值的个数大于20段，则为测序失败
        if (abnormalRangeNums > 20) {
            return true;
        }
        return false;
    }

    /**
     * 判断在一段周期内，ATCG的峰值情况
     *
     * @param round    周期长度
     * @param position 当前位置
     * @return
     */
    private int getPeakNumsOfRange(int round, int position) {

        int peakNums = 0;

        int halfRound = round / 2 + 1;
        int roundStart = Integer.valueOf(location[position]) - halfRound;
        int roundEnd = Integer.valueOf(location[position]) + halfRound;

        String[][] ATCGValues = {GValues, AValues, TValues, CValues};

        for (int i = 0; i < ATCGValues.length; i++) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();

            for (int n = roundStart; n <= roundEnd; n++) {
                map.put(n, Integer.valueOf(ATCGValues[i][n]));
            }

            Map<Integer, Integer> sortedGMap = util.sortMap(map);
            Iterator<Map.Entry<Integer, Integer>> iterator = sortedGMap.entrySet().iterator();

            //TODO 暂且认为峰的判定为峰值-两边值>200
            while (iterator.hasNext()) {
                Map.Entry<Integer, Integer> entry = iterator.next();
                int tk = entry.getKey();
                int value = entry.getValue();
                if (tk > roundStart && tk < roundEnd && map.get(tk) >= map.get(tk - 1) && map.get(tk) >= map.get(tk + 1)
                        && value - map.get(roundStart) > 200 && value - map.get(roundEnd) > 200) {
                    peakNums++;
                }
            }
        }

        return peakNums;
    }

    private void testOutput() {
        System.out.println("length loaction: " + location.length);
        System.out.println("length G: " + GValues.length);
        System.out.println("length A: " + AValues.length);
        System.out.println("length T: " + TValues.length);
        System.out.println("length C: " + CValues.length);
        System.out.println("location[]: ");
        for (int position = 0; position < location.length; position++) {
            System.out.print(position + " ");
        }
        System.out.println("GValues[]: ");
        for (int position = 0; position < location.length; position++) {
            System.out.print(GValues[position] + " ");
        }
        System.out.println("AValues[]: ");
        for (int position = 0; position < location.length; position++) {
            System.out.print(AValues[position] + " ");
        }
        System.out.println("TValues[]: ");
        for (int position = 0; position < location.length; position++) {
            System.out.print(TValues[position] + " ");
        }
        System.out.println("CValues[]: ");
        for (int position = 0; position < location.length; position++) {
            System.out.print(CValues[position] + " ");
        }
    }
}
