package main.java.serviceImpl;

import main.java.model.Directory;
import main.java.model.Head;
import main.java.util.MyFileReader;
import main.java.util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class DataAc {

    private MyFileReader file;
    private Head head;
    private Directory[] directories;
    private Map<String, String> dataMap;
    private Util util;

    public DataAc(String path) {
        int[] res = {0,0};
        try {
            file = new MyFileReader(path, "rw");
            System.out.println(file);
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

//            System.out.println("==================================");
//            System.out.println("filaName: " + head.getFileName());
//            System.out.println("==================================");
//            System.out.println("version: " + head.getVersion());
//            System.out.println("==================================");
//            System.out.println("tagName: " + head.getTagName());
//            System.out.println("==================================");
//            System.out.println("tagNum: " + head.getTagName());
//            System.out.println("==================================");
//            System.out.println("elementType: " + head.getElementType());
//            System.out.println("==================================");
//            System.out.println("elementSize: " + head.getElementSize());
//            System.out.println("==================================");
//            System.out.println("elementNum: " + head.getElementNum());
//            System.out.println("==================================");
//            System.out.println("dataSIze: " + head.getDataSize());
//            System.out.println("==================================");
//            System.out.println("dataOffset: " + head.getDataOffset());
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
        Directory directory; //目录 原变量名为temp
        directories = new Directory[e_num];
        try {
            file.seek(offset);
            for (int i = 0; i < e_num; i++) {
                directory = new Directory();
                directory.setTagName(file.readString(4));
                directory.setTagNum(file.readInt());
                short elementType = file.readShort(); //指向文件目录种类 原变量名为t
                directory.setElementType(elementType);
                directory.setElementSize(file.readShort());
                int elementNum = file.readInt(); //指向文件目录大小 原变量名为en
                directory.setElementNum(elementNum);
                int itemSize = file.readInt(); //指向文件目录数量 原变量名为s
                directory.setItemSize(itemSize);
                if (itemSize > 4) {
                    directory.setItemOffset(file.readInt());
                } else {
                    if (elementType == 4) {
                        if (elementNum == 1) {
                            directory.setRs(file.readShort() + "");
                            file.skipBytes(2);
                        } else {
                            directory.setRs(file.readShort() + ";" + file.readShort());
                        }

                    } else if (elementType == 1) {
                        String r = "";
                        for (int j = 0; j < elementNum; j++) {
                            r += (file.readUnsignedByte() + ";");
                        }
                        r = util.deleteEnd(r);
                        directory.setRs(r);
                        file.skipBytes(4 - elementNum);
                    } else if (elementType == 18) {
                        String r = "";
                        int nn = file.readUnsignedByte();
                        for (int j = 0; j < nn; j++) {
                            r += (file.readString(1));
                        }
                        r = util.deleteEnd(r);
                        directory.setRs(r);
                        file.skipBytes(3 - nn);
                    } else if (elementType == 19) {
                        if (elementNum == 2) {
                            directory.setRs(file.readCString(2) + file.readCString(2));
                        } else {
                            directory.setRs(file.readCString(2));
                            file.skipBytes(2);
                        }
                    } else if (elementType == 10) {
                        directory.setRs(file.readDate());
                    } else if (elementType == 11) {
                        directory.setRs(file.readTime());
                    } else if (elementType == 5) {
                        directory.setRs(file.readInt() + "");
                    } else if (elementType == 2) {
                        String res = "";
                        for (int j = 0; j < elementNum; j++) {
                            res += file.readString(1);
                        }
                        directory.setRs(res);
                        file.skipBytes(4 - elementNum);
                    } else if (elementType == 7) {
                        directory.setRs(file.readFloat() + "");
                    } else {
                        file.skipBytes(4);
                    }
                }

                file.skipBytes(4);
                directories[i] = directory;
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
