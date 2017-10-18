package main.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import main.daoImpl.AnalyseDao;
import main.serviceImpl.DataAc;
import main.serviceImpl.DataDeal;
import main.model.Analyse;
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
		JSONObject jsonObject=null;
		int start = 20;
		int end = 10;
		double tv1=0.6;  //ȷ��Ϊ˫�����ֵ
		double tv2=0.5;  //����Ϊ˫�����ֵ
		
		Map<String, String> dm=new HashMap<String, String>();
		
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

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("file");
		int fileSize = (int)mFile.getSize();
		
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
			DataAc dataAc = new DataAc(path);
			dm=dataAc.getAnalyseRes(start, end, tv1, tv2);
			//
			if( (dm.get("yc")+";"+dm.get("ys")).split(";").length>20){
			   String lack=new DataDeal().getMissGeneSort(dm);
			   System.out.println(lack);
			   dm.put("lack_gene", lack);
			}
			//
			jsonObject = JSONObject.fromObject(dm);
			
			Analyse analyse=new Analyse();
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
			new AnalyseDao().saveAnalyseRes(analyse);
			
			
		}
		return jsonObject.toString();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DCollection.class, args);
	}
}