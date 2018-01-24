package main.java.serviceImpl;

import main.java.daoImpl.GeneDaoImpl;
import main.java.service.DefectRecognitionService;
import main.java.util.Util;

import java.util.*;

/**
 * Created by island on 2017/10/17.
 */
public class DefectRecognition implements DefectRecognitionService {
    private DataAc dataAc;
    private Util util;

    public DefectRecognition(String path) {
        dataAc = new DataAc(path);
        util = new Util();
    }

    private Map<String, String> dataMap;

    private Map<String, String> checkSingleDefect() {
        return null;
    }

    private Map<String, String> checkExtraDefect() {
        return null;
    }

    private Map<String, String> checkMissDefect() {
        return null;
    }

    public String getMissGeneSort(Map<String, String> dataMap) {
        String la = dataMap.get("N_DNA");
        String SFInfo = dataMap.get("sf_info") + ";" + dataMap.get("smallPeakInfo");
        System.out.println("========================");
        System.out.println("-----------双峰信息----------: " + SFInfo);
        System.out.println("-----------正常DNA序列----------: " + la);
        System.out.println("========================");
        String[] locations = la.split("");
        String res = dataMap.get("yc") + ";" + dataMap.get("ys");
        String[] temp = res.split(";");
        int[] nums = new int[temp.length];

        for (int i = 0; i < temp.length; i++) {
            nums[i] = Integer.valueOf(temp[i]) - 1;
        }

//        res =

        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + "; ");
        }

        String[] tt = SFInfo.split(";");
        Map<String, String> sfMap = new HashMap<String, String>();
        for (String sft : tt) {
            String[] ta = sft.split(":");
            sfMap.put(ta[0], ta[1]);
        }

        int start = nums[0];
        //int start_index=0;
        int end = nums[nums.length - 1];
        //int end_index=nums.length-1;

        for (int j = 0; j < nums.length - 1; j++) {
            int n1 = nums[j];
            int n2 = nums[j + 1];
            if ((n2 - n1) > 4) {
                start = n2;
                //start_index=j+1;
            } else {
                break;
            }
        }

        for (int m = nums.length - 1; m > 0; m--) {
            int n3 = nums[m];
            int n4 = nums[m - 1];
            if ((n3 - n4) > 4) {
                end = n3;
                //end_index=m-1;
            } else {
                break;
            }
        }
        System.out.println("");
        System.out.println("连续双峰起始位置：" + (start + 1));
        System.out.println("连续双峰结束位置 " + (end + 1));

        String gs = "";

        for (int h = start - 20; h < start; h++) {
            gs += locations[h];
        }

        System.out.println("连续双峰起始处的前20位碱基序列 " + gs);

        //匹配标准DNA序列
//        String standardDna = new GeneDaoImpl().searchGeneByType("LPL").getSort();
        String standardDna = new GeneDaoImpl().searchGeneByType("LMF").getSort();
        standardDna = standardDna.toUpperCase();
//        System.out.println(standardDna);
        //匹配在序列中的位置
        int sindex = standardDna.indexOf(gs);
        System.out.println("sindex：" + sindex);
        //匹配失败
        if (sindex == -1) {
            return "-1;-1";
        }

        //匹配成功
        int gg = sindex + gs.length();
        System.out.println("在全长中的位置：" + gg);

        String cf = "";

        String ckString = "";
        for (int i = 0; i < 20; i++) {
            String ck_s = String.valueOf(standardDna.charAt(gg + i));
            ckString += ck_s;

            System.out.println("===============");
            System.out.println("start + i + 1: " + (start + i + 1));
            System.out.println("ck_s: " + ck_s);

            String sf = sfMap.get(start + i + 1 + "");
            if (sf != null) {
                System.out.println("sf: " + sf);
                int d = sf.indexOf(ck_s);
                //todo
                if (d == -1) {
                    d = sf.indexOf('N');
                }
                cf += sf.charAt(1 - d);
                System.out.println("cf: " + cf);
            } else {
                cf += ck_s;
                System.out.println("cf: " + cf);
            }

//            List<Integer> positions = util.getAllPlaces(standardDna, cf);
//            for (int j = 0; j < positions.size(); j++) {
//                System.out.print(positions.get(j) + ";");
//            }
//            System.out.println();
        }
        int eindex = standardDna.indexOf(cf);
        System.out.println("正常的20位：" + ckString);
//        int eindex = standardDna.indexOf(cf);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("cf: " + cf);
        System.out.println("eindex: " + eindex);
        return sindex + ";" + eindex;
    }

    private int getMaxOfRange(String[] data, String location, int round) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//        System.out.println(round);
        int half = round / 2 + 1;
