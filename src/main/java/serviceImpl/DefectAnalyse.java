package main.java.serviceImpl;

import main.java.dao.AnalyseDao;
import main.java.dao.GeneDao;
import main.java.daoImpl.AnalyseDaoImpl;
import main.java.daoImpl.GeneDaoImpl;
import main.java.model.AnalyseResult;
import main.java.service.DefectAnalyseService;

import java.util.*;

public class DefectAnalyse implements DefectAnalyseService {

    private Map<String, String> dataMap;
    private AnalyseDao analyseDao;
    private static GeneDao geneDao = new GeneDaoImpl();
    private String U_DNA;

    //TODO 目前只针对LPL
    private final String LPL = geneDao.searchGeneByType("LPL").getSort();
    private final String LPL_CDS = geneDao.searchGeneByType("LPL").getCds();


    public DefectAnalyse(String path, int start, int end, double tv1, double tv2) {
        DefectRecognition defectRecognition = new DefectRecognition(path);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
        analyseDao = new AnalyseDaoImpl();
        U_DNA = dataMap.get("U_DNA");
    }

    @Override
    public Map<String, AnalyseResult> getAnalyseResult() {
        Map<String, AnalyseResult> analyseResultMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();
        List<String> ycList = new ArrayList<>();
        List<String> singleList = new ArrayList<>();

        // ycList 是确认异常的点位
        ycList.addAll(Arrays.asList(dataMap.get("yc").split(";")));
        singleList.addAll(Arrays.asList(dataMap.get("single_peak_info").split(";")));

        if (ycList.size() > 20 || singleList.size() > 20) {
            AnalyseResult analyseResult = new AnalyseResult(1);
            analyseResultMap.put("", analyseResult);
        } else {
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

            for (int i = 0; i < singleList.size(); i++) {
                AnalyseResult analyseResult = new AnalyseResult();
                String items[] = singleList.get(i).split(":");
                int pos = Integer.parseInt(items[0]);
                analyseResult.setPosition(pos);
                int realPosition = getLocations(pos);
                analyseResult.setRealPosition(realPosition);
                if (realPosition != -2) {
                    startAnalyse(analyseResult, realPosition, items[1], pos);
                    analyseResultMap.put(pos + "", analyseResult);
                }
            }
        }
        return analyseResultMap;
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

            boolean isWrongResult = false;
            // 余数为0，向前拼接两位碱基构成密码子
            if (CDSPosition % 3 == 0) {
                if (CDSPosition < 2) {
                    isWrongResult = true;
                } else {
                    N_secret = LPL_CDS.substring(CDSPosition - 2, CDSPosition + 1);

                    // 求U_secret
                    int cnt = 1;
                    U_secret = changedInformation.substring(3);
                    for (int j = position - 2; j > 0; j--) {
                        if (getCDSPosition(getLocations(j)) != -1) {
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
                    N_secret = LPL_CDS.substring(CDSPosition, CDSPosition + 3);

                    // 求U_secret
                    int cnt = 1;
                    U_secret = changedInformation.substring(3);
                    for (int j = position; j < U_DNA.length(); j++) {
                        if (getCDSPosition(getLocations(j)) != -1) {
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
                    N_secret = LPL_CDS.substring(CDSPosition - 1, CDSPosition + 2);

                    // 求U_secret
                    U_secret = changedInformation.substring(3);
                    for (int j = position - 2; j > 0; j--) {
                        if (getCDSPosition(getLocations(j)) != -1) {
                            U_secret = U_DNA.substring(j, j + 1) + U_secret;
                            break;
                        }
                    }
                    for (int j = position; j < U_DNA.length(); j++) {
                        if (getCDSPosition(getLocations(j)) != -1) {
                            U_secret += U_DNA.substring(j, j + 1);
                            break;
                        }
                    }

                    System.out.println(position + ": CDSPosition % 3 == 2");
                }
            }

            // 找密码子对应的氨基酸
            if (isWrongResult) {
                analyseResult.setChangedSecret("analyse failed");
            } else {
                if (analyseDao.getSecret(U_secret) == null) {
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
            return realPosition;
        }
        int CDSPosition = 0;
        int count = 0;
        char gene[] = LPL.toCharArray();
        char cds[] = LPL_CDS.toCharArray();
        for (int k = 0; k <= realPosition; k++) {
            if (cds[CDSPosition + count] == gene[k]) {
                count++;
                if (k == realPosition) {
                    int u = 0;
                    while (count <= 20) {
                        if (cds[CDSPosition + count] == gene[k + u]) {
                            count++;
                            u++;
                        } else {
                            break;
                        }
                    }
                    if (count < 20) {
                        CDSPosition = 0;
                    } else {
                        CDSPosition += count;
                    }

                }
            } else {
                if (k == realPosition) {
                    CDSPosition = 0;
                }
                if (count > 20) {
                    CDSPosition += count;
                }
                count = 0;
            }
        }

        return CDSPosition - 1;
    }

    /**
     * 获得在全长的位置
     *
     * @param position 在序列片段上的位置
     * @return 在全长的位置
     */
    private int getLocations(int position) {
        String[] locations = dataMap.get("N_DNA").split("");
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

//        System.out.println("========================");
//        System.out.println(sindex);
//        System.out.println("========================");

        if (sindex == -1) { //匹配失败
            return -1;
        } else { //匹配成功
            return sindex + gs.length() + 1;
        }
    }

}
