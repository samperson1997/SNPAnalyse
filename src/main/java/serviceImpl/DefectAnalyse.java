package main.java.serviceImpl;

import main.java.dao.AnalyseDao;
import main.java.dao.GeneDao;
import main.java.daoImpl.AnalyseDaoImpl;
import main.java.daoImpl.GeneDaoImpl;
import main.java.model.AnalyseResult;
import main.java.service.DefectAnalyseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefectAnalyse implements DefectAnalyseService {

    private Map<String, String> dataMap;
    private AnalyseDao analyseDao;
    private static GeneDao geneDao = new GeneDaoImpl();
    private String stdGene;
    private String stdCDS;
    private ArrayList<String> CDS;

    //TODO 目前只针对LPL
    private final String LPL = geneDao.searchGeneByType("LPL").getSort();
    private final String LPL_CDS = geneDao.searchGeneByType("LPL").getCds();


    public DefectAnalyse(String path, int start, int end, double tv1, double tv2) {
        DefectRecognition defectRecognition = new DefectRecognition(path);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
        analyseDao = new AnalyseDaoImpl();
//        Dna stdDna = geneDao.matchGeneByFragment(N_DNA.substring(0, 20));
//        stdGene = stdDna.getSort();
//        stdCDS = stdDna.getCds();
        CDS = new ArrayList<>();

    }

    @Override
    public Map<String, AnalyseResult> getAnalyseResult() {
        Map<String, AnalyseResult> analyseResultMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();

        //TODO 目前只针对LPL, 后面需要首先判断是什么类型的基因, 再找位置
        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");

            AnalyseResult analyseResult = new AnalyseResult();

            if (!changedInfo[0].equals("")) {
                /**
                 * 异常在片段上的位置
                 */
                analyseResult.setPosition(Integer.parseInt(changedInfo[0]));

                /**
                 * 异常在完整DNA片段上的真实位置
                 */
                int realPosition = getLocations(Integer.parseInt(changedInfo[0]));
                analyseResult.setRealPosition(realPosition);

                /**
                 * 异常在完整CDS片段上的真实位置
                 */
                int CDSPosition = 0;
                String area = "inner";
                int count = 0;
                char gene[] = LPL.toCharArray();
                char cds[] = LPL_CDS.toCharArray();
                String cd = "";
                for (int k = 0; k <= realPosition; k++) {
                    if (cds[CDSPosition + count] == gene[k]) {
                        count++;
                        cd += gene[k];
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
                                area = "outer";
                                CDSPosition += count;
                                System.out.println(cd);
                            }

                        }
                    } else {
                        if (k == realPosition) {
                            CDSPosition = 0;
                        }
                        if (count > 20) {
                            CDSPosition += count;
                            System.out.println(cd);
                        }
                        count = 0;
                        cd = "";
                    }
                }
                System.out.println(area + " " + CDSPosition);


                analyseResult.setCDSPosition(CDSPosition);
                analyseResult.setSecretPosition(CDSPosition / 3 + 1);

                /**
                 * 异常所在DNA片段区域
                 */
                analyseResult.setArea(area);

                /**
                 * 异常变化信息
                 */
                String changedInformation = changedInfo[1].charAt(0) + "=>" + changedInfo[1].charAt(1);
                analyseResult.setChangedInfo(changedInformation);

                /**
                 * 氨基酸变化信息
                 */
                // 非内显子, 继续分析
                if (!area.equals("inner")) {
                    String U_secret = ""; // 异常密码子
                    String N_secret = ""; // 正常密码子

                    boolean isWrongResult = false;
                    // 余数为0，向前拼接两位碱基构成密码子
                    if (CDSPosition % 3 == 2) {
                        if (CDSPosition < 2) {
                            isWrongResult = true;
                        } else {
                            N_secret = LPL_CDS.substring(CDSPosition - 2, CDSPosition + 1);
                            U_secret = LPL_CDS.substring(CDSPosition - 2, CDSPosition) + changedInformation.substring(3);
                            //System.out.println("CDSPosition % 3 == 0");
                        }
                    }
                    // 余数为1，向后拼接两位碱基构成密码子
                    else if (CDSPosition % 3 == 0) {
                        if (CDSPosition < 0) {
                            isWrongResult = true;
                        } else {
                            N_secret = LPL_CDS.substring(CDSPosition, CDSPosition + 3);
                            U_secret = changedInformation.substring(3) + LPL_CDS.substring(CDSPosition + 1, CDSPosition + 3);
                            //System.out.println("CDSPosition % 3 == 1");
                        }
                    }
                    // 余数为2，取一前一后两位碱基构成密码子
                    else if (CDSPosition % 3 == 1) {
                        if (CDSPosition < 1) {
                            isWrongResult = true;
                        } else {
                            N_secret = LPL_CDS.substring(CDSPosition - 1, CDSPosition + 2);
                            U_secret = LPL_CDS.substring(CDSPosition - 1, CDSPosition) + changedInformation.substring(3)
                                    + LPL_CDS.substring(CDSPosition + 1, CDSPosition + 2);
                            //System.out.println("CDSPosition % 3 == 2");
                        }
                    }

                    // 找密码子对应的氨基酸
                    if (isWrongResult) {
                        analyseResult.setChangedSecret("wrong analyse result");
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
                analyseResultMap.put(changedInfo[0], analyseResult);
            }
        }

        return analyseResultMap;
    }


    /**
     * 获得在全长中的位置
     */
    private int getLocations(int start) {
        String[] locations = dataMap.get("N_DNA").split("");
        String gs = "";

        for (int h = start - 20; h < start; h++) {
            gs += locations[h];
        }
        //匹配标准DNA序列
        String standardDna = new GeneDaoImpl().searchGeneByType("LPL").getSort();
        standardDna = standardDna.toUpperCase();
        //匹配在序列中的位置
        int sindex = standardDna.indexOf(gs);

        if (sindex == -1) { //匹配失败
            return -1;
        } else { //匹配成功
            return sindex + gs.length() - 1;
        }
    }
}