//        System.out.println(half);
        int start = Integer.valueOf(location) - round;
        int end = Integer.valueOf(location) + round;
        int firstValue = 0;
        int lastValue = 0;

        for (int n = start; n <= end; n++) {
            map.put(n, Integer.valueOf(data[n]));
            if (n == start) {
                firstValue = Integer.valueOf(data[n]);
            }

            if (n == end) {
                lastValue = Integer.valueOf(data[n]);
            }
        }

        if (firstValue == 0) {
            firstValue = 1;
        }
        if (lastValue == 0) {
            lastValue = 1;
        }

        Map<Integer, Integer> sortedMap = util.sortMap(map);

        //该值用于判断该范围最大值是否为峰值
        double isPeak = 0.6;

        Iterator<Map.Entry<Integer, Integer>> it = sortedMap.entrySet().iterator();
//        System.out.println("==================");
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int tk = entry.getKey();
//            System.out.println(entry.getValue());
            if (tk > start && tk < end && map.get(tk) >= map.get(tk - 1) && map.get(tk) >= map.get(tk + 1)) {
                int curValue = entry.getValue();
                if (curValue != 0) {
                    if (firstValue / curValue < isPeak && lastValue / curValue < isPeak) {
                        return entry.getValue();
                    } else {
                        return -Integer.valueOf(data[(start + end) / 2]);
                    }
                }
            }
        }
//        System.out.println("==================");

        return -Integer.valueOf(data[(start + end) / 2]);

