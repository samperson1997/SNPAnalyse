package main.java.util;

import java.util.*;

public class Util {


    public String deleteEnd(String s) {
        if (s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public <T> Map<T, Integer> sortMap(Map<T, Integer> oldMap) {
        ArrayList<Map.Entry<T, Integer>> list = new ArrayList<Map.Entry<T, Integer>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<T, Integer>>() {

            @Override
            public int compare(Map.Entry<T, Integer> arg0, Map.Entry<T, Integer> arg1) {
                return Math.abs(arg1.getValue()) - Math.abs(arg0.getValue());
            }
        });
        Map<T, Integer> newMap = new LinkedHashMap<T, Integer>();
//        System.out.println("=====================");

        for (int i = 0; i < list.size(); i++) {
//            if (list.size() < 5) {
//                System.out.println(list.get(i).getValue());
//            }
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
//        System.out.println("=====================");

        return newMap;
    }
}
