package main.java.controller;

import main.java.dao.AnalyseDao;
import main.java.daoImpl.AnalyseDaoImpl;
import main.java.model.Analyse;
import main.java.serviceImpl.DefectRecognition;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class input {

    public static void main(String[] args) throws Exception {
        String path = "./test"; // 文件夹路径修改这里
        traverseFolder(path);
    }

    private static void traverseFolder(String path) throws IOException {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();

            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("folder:" + file2.getAbsolutePath());
                    traverseFolder(file2.getAbsolutePath());
                } else {
                    System.out.println("file:" + file2.getAbsolutePath());
                    startTest(file2);
                }
            }

        }
    }

    private static void startTest(File mFile) throws IOException {

        if (mFile.getAbsolutePath().endsWith(".ab1")) {
//            defectRecognitionTest(mFile);
            defectAnalyseTest(mFile);
        }
    }

    private static void defectRecognitionTest(File mFile) {
        DefectRecognition defectRecognition = new DefectRecognition(mFile.getAbsolutePath());
        int start = 35;
        int end = 10;
        double tv1 = 0.6;  //确认双峰阈值
        double tv2 = 0.5;  //疑似双峰阈值

        Map<String, String> dm = defectRecognition.getAnalyseRes(start, end, tv1, tv2);

        Analyse analyse = new Analyse();
        analyse.setFileName(mFile.getAbsolutePath().split("/")[mFile.getAbsolutePath().split("/").length - 1]);
        analyse.setData9(dm.get("DATA 9"));
        analyse.setData10(dm.get("DATA 10"));
        analyse.setData11(dm.get("DATA 11"));
        analyse.setData12(dm.get("DATA 12"));
        analyse.setDna(dm.get("N_DNA"));
        analyse.setUDna(dm.get("U_DNA"));
        analyse.setYc(dm.get("yc"));
        analyse.setYs(dm.get("ys"));
        analyse.setLocation(dm.get("PLOC 2"));

        AnalyseDao analyseDao = new AnalyseDaoImpl();
        analyseDao.saveAnalyseRes(analyse);

//            if ((dm.get("yc") + ";" + dm.get("ys")).split(";").length > 20) {
//                String lack = defectRecognition.getMissGeneSort(dm);
//                System.out.println("-----lack-------: " + lack);
//                dm.put("lack_gene", lack);
//            }
    }

    private static void defectAnalyseTest(File mFile) {
        int start = 20;
        int end = 10;
        double tv1 = 0.6;  //确认双峰阈值
        double tv2 = 0.5;  //疑似双峰阈值
        DefectAnalyseService defectAnalyse = new DefectAnalyse(mFile.getAbsolutePath(), start, end, tv1, tv2);
        Map<String, AnalyseResult> analyseResultMap = defectAnalyse.getAnalyseResult();

        for (Map.Entry<String, AnalyseResult> entry : analyseResultMap.entrySet()) {
            AnalyseResult analyseResult = entry.getValue();
            analyseResult.setFileName(mFile.getAbsolutePath().split("/")[mFile.getAbsolutePath().split("/").length - 1]);

            AnalyseDao analyseDao = new AnalyseDaoImpl();
            analyseDao.saveAnalyseResultRes(analyseResult);
        }
    }
}
