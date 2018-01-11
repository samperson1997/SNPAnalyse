package main.java.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import main.java.dao.AnalyseDao;
import main.java.daoImpl.AnalyseDaoImpl;
import main.java.model.Analyse;
import main.java.model.AnalyseResult;
import main.java.service.DefectAnalyseService;
import main.java.service.SequenceFileCheck;
import main.java.serviceImpl.DefectAnalyse;
import main.java.serviceImpl.DefectRecognition;
import main.java.serviceImpl.SequenceFileCheckImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.sf.json.JSONObject;

@RestController
@EnableAutoConfiguration
public class DCollection {

    @RequestMapping("/")
    String home() {
        return "welcome";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String getAll(HttpServletRequest request) throws IOException {
        JSONObject jsonObject = null;
        int start = 35;
        int end = 10;
        double tv1 = 0.6;  //确认双峰阈值
        double tv2 = 0.5;  //疑似双峰阈值

        Map<String, String> dm;

        if (request.getParameter("start") != null) {
            start = Integer.valueOf(request.getParameter("start"));
        }

        if (request.getParameter("end") != null) {
            end = Integer.valueOf(request.getParameter("end"));
        }

        if (request.getParameter("tv1") != null) {
            tv1 = Double.valueOf(request.getParameter("tv1"));
        }

        if (request.getParameter("tv2") != null) {
            tv2 = Double.valueOf(request.getParameter("tv2"));
        }

        start = 35;

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile mFile = multipartRequest.getFile("file");
        int fileSize = (int) mFile.getSize();

        if (!mFile.isEmpty()) {
            String path = request.getSession().getServletContext().getRealPath("/");
            String filename = mFile.getOriginalFilename();
            java.io.InputStream inputStream = mFile.getInputStream();
            byte[] b = new byte[fileSize];
            int length = inputStream.read(b);
            path += "/upload" + filename;
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(b, 0, length);
            inputStream.close();
            outputStream.close();
//			DataAc dataAc = new DataAc(path);
            DefectRecognition defectRecognition = new DefectRecognition(path);

            // TODO 这里开始异常的分析
//            DefectAnalyseService defectAnalyse = new DefectAnalyse(path, start, end, tv1, tv2);
//            defectAnalyse.getAnalyseResult();


            SequenceFileCheck sequenceFileCheck = new SequenceFileCheckImpl(path, start, end);
            if (!sequenceFileCheck.checkGeneFileIsNormal()) {
                System.out.println("sequence file error!!!!");
                AnalyseResult analyseResult = new AnalyseResult(path.split("/")[path.split("/").length - 1]);
                AnalyseDao analyseDao = new AnalyseDaoImpl();
                analyseDao.saveAnalyseResultRes(analyseResult);
                return "";
            }


            dm = defectRecognition.getAnalyseRes(start, end, tv1, tv2);

            if ((dm.get("yc") + ";" + dm.get("ys")).split(";").length > 20) {
                String lack = defectRecognition.getMissGeneSort(dm);
                System.out.println("-----lack-------: " + lack);
                dm.put("lack_gene", lack);
            }

            jsonObject = JSONObject.fromObject(dm);

            Analyse analyse = new Analyse();
            analyse.setFileName(filename);
            analyse.setData9(dm.get("DATA 9"));
            analyse.setData10(dm.get("DATA 10"));
            analyse.setData11(dm.get("DATA 11"));
            analyse.setData12(dm.get("DATA 12"));
            analyse.setDna(dm.get("N_DNA"));
            analyse.setUDna(dm.get("U_DNA"));
            analyse.setYc(dm.get("yc"));
            analyse.setYs(dm.get("ys"));
            analyse.setLocation(dm.get("PLOC 2"));

//            System.out.println("==================================");
//            System.out.println("filename: " + analyse.getFileName());
//            System.out.println("==================================");
//            System.out.println("DATA 9: " + analyse.getData9());
//            System.out.println("==================================");
//            System.out.println("DATA 10: " + analyse.getData10());
//            System.out.println("==================================");
//            System.out.println("DATA 11: " + analyse.getData11());
//            System.out.println("==================================");
//            System.out.println("DATA 12: " + analyse.getData12());
//            System.out.println("==================================");
//            System.out.println("N_DN: A" + analyse.getDna());
//            System.out.println("==================================");
//            System.out.println("U_DNA: " + analyse.getUDna());
//            System.out.println("==================================");
//            System.out.println("yc: " + analyse.getYc());
//            System.out.println("==================================");
//            System.out.println("ys: " + analyse.getYs());
//            System.out.println("==================================");
//            System.out.println("PLOC 2: " + analyse.getLocation());
//            new AnalyseDao().saveAnalyseRes(analyse);


        }
        return jsonObject.toString();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DCollection.class, args);
    }
}