package main.java.serviceImpl;

import main.java.dao.AnalyseDao;
import main.java.dao.GeneDao;
import main.java.daoImpl.AnalyseDaoImpl;
import main.java.daoImpl.GeneDaoImpl;
import main.java.model.AnalyseResult;
import main.java.service.DefectAnalyseService;
import main.java.util.Util;

import java.util.*;

public class DefectAnalyse implements DefectAnalyseService {

    private Map<String, String> dataMap;
    private AnalyseDao analyseDao;
    private static GeneDao geneDao = new GeneDaoImpl();
    private String U_DNA;
    private String[] locations;
    private Util util;

    //TODO 目前只针对LPL
    private final String LPL = geneDao.searchGeneByType("LPL").getSort();
    private final String LPL_CDS = geneDao.searchGeneByType("LPL").getCds();


    public DefectAnalyse(String path, int start, int end, double tv1, double tv2) {
        DefectRecognition defectRecognition = new DefectRecognition(path);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);

        if ((dataMap.get("yc") + ";" + dataMap.get("ys")).split(";").length > 20) {
            String lack = defectRecognition.getMissGeneSort(dataMap);
            System.out.println("-----lack-------: " + lack);
            dataMap.put("lack_gene", lack);
        }

        analyseDao = new AnalyseDaoImpl();
        U_DNA = dataMap.get("U_DNA");
        locations = dataMap.get("N_DNA").split("");
        util = new Util();
    }

    @Override
    public Map<String, AnalyseResult> getAnalyseResult() {
        Map<String, AnalyseResult> analyseResultMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();
        List<String> ycList = new ArrayList<>();
//        List<String> singleList = new ArrayList<>();
//        boolean singleFound = true;
//        if (dataMap.get("single_peak_info").equals("")) {
//            singleFound = false;
//        }
//        checkSinglePeak();

        // ycList 是确认异常的点位
        ycList.addAll(Arrays.asList(dataMap.get("yc").split(";")));


//        singleList.addAll(Arrays.asList(dataMap.get("single_peak_info").split(";")));

//        if (ycList.size() > 20 || singleList.size() > 20) {
//            AnalyseResult analyseResult = new AnalyseResult(1);
//            analyseResultMap.put("", analyseResult);
//        } else {


        if (dataMap.get("lack_gene") == null || dataMap.get("lack_gene").equals("")) {

            for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
                // 查看双峰点位是不是在确认异常的点位
                String sf_info = dataMap.get("sf_info").split(";")[i].split(":")[0];
                if (!ycList.contains(sf_info)) {
                    continue;
                }

                changedList.add(dataMap.get("sf_info").split(";")[i]);
                String[] changedInfo = dataMap.get("sf_info").split(";")[i].split(":");

                AnalyseResult analyseResult = new AnalyseResult();

                if (!changedInfo[0].equals("")) {
                /*
                 * 异常在片段上的位置
                 */
                    int position = Integer.parseInt(changedInfo[0]);
                    analyseResult.setPosition(position);

                /*
                 * 异常在完整DNA片段上的真实位置
                 */
                    int realPosition = getLocations(Integer.parseInt(changedInfo[0]));
                    analyseResult.setRealPosition(realPosition);

                    // -2表示出现N, 一般出现在结果的前10个或后10个碱基
                    // 这个位置由于测序方法的问题, 会导致前后端无法测序准确, 可以直接跳过
                    if (realPosition != -2) {
                        String changedInformation = changedInfo[1].charAt(1) + "=>" + changedInfo[1].charAt(0);
                        startAnalyse(analyseResult, realPosition, changedInformation, position);
                        analyseResultMap.put(changedInfo[0], analyseResult);
                    }
                }
            }

//            if (singleFound) {
//                for (int i = 0; i < singleList.size(); i++) {
//                    AnalyseResult analyseResult = new AnalyseResult();
//                    String items[] = singleList.get(i).split(":");
//                    String position[] = items[0].split("-");
//                    int pos = Integer.parseInt(position[1]);
//                    analyseResult.setPosition(pos);
//                    int realPosition = Integer.parseInt(position[0]);
//                    analyseResult.setRealPosition(realPosition);
//                    if (realPosition != -2) {
//                        startAnalyse(analyseResult, realPosition, items[1], pos);
//                        analyseResultMap.put(pos + "", analyseResult);
//                    }
//                }
//            }
//
//        }
        } else {
            AnalyseResult analyseResult = new AnalyseResult(dataMap.get("lack_gene"));
            analyseResultMap.put("", analyseResult);
        }

        return analyseResultMap;

    }

    private void checkSinglePeak() {
        int leng = locations.length;
        char LPLGene[] = LPL.toCharArray();
        int real_pos = 0;
        int seq_pos = 20;
        String result = "";
        String head = "";

        while (seq_pos + 20 < leng) {
            int l = seq_pos + 20;
            while (seq_pos < l) {
                head += locations[seq_pos];
                seq_pos++;
            }
            real_pos = LPL.indexOf(head);
            if (real_pos >= 0) {
                seq_pos -= 20;
                break;
            }
            head = "";
        }
        if (real_pos < 0) {
            System.out.println("=================NOT FOUND=============");
            return;
        }
        real_pos = real_pos - (seq_pos - 20);
        seq_pos = 20;

        while (seq_pos < leng) {
            if (!locations[seq_pos].equals("" + LPLGene[real_pos])) {
                result += (real_pos + 1) + "-" + (seq_pos + 1) + ":" + LPLGene[real_pos] + "=>" + locations[seq_pos] + ";";
                locations[seq_pos] = LPLGene[real_pos] + "";
            }
            seq_pos++;
            real_pos++;
        }
        if (!result.equals("")) {
            result = util.deleteEnd(result);
        }
        dataMap.put("single_peak_info", result);
    }

    private void startAnalyse(AnalyseResult analyseResult, int realPosition, String changedInformation, int position) {
        /*
         * 异常在完整CDS片段上的真实位置
         * -1表示不在CDS序列上
         */
        int CDSPosition = getCDSPosition(realPosition);
        analyseResult.setCDSPosition(CDSPosition);

        /*
         * 氨基酸变化位置
         */

        analyseResult.setSecretPosition(CDSPosition / 3);

        /*
         * 异常所在DNA片段区域
         */
        String area = (CDSPosition == -1) ? "inner" : "outer";
        analyseResult.setArea(area);

        /*
         * 异常变化信息
         */
        analyseResult.setChangedInfo(changedInformation);

        /*
         * 氨基酸变化信息
         */
        // 非内显子, 继续分析
        if (!area.equals("inner")) {
            String U_secret = ""; // 异常密码子
            String N_secret = ""; // 正常密码子

            // 因为代码计数从0开始，CDS序列计数从1开始，所以必须纠正误差
            int CDSPointer = CDSPosition - 1;

            boolean isWrongResult = false;
            // 余数为0，向前拼接两位碱基构成密码子
            if (CDSPosition % 3 == 0) {
                if (CDSPosition < 2) {
                    isWrongResult = true;
                } else {
                    N_secret = LPL_CDS.substring(CDSPointer - 2, CDSPointer + 1);

                    // 求U_secret
                    int cnt = 1;
                    U_secret = changedInformation.substring(3);
                    for (int j = position - 2; j > 0; j--) {
                        int jRealPosition = realPosition + j - position;

                        if (getCDSPosition(jRealPosition) != -1) {
                            cnt++;
                            U_secret = U_DNA.substring(j, j + 1) + U_secret;
                        }
                        if (cnt == 3) {
                            break;
                        }
                    }
                    System.out.println(position + ": CDSPosition % 3 == 0");
                }
            }
            // 余数为1，向后拼接两位碱基构成密码子
            else if (CDSPosition % 3 == 1) {
                if (CDSPosition < 0) {
                    isWrongResult = true;
                } else {
                    N_secret = LPL_CDS.substring(CDSPointer, CDSPointer + 3);

                    // 求U_secret
                    int cnt = 1;
                    U_secret = changedInformation.substring(3);

                    for (int j = position + 1; j < U_DNA.length(); j++) {
                        int jRealPosition = realPosition + j - position;

                        if (getCDSPosition(jRealPosition) != -1) {
                            cnt++;
                            U_secret += U_DNA.substring(j, j + 1);
                        }
                        if (cnt == 3) {
                            break;
                        }
                    }

                    System.out.println(position + ": CDSPosition % 3 == 1");

                }
            }
            // 余数为2，取一前一后两位碱基构成密码子
            else if (CDSPosition % 3 == 2) {
                if (CDSPosition < 1) {
                    isWrongResult = true;
                } else {
                    N_secret = LPL_CDS.substring(CDSPointer - 1, CDSPointer + 2);

                    // 求U_secret
                    U_secret = changedInformation.substring(3);
                    for (int j = position - 2; j > 0; j--) {
                        int jRealPosition = realPosition + j - position;

                        if (getCDSPosition(jRealPosition) != -1) {
                            U_secret = U_DNA.substring(j, j + 1) + U_secret;
                            break;
                        }
                    }
                    for (int j = position; j < U_DNA.length(); j++) {
                        int jRealPosition = realPosition + j - position;

                        if (getCDSPosition(jRealPosition) != -1) {
                            U_secret += U_DNA.substring(j, j + 1);
                            break;
                        }
                    }

                    System.out.println(position + ": CDSPosition % 3 == 2");
                }
            }

            System.out.println("U:::::::::::" + U_secret);
            System.out.println("N:::::::::::" + LPL_CDS.substring(CDSPointer - 2));

            // 找密码子对应的氨基酸
            if (isWrongResult) {
                analyseResult.setChangedSecret("analyse failed");
            } else {
                if (U_secret.contains("N")) {
                    analyseResult.setChangedSecret("failed nearby");
                } else if (analyseDao.getSecret(U_secret) == null) {
                    analyseResult.setChangedSecret(analyseDao.getSecret(N_secret).getSim_name() + "=>unknown amino acid");
                } else {
                    analyseResult.setChangedSecret(analyseDao.getSecret(N_secret).getSim_name() + "=>"
                            + analyseDao.getSecret(U_secret).getSim_name());
                }
            }
        } else {
            analyseResult.setChangedSecret("-");
        }

        // 打印
        System.out.println(analyseResult.toString());
    }

    /**
     * 获得在CDS序列上的位置
     *
     * @param realPosition 在全长的位置
     * @return 在CDS序列上的位置
     */
    private int getCDSPosition(int realPosition) {
        if (realPosition < 0) {
            return -1;
        }
        int CDSPosition = 0;
        int count = 0;
        char gene[] = LPL.toCharArray();
        char cds[] = LPL_CDS.toCharArray();
        int cursor = 1;
        for (int k = 0; k < realPosition; k++) {
            if (CDSPosition + count >= cds.length) {
                CDSPosition = -1;
                break;
            }
            if (cds[CDSPosition + count] == gene[k]) {
                count++;
                if (k == realPosition - 1) {
                    int u = 1;
                    while (count <= 20) {
                        if (cds[CDSPosition + count] == gene[k + u]) {
                            count++;
                            u++;
                        } else {
                            break;
                        }
                    }
                    if (count < 20) {
                        CDSPosition = -1;
                    } else {
                        CDSPosition += count;
                    }

                }
            } else {
                if (k == realPosition - 1) {
                    CDSPosition = -1;
                } else {
                    if (count > 20) {
                        CDSPosition += count;
                    } else {
                        if (count > 0) {
                            k = cursor;
                            cursor++;
                        }
                    }
                }
                count = 0;
            }
        }

        return CDSPosition;
    }

    /**
     * 获得在全长的位置
     *
     * @param position 在序列片段上的位置
     * @return 在全长的位置
     */
    private int getLocations(int position) {

        String gs = "";

        int start = (position - 20 < 0) ? 0 : (position - 20);
        System.out.println("========================");
        for (int h = start; h < position - 1; h++) {
            gs += locations[h];
            if (locations[h].equals("N")) {
                return -2;
            }
            System.out.print(locations[h]);
        }
        System.out.println("\n========================");

        //匹配标准DNA序列
        String standardDna = new GeneDaoImpl().searchGeneByType("LPL").getSort();
        standardDna = standardDna.toUpperCase();
        //匹配在序列中的位置
        int sindex = standardDna.indexOf(gs);

        if (sindex == -1) { //匹配失败
            return -1;
        } else { //匹配成功
            return sindex + gs.length() + 1;
        }
    }

}

