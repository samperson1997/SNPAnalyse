package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataDeal {
	
	//将疑似双峰标记成双峰
	public void changeYsToYc(String filename,String ys){
		
		AnalyseDao analyseDao=new AnalyseDao();
		Analyse analyse=analyseDao.searchAnalyseByFileName(filename);
		
		String old_ys=analyse.getYs();
		String old_yc=analyse.getYc();
	
		String[] ys_arr=old_ys.split(";");
		String [] yc_arr=old_yc.split(";");
		
		
		
		ArrayList<Integer> ys_list=new ArrayList<Integer>();
		ArrayList<Integer> yc_list=new ArrayList<Integer>();
		
		for(String tt1:ys_arr){
			ys_list.add(Integer.valueOf(tt1));
		}
		
		for(String tt2:yc_arr){
			yc_list.add(Integer.valueOf(tt2));
		}
		
		String [] new_ys=ys.split("");
		
		for(String temp:new_ys){
			ys_list.remove(Integer.valueOf(temp));
			yc_list.add(Integer.valueOf(temp));
		}
		
		Collections.sort(yc_list);  
		
		String r1="";
		String r2="";
		
		for(int temp1:yc_list){
			r1+=(temp1+";");
		}
		
		for(int temp2:ys_list){
			r2+=(temp2+";");
		}
		
		r1=deleteEnd(r1);
		r2=deleteEnd(r2);
			
		analyse.setYc(r1);
		analyse.setYs(r2);
		
	    analyseDao.updateAnalyseRes(analyse);
		
	}
	
	public String  getMissGeneSort(Map<String, String> dataMap){
		String la=dataMap.get("N_DNA");
		String SFInfo=dataMap.get("sf_info");
        System.out.println(SFInfo);
		String [] locations=la.split("");
		String res=dataMap.get("yc")+";"+dataMap.get("ys");
		String [] temp=res.split(";");
		int [] nums=new int[temp.length];
		
		for(int i=0;i<temp.length;i++){
			nums[i]=Integer.valueOf(temp[i])-1;
		}
		Arrays.sort(nums);
		
		String [] tt=SFInfo.split(";");
		Map<String, String> sfMap=new HashMap<String, String>();
		for(String sft : tt){
			String [] ta=sft.split(":");
			sfMap.put(ta[0], ta[1]);
		}
		
		int start=nums[0];
		//int start_index=0;
		int end=nums[nums.length-1];
		//int end_index=nums.length-1;
		//获得连续双峰的起点
		for(int j=0;j<nums.length-1;j++){
			int n1=nums[j];
			int n2=nums[j+1];
			if( (n2-n1)>4){
				start=n2;
				//start_index=j+1;
			}else{
				break;
			}
		}
		
		//获得连续双峰的结点
		for(int m=nums.length-1;m>0;m--){
			int n3=nums[m];
			int n4=nums[m-1];
			if( (n3-n4)>4){
				end=n3;
				//end_index=m-1;
			}else{
				break;
			}
		}
		System.out.println("连续双峰开始处： "+(start+1));
		System.out.println("连续双峰结束处： "+(end+1));
		String gs="";
		
		for(int h=start-10;h<start;h++){
			gs+=locations[h];
		}
		
		System.out.println("连续双峰开始前10个碱基： "+gs);
		String ck=new GeneDao().searchGeneByType("LPL").getSort();
		ck=ck.toUpperCase();
		int sindex=ck.indexOf(gs);
		if(sindex==-1){
			return "-1;-1";
		}
		int gg=sindex+gs.length();
		System.out.println(gg);
		String cf="";
		for(int i=0;i<20;i++){			
			String ck_s=String.valueOf(ck.charAt(gg+i));
							
			if(res.indexOf(start+i+1+"")==-1){
                cf+=ck_s;
				continue;
			}
			String sf=sfMap.get(start+i+1+"");
			int d=sf.indexOf(ck_s);
			cf+=sf.charAt(1-d);
		}
		int eindex=ck.substring(gg).indexOf(cf);
		System.out.println(cf);
		System.out.println("");
		System.out.println(eindex);
		System.out.println("....................");
		if(ck.indexOf(cf)==-1){
			return sindex+";-1";
		}else{
			return sindex+";"+(eindex-1);
		}
		
	}
	
	
	public void getCKIndex(Analyse analyse){
		String ck=new GeneDao().searchGeneByType("LPL").getSort();
		String dna=analyse.getDna();
		String [] yc=analyse.getYc().split(";");
		for(String temp:yc){
			int index=Integer.valueOf(temp);
			String location=dna.substring(index,index+20);
			int s_index=ck.indexOf(location);
			
		}
		
		
	}
	
	public static String deleteEnd(String s){
		if(s.length()>0){
			s=s.substring(0,s.length()-1);
		}
		return s;
	}

}
