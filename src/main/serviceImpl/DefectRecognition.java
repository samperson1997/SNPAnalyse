package main.serviceImpl;

import main.dao.GeneDao;
import main.daoImpl.GeneDaoImpl;
import main.service.DefectRecognitionService;
import main.util.Util;

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
        System.out.println(SFInfo);
        String[] locations = la.split("");
        String res = dataMap.get("yc") + ";" + dataMap.get("ys");
        String[] temp = res.split(";");
        int[] nums = new int[temp.length];

        for (int i = 0; i < temp.length; i++) {
            nums[i] = Integer.valueOf(temp[i]) - 1;
        }
        Arrays.sort(nums);

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
        //??????????????
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

        //?????????????
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
        System.out.println("??????ть????? " + (start + 1));
        System.out.println("?????????????? " + (end + 1));
        String gs = "";

        for (int h = start - 10; h < start; h++) {
            gs += locations[h];
        }

        System.out.println("??????ть??10??????? " + gs);
        String ck = new GeneDaoImpl().searchGeneByType("LPL").getSort();
        ck = ck.toUpperCase();
        int sindex = ck.indexOf(gs);
        if (sindex == -1) {
            return "-1;-1";
        }
        int gg = sindex + gs.length();
        System.out.println(gg);
        String cf = "";
        for (int i = 0; i < 20; i++) {
            String ck_s = String.valueOf(ck.charAt(gg + i));

            if (res.indexOf(start + i + 1 + "") == -1) {
                cf += ck_s;
                continue;
            }
            String sf = sfMap.get(start + i + 1 + "");
            int d = sf.indexOf(ck_s);
            cf += sf.charAt(1 - d);
        }
        int eindex = ck.substring(gg).indexOf(cf);
        System.out.println(cf);
        System.out.println("");
        System.out.println(eindex);
        System.out.println("....................");
        if (ck.indexOf(cf) == -1) {
            return sindex + ";-1";
        } else {
            return sindex + ";" + (eindex - 1);
        }

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
        String[] location = dataMap.get("PLOC 2").split(";");
        String[] data9 = dataMap.get("DATA 9").split(";");
        String[] data10 = dataMap.get("DATA 10").split(";");
        String[] data11 = dataMap.get("DATA 11").split(";");
        String[] data12 = dataMap.get("DATA 12").split(";");

        String N_DNA = "";
        String SFInfo = "";
        for (int n = 0; n < DNA.length; n++) {
            N_DNA += DNA[n];
        }

        Map<String, Integer> data = null;

        ArrayList<Integer> yc = new ArrayList<Integer>();
        ArrayList<Integer> ys = new ArrayList<Integer>();
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
            data = new HashMap<String, Integer>();

            data.put(channelMap.get("data9"), getMaxOfRange(data9, location[i], round));
            data.put(channelMap.get("data10"), getMaxOfRange(data10, location[i], round));
            data.put(channelMap.get("data11"), getMaxOfRange(data11, location[i], round));
            data.put(channelMap.get("data12"), getMaxOfRange(data12, location[i], round));

            Map<String, Integer> sortedMap = sortMap(data);

            Iterator<Map.Entry<String, Integer>> it = sortedMap.entrySet().iterator();

            Map.Entry<String, Integer> e1 = it.next();
            Map.Entry<String, Integer> e2 = it.next();

            float n1 = e1.getValue();
            float n2 = e2.getValue();


            if (n1 != 0 && n2 != 0) {

                if (n2 / n1 > tv1) {
                    yc.add(i);
                    DNA[i] = e2.getKey();
                    SFInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                    r1 += (i + 1 + ";");
                } else if (n2 / n1 < tv1 && n2 / n1 > tv2) {
                    ys.add(i);
                    SFInfo += (i + 1 + ":" + e1.getKey() + e2.getKey() + ";");
                    r2 += (i + 1 + ";");
                }
            }

        }

        String U_DNA = "";


        for (int n = 0; n < DNA.length; n++) {
            U_DNA += DNA[n];
        }


        r1 = util.deleteEnd(r1);
        r2 = util.deleteEnd(r2);
        SFInfo = util.deleteEnd(SFInfo);

        if (r2.length() > 0) {
            r2 = r2.substring(0, r2.length() - 1);
        }

        System.out.println("============yc============");
        System.out.println(r1);
        System.out.println("============ys============");
        System.out.println(r2);

        dataMap.put("yc", r1);
        dataMap.put("ys", r2);
        dataMap.put("U_DNA", U_DNA);
        dataMap.put("N_DNA", N_DNA);
        dataMap.put("sf_info", SFInfo);

        System.out.println("U_DNA:" + U_DNA);
        System.out.println("N_DNA:" + N_DNA);
        System.out.println("sf_info:" + SFInfo);

        return dataMap;
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
