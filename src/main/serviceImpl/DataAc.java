package main.serviceImpl;

import main.model.Directory;
import main.model.Head;
import main.util.MyFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class DataAc {

	private MyFileReader file;
	private Head head;
	private Directory[] directories;
	private Map<String, String> dataMap;


	public DataAc(String path) {

		try {
			file = new MyFileReader(path, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("no file");
			e.printStackTrace();
		}
	}

	/*读取文件头信息*/
	public void getHead() {
		head = new Head();

		try {
			file.seek(0);
			head.setFileName(file.readString(4));
			head.setVersion(file.readShort());
			head.setTagName(file.readString(4));
			head.setTagNum(file.readInt());
			head.setElementType(file.readShort());
			head.setElementSize(file.readShort());
			head.setElementNum(file.readInt());
			head.setDataSize(file.readInt());
			head.setDataOffset(file.readInt());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Directory[] getDirectories() {
		getHead();
		/*目录开始位置*/
		int offset = head.getDataOffset();
		/*目录数量*/
		int e_num = head.getElementNum();
		Directory temp;
		directories = new Directory[e_num];
		try {
			file.seek(offset);
			for (int i = 0; i < e_num; i++) {
				temp = new Directory();
				temp.setTagName(file.readString(4));
				temp.setTagNum(file.readInt());
				short t=file.readShort();
				temp.setElementType(t);
				temp.setElementSize(file.readShort());
				int en=file.readInt();
				temp.setElementNum(en);
				int s=file.readInt();
				temp.setItemSize(s);
				if(s>4){
					temp.setItemOffset(file.readInt());
				}else{
					if(t==4){
						if(en==1){
							temp.setRs(file.readShort()+"");
							file.skipBytes(2);
						}else{
							temp.setRs(file.readShort()+";"+file.readShort());
						}
						
					}
					
					else if (t==1) {
						String r="";
						for(int j=0;j<en;j++){
							r+=(file.readUnsignedByte()+";");
						}
						r=deleteEnd(r);
						temp.setRs(r);
						file.skipBytes(4-en);
					}
					
					else if(t==18){
						String r="";
						int nn=file.readUnsignedByte();
						for(int j=0;j<nn;j++){
							r+=(file.readString(1));	
						}
						r=deleteEnd(r);
						temp.setRs(r);
						file.skipBytes(3-nn);
					}
					
					else if(t==19){
						
						if(en==2){
							temp.setRs(file.readCString(2)+file.readCString(2));
						}else{
							temp.setRs(file.readCString(2));
							file.skipBytes(2);
						}
					}
					
					else if(t==10){
                        temp.setRs(file.readDate());
					}
					
					else if(t==11){
						temp.setRs(file.readTime());
				    }
					
					else if(t==5){
						temp.setRs(file.readInt()+"");
				    }
					
					else if(t==2){
						String res="";
						for (int j = 0; j < en; j++) {
							res += file.readString(1);
						}
						temp.setRs(res);
						file.skipBytes(4-en);
					}
					
					else if(t==7){
						temp.setRs(file.readFloat()+"");
					}
					
					else{
					file.skipBytes(4);
					}
				}

				file.skipBytes(4);
				directories[i] = temp;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return directories;
	}


	public Map<String, String> getAllData() {
		getDirectories();
		try {/*使用红黑树*/
			dataMap = new TreeMap<String, String>();
			for (Directory d : directories) {
				int e_num = d.getElementNum();
				int i_size = d.getItemSize();
				short type = d.getElementType();
				StringBuffer res = new StringBuffer();

				if (i_size > 4) {
					file.seek(d.getItemOffset());
					if (type < 1024) {
						switch (type) {
						case 1:
							for (int i = 0; i < e_num; i++) {	
								res.append(file.readUnsignedByte()+";");
							}
							res=new StringBuffer(deleteEnd(res.toString()));
							break;
						case 2:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readString(1));
							}
							break;
						case 4:
							for (int i = 0; i < e_num; i++) {
								res.append((file.readShort()+";"));
							}
							res=new StringBuffer(deleteEnd(res.toString()));
							break;
						case 5:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readInt());
							}
							break;
						case 7:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readFloat());
							}
							break;
						case 8:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readDouble());
							}
							break;
						case 10:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readDate());
							}
							break;
						case 11:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readTime());
							}
							break;
						case 18:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readString(1));
							}
							break;
						case 19:
							for (int i = 0; i < e_num; i++) {
								res.append(file.readCString(2));
							}
							break;
						}
					}
				}else{
					res.append(d.getRs());
				}
				dataMap.put(d.getTagName() +" "+ d.getTagNum(), res.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	
	public Map<String, String> getAnalyseRes(int start,int end,double tv1,double tv2){
		getAllData();
		String r1="";
		String r2="";
		String [] DNA=dataMap.get("PBAS 2").split("");
		String [] location=dataMap.get("PLOC 2").split(";");
		String [] data9=dataMap.get("DATA 9").split(";");
		String [] data10=dataMap.get("DATA 10").split(";");
		String [] data11=dataMap.get("DATA 11").split(";");
		String [] data12=dataMap.get("DATA 12").split(";");
		
		String N_DNA="";
		String SFInfo="";  
		for(int n=0;n<DNA.length;n++){
        	N_DNA+=DNA[n];
        }
	
		Map<String,Integer> data=null;
		
		ArrayList<Integer> yc=new ArrayList<Integer>();
		ArrayList<Integer> ys=new ArrayList<Integer>();
		int round=data9.length/location.length/2;
		
		Map<String, String> channelMap=new HashMap<String, String>();
		int s=start;
		while(channelMap.size()<4){
			Map<String, Integer> type=new HashMap<String, Integer>(4);
			type.put("data9", Integer.valueOf(data9[Integer.valueOf(location[s])]));
			type.put("data10", Integer.valueOf(data10[Integer.valueOf(location[s])]));
			type.put("data11", Integer.valueOf(data11[Integer.valueOf(location[s])]));
			type.put("data12", Integer.valueOf(data12[Integer.valueOf(location[s])]));
			Map<String, Integer> temp=sortMap(type);
			Iterator<Entry<String, Integer>> it = temp.entrySet().iterator();
			String ts=it.next().getKey();
			if(channelMap.get(ts)==null){
				channelMap.put(ts, DNA[s]);
			}
			s++;
		}
		
		
		
		for(int i=start;i<location.length-end;i++){
			data=new HashMap<String, Integer>();
			
			data.put(channelMap.get("data9"), getMaxOfRange(data9, location[i],round));
			data.put(channelMap.get("data10"), getMaxOfRange(data10, location[i],round));
			data.put(channelMap.get("data11"), getMaxOfRange(data11, location[i],round));
			data.put(channelMap.get("data12"), getMaxOfRange(data12, location[i],round));
			
			Map<String, Integer> sortedMap=sortMap(data);
			
			Iterator<Entry<String, Integer>> it = sortedMap.entrySet().iterator();  
			
			Entry<String, Integer> e1=it.next();
			Entry<String, Integer> e2=it.next();
			
			float n1=e1.getValue();
			float n2=e2.getValue();
			
			
			if(n1!=0 && n2!=0){

                if(n2/n1>tv1){
                    yc.add(i);
                    DNA[i]=e2.getKey();
                    SFInfo+=(i+1+":"+e1.getKey()+e2.getKey()+";");
                    r1+=(i+1+";");
                }

                else if(n2/n1<tv1 && n2/n1>tv2){
                    ys.add(i);
                    SFInfo+=(i+1+":"+e1.getKey()+e2.getKey()+";");
                    r2+=(i+1+";");
                }
			}
			
		}

		String U_DNA="";
		
		
		for(int n=0;n<DNA.length;n++){
        	U_DNA+=DNA[n];
        }
		
		
		r1=deleteEnd(r1);
		r2=deleteEnd(r2);
		SFInfo=deleteEnd(SFInfo);
		
		if(r2.length()>0){
			r2=r2.substring(0,r2.length()-1);
		}
		
		System.out.println("============yc============");
		System.out.println(r1);
		System.out.println("============ys============");
		System.out.println(r2);
		
		dataMap.put("yc", r1);
		dataMap.put("ys", r2);
		dataMap.put("U_DNA", U_DNA);
		dataMap.put("N_DNA", N_DNA);
		dataMap.put("sf_info",SFInfo );

		return dataMap;
	}

	public static int getMaxOfRange(String [] data ,String location ,int round){
		Map< Integer, Integer> map=new HashMap<Integer, Integer>();
		int start=Integer.valueOf(location)-round;
		int end=Integer.valueOf(location)+round;
		for(int n=start;n<=end;n++){
			map.put(n, Integer.valueOf(data[n]));
		}
		
		Map< Integer, Integer> sortedMap=sortMap(map);
		
		Iterator<Entry<Integer, Integer>> it = sortedMap.entrySet().iterator();  
		
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next(); 
			int tk=entry.getKey();
			if(tk>start && tk<end && map.get(tk)>=map.get(tk-1) && map.get(tk)>=map.get(tk+1) ){
				return entry.getValue();
			}
		}
		
		return Integer.valueOf(data[(start+end)/2]);
		
	}
	
	public static <T> Map<T,Integer> sortMap(Map<T,Integer> oldMap) {  
        ArrayList<Map.Entry<T, Integer>> list = new ArrayList<Map.Entry<T, Integer>>(oldMap.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<T, Integer>>() {  
  
            @Override  
            public int compare(Entry<T, Integer> arg0,  Entry<T, Integer> arg1) {  
                return arg1.getValue() - arg0.getValue();  
            }  
        });  
        Map<T, Integer> newMap = new LinkedHashMap<T, Integer>();  
        for (int i = 0; i < list.size(); i++) {  
            newMap.put(list.get(i).getKey(), list.get(i).getValue());  
        }  
        return newMap;  
    }  
	
	public static String deleteEnd(String s){
		if(s.length()>0){
			s=s.substring(0,s.length()-1);
		}
		return s;
	}


}
