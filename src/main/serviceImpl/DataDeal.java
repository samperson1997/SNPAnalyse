package main.serviceImpl;

import main.daoImpl.AnalyseDaoImpl;
import main.daoImpl.GeneDaoImpl;
import main.model.Analyse;
import main.util.Util;

import java.util.ArrayList;
import java.util.Collections;

public class DataDeal {
    private Util util;

    public DataDeal() {
        util = new Util();
    }


    /**
     * 将疑似异常确认为异常
     * @param filename
     * @param ys
     */
    public void changeYsToYc(String filename, String ys) {

        AnalyseDaoImpl analyseDaoImpl = new AnalyseDaoImpl();
        Analyse analyse = analyseDaoImpl.searchAnalyseByFileName(filename);

        String old_ys = analyse.getYs();
        String old_yc = analyse.getYc();

        String[] ys_arr = old_ys.split(";");
        String[] yc_arr = old_yc.split(";");


        ArrayList<Integer> ys_list = new ArrayList<Integer>();
        ArrayList<Integer> yc_list = new ArrayList<Integer>();

        for (String tt1 : ys_arr) {
            ys_list.add(Integer.valueOf(tt1));
        }

        for (String tt2 : yc_arr) {
            yc_list.add(Integer.valueOf(tt2));
        }

        String[] new_ys = ys.split("");

        for (String temp : new_ys) {
            ys_list.remove(Integer.valueOf(temp));
            yc_list.add(Integer.valueOf(temp));
        }

        Collections.sort(yc_list);

        String r1 = "";
        String r2 = "";

        for (int temp1 : yc_list) {
            r1 += (temp1 + ";");
        }

        for (int temp2 : ys_list) {
            r2 += (temp2 + ";");
        }

        r1 = util.deleteEnd(r1);
        r2 = util.deleteEnd(r2);

        analyse.setYc(r1);
        analyse.setYs(r2);

        analyseDaoImpl.updateAnalyseRes(analyse);

    }


    public void getCKIndex(Analyse analyse) {
        String ck = new GeneDaoImpl().searchGeneByType("LPL").getSort();
        String dna = analyse.getDna();
        String[] yc = analyse.getYc().split(";");
        for (String temp : yc) {
            int index = Integer.valueOf(temp);
            String location = dna.substring(index, index + 20);
            int s_index = ck.indexOf(location);

        }


    }

}
