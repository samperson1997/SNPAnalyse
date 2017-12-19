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
        String SFInfo = dataMap.get("sf_info");
        System.out.println("-----------双峰信息----------: " + SFInfo);
        System.out.println("-----------正常DNA序列----------: " + la);
        String[] locations = la.split("");
        String res = dataMap.get("yc") + ";" + dataMap.get("ys");
        String[] temp = res.split(";");
        int[] nums = new int[temp.length];

        for (int i = 0; i < temp.length; i++) {
            nums[i] = Integer.valueOf(temp[i]) - 1;
        }
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
        System.out.println(standardDna);
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
        for (int i = 0; i < 20; i++) {
            String ck_s = String.valueOf(standardDna.charAt(gg + i));
            System.out.println("===============");
            System.out.println("start + i + 1: " + (start + i + 1));
            System.out.println("ck_s: " + ck_s);
            //todo 修改此处匹配方式 此方法有漏洞
//            if (res.indexOf(start + i + 1 + "") == -1) {
//                cf += ck_s;
//                continue;
//            }
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
//            System.out.println("===============");
        }
        int eindex = standardDna.substring(gg).indexOf(cf);
//        int eindex = standardDna.indexOf(cf);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("cf: " + cf);
        System.out.println("eindex: " + eindex);
        System.out.println("....................");
        if (standardDna.indexOf(cf) == -1) {
            return sindex + ";-1";
        } else {
            return sindex + ";" + (eindex - 1);
        }
    }

    /**
     * 获得在全长中的位置
     */
    public void getLocations(int start) {
        String la = dataMap.get("N_DNA");
        System.out.println("-----------正常DNA序列----------: " + la);
        String[] locations = la.split("");

        String gs = "";

        for (int h = start - 20; h < start; h++) {
            gs += locations[h];
        }

        System.out.println("连续双峰起始处的前20位碱基序列 " + gs);

        //匹配标准DNA序列
//        String standardDna = new GeneDaoImpl().searchGeneByType("LPL").getSort();
        String standardDna = new GeneDaoImpl().searchGeneByType("LMF").getSort();
        standardDna = standardDna.toUpperCase();
        System.out.println(standardDna);
        //匹配在序列中的位置
        int sindex = standardDna.indexOf(gs);
        System.out.println("sindex：" + sindex);
        //匹配失败
        if (sindex == -1) {
            //todo
//            return "-1;-1";
        }

        //匹配成功
        int gg = sindex + gs.length();
        System.out.println("在全长中的位置：" + gg);
    }

    private int getMaxOfRange(String[] data, String location, int round) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int start = Integer.valueOf(location) - round;
        int end = Integer.valueOf(location) + round;
        for (int n = start; n <= end; n++) {
            map.put(n, Integer.valueOf(data[n]));
        }

        Map<Integer, Integer> sortedMap = sortMap(map);

        Iterator<Map.Entry<Integer, Integer>> it = sortedMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int tk = entry.getKey();
            if (tk > start && tk < end && map.get(tk) >= map.get(tk - 1) && map.get(tk) >= map.get(tk + 1)) {
                return entry.getValue();
            }
        }

        return Integer.valueOf(data[(start + end) / 2]);

    }


    @Override
    public Map<String, String> getAnalyseRes(int start, int end, double tv1, double tv2) {
        dataMap = dataAc.getAllData();
        String r1 = "";
        String r2 = "";
        String[] DNA = dataMap.get("PBAS 2").split("");
//        System.out.println(DNA.toString());
        String[] location = dataMap.get("PLOC 2").split(";");
        String[] data9 = dataMap.get("DATA 9").split(";");
        String[] data10 = dataMap.get("DATA 10").split(";");
        String[] data11 = dataMap.get("DATA 11").split(";");
        String[] data12 = dataMap.get("DATA 12").split(";");

        //正常的DNA序列
        String N_DNA = "";
        String doublePeakInfo = ""; //双峰信息 原变量名为SFInfo
        for (int n = 0; n < DNA.length; n++) {
            N_DNA += DNA[n];
        }

        Map<String, Integer> data = null;

        ArrayList<Integer> confirmedDoublePeak = new ArrayList<Integer>(); //确认双峰异常 原变量名为：yc
        ArrayList<Integer> suspectedDoublePeak = new ArrayList<Integer>(); //疑似双峰异常 原变量名为：ys
        int round = data9.length / location.length / 2;

        Map<String, String> channelMap = new HashMap<String, String>();
        int s = start;
        while (channelMap.size() < 4) {
            Map<String, Integer> type = new HashMap<String, Integer>(4);
            type.put("data9", Integer.valueOf(data9[Integer.valueOf(location[s])]));
            type.put("data10", Integer.valueOf(data10[Integer.valueOf(location[s])]));
            type.put("data11", Integer.valueOf(data11[Integer.valueOf(location[s])]));
            type.put("data12", Integer.valueOf(data12[Integer.valueOf(location[s])]));
            Map<String, Integer> temp = sortMap(type);
            Iterator<Map.Entry<String, Integer>> it = temp.entrySet().iterator();
            String ts = it.next().getKey();
            if (channelMap.get(ts) == null) {
                channelMap.put(ts, DNA[s]);
            }
            s++;
        }

        for (int i = start; i < location.length - end; i++) {
            List<Map.Entry<String, Integer>> curValues = getTwoLargerValue(channelMap, data9, data10, data11, data12, location, i, round);
            List<Map.Entry<String, Integer>> preValues = getTwoLargerValue(channelMap, data9, data10, data11, data12, location, i - 1, round);
            List<Map.Entry<String, Integer>> postValues = getTwoLargerValue(channelMap, data9, data10, data11, data12, location, i + 1, round);

            Map.Entry<String, Integer> e1 = curValues.get(0);
            Map.Entry<String, Integer> e2 = curValues.get(1);
            Map.Entry<String, Integer> preE1 = getSameKeyMapEntry(e1, preValues);
            Map.Entry<String, Integer> postE1 = getSameKeyMapEntry(e1, postValues);
            Map.Entry<String, Integer> preE2 = getSameKeyMapEntry(e2, preValues);
            Map.Entry<String, Integer> postE2 = getSameKeyMapEntry(e2, postValues);

            double n1 = e1.getValue();
            double n2 = e2.getValue();
            double preN1 = preE1.getValue();
            double preN2 = preE2.getValue();
            double postN1 = postE1.getValue();
            double postN2 = postE2.getValue();


//            System.out.println("===================");
//            System.out.println("i: " + i);
//            System.out.println("n1: " + n1);
//            System.out.println("n2: " + n2);
//            System.out.println("===================");

            if (n1 != 0 && n2 != 0) {

                if (n1 >= preN1 && n2 >= preN2 && n1 >= postN1 && n2 >= postN2) {
                    if (n2 / n1 > tv1) {
                        confirmedDoublePeak.add(i);
                        DNA[i] = e2.getKey();
                        doublePeakInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                        r1 += (i + 1 + ";");
                    } else if (n2 / n1 < tv1 && n2 / n1 > tv2) {
                        suspectedDoublePeak.add(i);
                        doublePeakInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                        r2 += (i + 1 + ";");
                    }
                }
            }

        }

        String U_DNA = "";


        for (int n = 0; n < DNA.length; n++) {
            U_DNA += DNA[n];
        }


        r1 = util.deleteEnd(r1);
        r2 = util.deleteEnd(r2);
        doublePeakInfo = util.deleteEnd(doublePeakInfo);

        if (r2.length() > 0) {
            r2 = r2.substring(0, r2.length() - 1);
        }

        dataMap.put("yc", r1);
        dataMap.put("ys", r2);
        dataMap.put("U_DNA", U_DNA);
        dataMap.put("N_DNA", N_DNA);
        dataMap.put("sf_info", doublePeakInfo);
        return dataMap;
    }

    private List<Map.Entry<String, Integer>> getTwoLargerValue(Map<String, String> channelMap, String[] data9, String[] data10, String[] data11, String[] data12, String[] location, int i, int round) {
        Map<String, Integer> data = new HashMap<String, Integer>();

        data.put(channelMap.get("data9"), getMaxOfRange(data9, location[i], round));
        data.put(channelMap.get("data10"), getMaxOfRange(data10, location[i], round));
        data.put(channelMap.get("data11"), getMaxOfRange(data11, location[i], round));
        data.put(channelMap.get("data12"), getMaxOfRange(data12, location[i], round));

        Map<String, Integer> sortedMap = sortMap(data);

        Iterator<Map.Entry<String, Integer>> it = sortedMap.entrySet().iterator();

        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            list.add(it.next());
        }
        return list;
    }

    private Map.Entry<String, Integer> getSameKeyMapEntry(Map.Entry<String, Integer> entry, List<Map.Entry<String, Integer>> entries) {
        for (int i = 0; i < entries.size(); i++) {
            if (entry.getKey().equals(entries.get(i).getKey())) {
                return entries.get(i);
            }
        }

        return null;
    }

    private <T> Map<T, Integer> sortMap(Map<T, Integer> oldMap) {
        ArrayList<Map.Entry<T, Integer>> list = new ArrayList<Map.Entry<T, Integer>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<T, Integer>>() {

            @Override
            public int compare(Map.Entry<T, Integer> arg0, Map.Entry<T, Integer> arg1) {
                return arg1.getValue() - arg0.getValue();
            }
        });
        Map<T, Integer> newMap = new LinkedHashMap<T, Integer>();
        for (int i = 0; i < list.size(); i++) {
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return newMap;
    }

}
