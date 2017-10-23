package main.serviceImpl;

import main.model.Directory;
import main.model.Head;
import main.util.MyFileReader;
import main.util.Util;

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
    private Util util;

    public DataAc(String path) {

        try {
            file = new MyFileReader(path, "rw");
        } catch (FileNotFoundException e) {
            System.out.println("no file");
            e.printStackTrace();
        }

        util = new Util();
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
                short t = file.readShort();
                temp.setElementType(t);
                temp.setElementSize(file.readShort());
                int en = file.readInt();
                temp.setElementNum(en);
                int s = file.readInt();
                temp.setItemSize(s);
                if (s > 4) {
                    temp.setItemOffset(file.readInt());
                } else {
                    if (t == 4) {
                        if (en == 1) {
                            temp.setRs(file.readShort() + "");
                            file.skipBytes(2);
                        } else {
                            temp.setRs(file.readShort() + ";" + file.readShort());
                        }

                    } else if (t == 1) {
                        String r = "";
                        for (int j = 0; j < en; j++) {
                            r += (file.readUnsignedByte() + ";");
                        }
                        r = util.deleteEnd(r);
                        temp.setRs(r);
                        file.skipBytes(4 - en);
                    } else if (t == 18) {
                        String r = "";
                        int nn = file.readUnsignedByte();
                        for (int j = 0; j < nn; j++) {
                            r += (file.readString(1));
                        }
                        r = util.deleteEnd(r);
                        temp.setRs(r);
                        file.skipBytes(3 - nn);
                    } else if (t == 19) {

                        if (en == 2) {
                            temp.setRs(file.readCString(2) + file.readCString(2));
                        } else {
                            temp.setRs(file.readCString(2));
                            file.skipBytes(2);
                        }
                    } else if (t == 10) {
                        temp.setRs(file.readDate());
                    } else if (t == 11) {
                        temp.setRs(file.readTime());
                    } else if (t == 5) {
                        temp.setRs(file.readInt() + "");
                    } else if (t == 2) {
                        String res = "";
                        for (int j = 0; j < en; j++) {
                            res += file.readString(1);
                        }
                        temp.setRs(res);
                        file.skipBytes(4 - en);
                    } else if (t == 7) {
                        temp.setRs(file.readFloat() + "");
                    } else {
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
                                    res.append(file.readUnsignedByte() + ";");
                                }
                                res = new StringBuffer(util.deleteEnd(res.toString()));
                                break;
                            case 2:
                                for (int i = 0; i < e_num; i++) {
                                    res.append(file.readString(1));
                                }
                                break;
                            case 4:
                                for (int i = 0; i < e_num; i++) {
                                    res.append((file.readShort() + ";"));
                                }
                                res = new StringBuffer(util.deleteEnd(res.toString()));
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
                } else {
                    res.append(d.getRs());
                }
                dataMap.put(d.getTagName() + " " + d.getTagNum(), res.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
    }
}