//        return 0;
    }
    //tgtgtgcagtaggagccggggtc


    @Override
    public Map<String, String> getAnalyseRes(int start, int end, double tv1, double tv2) {
        // todo
//        tv2 = 0.1;

        dataMap = dataAc.getAllData();
        String r1 = "";
        String r2 = "";
        System.out.println(dataMap.get("PBAS 2"));
        String[] DNA = dataMap.get("PBAS 2").split("");
        String[] location = dataMap.get("PLOC 2").split(";");
        String[] data9 = dataMap.get("DATA 9").split(";");
        String[] data10 = dataMap.get("DATA 10").split(";");
        String[] data11 = dataMap.get("DATA 11").split(";");
        String[] data12 = dataMap.get("DATA 12").split(";");

        //正常的DNA序列
        String N_DNA = "";
        String doublePeakInfo = ""; //双峰信息 原变量名为SFInfo
        String smallPeakInfo = "";

        for (int n = 0; n < DNA.length; n++) {
            N_DNA += DNA[n];
        }

        Map<String, Integer> data = null;

        ArrayList<Integer> confirmedDoublePeak = new ArrayList<Integer>(); //确认双峰异常 原变量名为：yc
        ArrayList<Integer> suspectedDoublePeak = new ArrayList<Integer>(); //疑似双峰异常 原变量名为：ys
        ArrayList<Integer> smallPeak = new ArrayList<Integer>(); //疑似双峰异常 原变量名为：ys
        ArrayList<Integer> sequencingError = new ArrayList<Integer>(); //疑似双峰异常 原变量名为：ys
        int round = 6;
//        for (int i = 1; i < location.length; i++) {
//            int curRound = (Integer.parseInt(location[i]) - Integer.parseInt(location[i - 1])) / 2;
//            if (curRound < round) {
//                round = curRound;
//            }
//        }
//        round = Math.min(round, data11.length / location.length / 2);
//        round = Math.min(round, data12.length / location.length / 2);

        System.out.println(round);

        Map<String, String> channelMap = new HashMap<String, String>();
        channelMap.put("data9", "G");
        channelMap.put("data10", "A");
        channelMap.put("data11", "T");
        channelMap.put("data12", "C");

        for (int i = start; i < location.length - end; i++) {
            List<Map.Entry<String, Integer>> curValues = getTwoLargerValue(channelMap, data9, data10, data11, data12, location, i, round);
            Map.Entry<String, Integer> e1 = curValues.get(0);
            Map.Entry<String, Integer> e2 = curValues.get(1);
            double n1 = e1.getValue();
            double n2 = e2.getValue();

//            System.out.println("===================");
//            System.out.println("i: " + i);
//            System.out.println("n1: " + n1 + ";" + e1.getKey());
//            System.out.println("n2: " + n2 + ";" + e2.getKey());
//            System.out.println("r1: " + r1);
//            System.out.println("r2: " + r2);
//            System.out.println("===================");

            //该值用于判断双峰是否由背景值过高造成
            double tv3 = 0.2;

            //该值用于判断连续双峰中部分另一峰值较低的背景峰
            double tv4 = 0.1;


            if (n1 > 0 && n2 > 0) {
                if (n2 / n1 > tv2) {
                    if (isSlope(e1, data9, data10, data11, data12, location, i) ||
                            isSlope(e2, data9, data10, data11, data12, location, i)) {
                        sequencingError.add(i);
                        continue;
                    }
                }
                if (n2 / n1 > tv1) {
                    confirmedDoublePeak.add(i);
                    DNA[i] = e2.getKey();
                    doublePeakInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                    r1 += (i + 1 + ";");
                } else if (n2 / n1 < tv1 && n2 / n1 > tv2) {
                    suspectedDoublePeak.add(i);
                    doublePeakInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                    r2 += (i + 1 + ";");
                } else if (n2 / n1 < tv2 && n2 / n1 > tv4) {
                    smallPeak.add(i);
                    smallPeakInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                }
//                if (n2 / n1)
            } else {
                if (Math.abs(n2) / Math.abs(n1) > tv3) {
                    sequencingError.add(i);
                }

            }

        }

        String U_DNA = "";

        for (int n = 0; n < DNA.length; n++) {
            U_DNA += DNA[n];
        }

        String sequencingErrorString = "";
        for (int i = 0; i < sequencingError.size(); i++) {
            sequencingErrorString += sequencingError.get(i) + 1 + ";";
        }

        String smallPeakString = "";
        for (int i = 0; i < smallPeak.size(); i++) {
            smallPeakString += smallPeak.get(i) + 1 + ";";
        }

        //deleteEnd 删除以;连接的数字组成的字符串最后一个;
        dataMap.put("yc", util.deleteEnd(r1));
        dataMap.put("ys", util.deleteEnd(r2));
        dataMap.put("U_DNA", U_DNA);
        dataMap.put("N_DNA", N_DNA);
        dataMap.put("sf_info", util.deleteEnd(doublePeakInfo));
        dataMap.put("sequencingError", util.deleteEnd(sequencingErrorString));
        dataMap.put("smallPeaks", util.deleteEnd(smallPeakString));
        dataMap.put("smallPeakInfo", util.deleteEnd(smallPeakInfo));
//        System.out.println(sequencingErrorString);

        return dataMap;
    }

    private List<Map.Entry<String, Integer>> getTwoLargerValue(Map<String, String> channelMap, String[] data9, String[] data10, String[] data11, String[] data12, String[] location, int i, int round) {
        Map<String, Integer> data = new HashMap<String, Integer>();

        data.put(channelMap.get("data9"), getMaxOfRange(data9, location[i], round));
        data.put(channelMap.get("data10"), getMaxOfRange(data10, location[i], round));
        data.put(channelMap.get("data11"), getMaxOfRange(data11, location[i], round));
        data.put(channelMap.get("data12"), getMaxOfRange(data12, location[i], round));
//        System.out.println("=====================");
//
//        System.out.println(channelMap.get("data9") + " :" + data.get(channelMap.get("data9")));
//        System.out.println(channelMap.get("data10") + " :" + data.get(channelMap.get("data10")));
//        System.out.println(channelMap.get("data11") + " :" + data.get(channelMap.get("data11")));
//        System.out.println(channelMap.get("data12") + " :" + data.get(channelMap.get("data12")));
//        System.out.println("preMapSize: " + data.size());
//
//        for (String key: data.keySet()) {
//            System.out.println(key);
//        }

        Map<String, Integer> sortedMap = util.sortMap(data);
//        System.out.println("postMapSize: " + sortedMap.size());
//        System.out.println("=====================");
//        for (String key: sortedMap.keySet()) {
//            System.out.println(sortedMap.get(key));
//        }
//        System.out.println("=====================");
        return new ArrayList<>(sortedMap.entrySet());
    }

    private Map.Entry<String, Integer> getSameKeyMapEntry(Map.Entry<String, Integer> entry, List<Map.Entry<String, Integer>> entries) {
        for (int i = 0; i < entries.size(); i++) {
            if (entry.getKey().equals(entries.get(i).getKey())) {
                return entries.get(i);
            }
        }

        return null;
    }

    private boolean isSlope(Map.Entry<String, Integer> e, String[] data9, String[] data10, String[] data11, String[] data12, String[] location, int i) {
        String[] data;
        switch (e.getKey()) {
            case "G":
                data = data9;
                break;
            case "A":
                data = data10;
                break;
            case "T":
                data = data11;
                break;
            case "C":
                data = data12;
                break;
            default:
                data = new String[0];
        }


        if (data.length > i + 1) {
            int preValue = Integer.parseInt(data[Integer.parseInt(location[i - 1])]);
            int curValye = Integer.parseInt(data[Integer.parseInt(location[i])]);
            int postValue = Integer.parseInt(data[Integer.parseInt(location[i + 1])]);

            if (preValue < 0 || postValue < 0) {
                return true;
            }
        }

        return false;
    }

}
