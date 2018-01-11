package main.java.serviceImpl;

import main.java.service.SequenceFileCheck;
import main.java.util.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public boolean checkGeneFileIsNormal() {
        getGeneInfo();

        int round = GValues.length / location.length / 2;

        int abnormalRangeNums = 0;

        System.out.println("peakNums：");
        for (int position = start; position < location.length - end; position++) {
            int peakNums = getPeakNumsOfRange(round, position);
            System.out.println("position: " + position + "; peakNums: " + peakNums + " ");
            if (peakNums >= 3) {
                return false;
            }
            if (peakNums > 2) {
                abnormalRangeNums++;
            }
        }
        System.out.println();

        //暂且认为如果同一段周期内有多个峰值的个数大于20段，则为测序失败
        if (abnormalRangeNums > 20) {
            return false;
        }

        return true;
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

            // 暂且认为峰的判定为峰值-两边值>200
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
