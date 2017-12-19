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

    private int[] CDS_start = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] CDS_end = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    private String CDS1 = "ATGGAGAGCAAAGCCCTGCTCGTGCTGACTCTGGCCGTGTGGCTCCAGAGTCTGACCGCCTCCCGCGGAGGGGTGGCCGCCGCCGACC";
    private String CDS2 = "AAAGAAGAGATTTTATCGACATCGAAAGTAAATTTGCCCTAAGGACCCCTGAAGACACAGCTGAGGACACTTGCCACCTCATTCCCGGAGTAGCAGAGTCCGTGGCTACCTGTCATTTCAATCACAGCAGCAAAACCTTCATGGTGATCCATGGCTGGACGGTAA";
    private String CDS3 = "CAGGAATGTATGAGAGTTGGGTGCCAAAACTTGTGGCCGCCCTGTACAAGAGAGAACCAGACTCCAATGTCATTGTGGTGGACTGGCTGTCACGGGCTCAGGAGCATTACCCAGTGTCCGCGGGCTACACCAAACTGGTGGGACAGGATGTGGCCCGGTTTATCAACTGGATGGAGG";
    private String CDS4 = "AGGAGTTTAACTACCCTCTGGACAATGTCCATCTCTTGGGATACAGCCTTGGAGCCCATGCTGCTGGCATTGCAGGAAGTCTGACCAATAAGAAAGTCAACAGAATTACTGG";
    private String CDS5 = "CCTCGATCCAGCTGGACCTAACTTTGAGTATGCAGAAGCCCCGAGTCGTCTTTCTCCTGATGATGCAGATTTTGTAGACGTCTTACACACATTCACCAGAGGGTCCCCTGGTCGAAGCATTGGAATCCAGAAACCAGTTGGGCATGTTGACATTTACCCGAATGGAGGTACTTTTCAGCCAGGATGTAACATTGGAGAAGCTATCCGCGTGATTGCAGAGAGAGGACTTGGAG";
    private String CDS6 = "ATGTGGACCAGCTAGTGAAGTGCTCCCACGAGCGCTCCATTCATCTCTTCATCGACTCTCTGTTGAATGAAGAAAATCCAAGTAAGGCCTACAGGTGCAGTTCCAAGGAAGCCTTTGAGAAAGGGCTCTGCTTGAGTTGTAGAAAGAACCGCTGCAACAATCTGGGCTATGAGATCAATAAAGTCAGAGCCAAAAGAAGCAGCAAAATGTACCTGAAGACTCGTTCTCAGATGCCCTACAAAG";
    private String CDS7 = "TCTTCCATTACCAAGTAAAGATTCATTTTTCTGGGACTGAGAGTGAAACCCATACCAATCAGGCCTTTGAGATTTCTCTGTATGGCACCGTGGCCGAGAGTGAGAACATCCCATTCACTCTG";
    private String CDS8 = "CCTGAAGTTTCCACAAATAAGACCTACTCCTTCCTAATTTACACAGAGGTAGATATTGGAGAACTACTCATGTTGAAGCTCAAATGGAAGAGTGATTCATACTTTAGCTGGTCAGACTGGTGGAGCAGTCCCGGCTTCGCCATTCAGAAGATCAGAGTAAAAGCAGGAGAGACTCAGAAAAAG";
    private String CDS9 = "GTGATCTTCTGTTCTAGGGAGAAAGTGTCTCATTTGCAGAAAGGAAAGGCACCTGCGGTATTTGTGAAATGCCATGACAAGTCTCTGAATAAGAAGTCAGGCTG";

    private String[] CDSs = {CDS1, CDS2, CDS3, CDS4, CDS5, CDS6, CDS7, CDS8, CDS9};

    public DefectAnalyse(String path, int start, int end, double tv1, double tv2) {
        DefectRecognition defectRecognition = new DefectRecognition(path);
        dataMap = defectRecognition.getAnalyseRes(start, end, tv1, tv2);
        analyseDao = new AnalyseDaoImpl();
//        Dna stdDna = geneDao.matchGeneByFragment(N_DNA.substring(0, 20));
//        stdGene = stdDna.getSort();
//        stdCDS = stdDna.getCds();
        CDS = new ArrayList<>();

        for (int i = 0; i < CDSs.length; i++) {
            CDS_start[i] = LPL.indexOf(CDSs[i]);
            CDS_end[i] = LPL.indexOf(CDSs[i]) + CDSs[i].length() - 1;
        }
    }

    @Override
    public Map<String, AnalyseResult> getAnalyseResult() {
        Map<String, AnalyseResult> analyseResultMap = new HashMap<>();
        List<String> changedList = new ArrayList<>();

        //TODO 目前只针对LPL, 后面需要首先判断是什么类型的基因, 再找位置
        String n_DNA = dataMap.get("N_DNA");
        int firstPosition = LPL.indexOf(n_DNA);

        for (int i = 0; i < dataMap.get("sf_info").split(";").length; i++) {
            changedList.add(dataMap.get("sf_info").split(";")[i]);
            String[] changedInfo = changedList.get(i).split(":");

            AnalyseResult analyseResult = new AnalyseResult();
            /**
             * 异常在完整DNA片段上的真实位置
             */
            int realPosition = getLocations(Integer.parseInt(changedInfo[0]));
            analyseResult.setRealPosition(realPosition);

            /**
             * 异常在完整CDS片段上的真实位置
             * 先判断在CDS上，再计算异常在完整CDS片段上的真实位置
             */
            int CDSPosition = 0;
            String area = "inner";
            if (isCDS(realPosition)) {
                area = "outer";
                for (int j = 0; j < CDSs.length; j++) {
                    if (CDS_end[j] < realPosition) {
                        CDSPosition += (CDS_end[j] - CDS_start[j]);
                    } else {
                        CDSPosition += (realPosition - CDS_start[j]);
                        break;
                    }
                }
            }
            analyseResult.setCDSPosition(CDSPosition);

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

                // 余数为0，向前拼接两位碱基构成密码子
                if (CDSPosition % 3 == 0) {
                    N_secret = LPL_CDS.substring(CDSPosition - 2, CDSPosition + 1);
                    U_secret = LPL_CDS.substring(CDSPosition - 2, CDSPosition) + changedInformation.substring(3);
                    System.out.println("CDSPosition % 3 == 0");
                }
                // 余数为1，向后拼接两位碱基构成密码子
                else if (CDSPosition % 3 == 1) {
                    N_secret = LPL_CDS.substring(CDSPosition, CDSPosition + 3);
                    U_secret = changedInformation.substring(3) + LPL_CDS.substring(CDSPosition, CDSPosition + 2);
                    System.out.println("CDSPosition % 3 == 1");
                }
                // 余数为2，取一前一后两位碱基构成密码子
                else if (CDSPosition % 3 == 2) {
                    N_secret = LPL_CDS.substring(CDSPosition - 1, CDSPosition + 2);
                    U_secret = LPL_CDS.substring(CDSPosition - 1, CDSPosition) + changedInformation.substring(3)
                            + LPL_CDS.substring(CDSPosition + 1, CDSPosition + 2);
                    System.out.println("CDSPosition % 3 == 2");
                }

                // 找密码子对应的氨基酸
//                analyseResult.setChangedSecret("方法有点问题");
                analyseResult.setChangedSecret(analyseDao.getSecret(N_secret).getChs_name() + "=>"
                        + analyseDao.getSecret(U_secret).getChs_name());
            } else {
                analyseResult.setChangedSecret("-");
            }

            // 打印
            System.out.println(analyseResult.toString());
            analyseResultMap.put(changedInfo[0], analyseResult);
        }

        return analyseResultMap;
    }


    /**
     * @param position 基因真实位置
     * @return 基因是否在CDS序列上
     */
    private boolean isCDS(int position) {
        boolean res = false;
        for (int i = 0; i < CDSs.length; i++) {
            if (CDS_start[i] <= position && position <= CDS_end[i]) {
                res = true;
                break;
            }
        }

        return res;

    }

    /**
     * 找出CDS片段
     */
    private void getCDS() {
        char gene[] = stdGene.toCharArray();
        char cds[] = stdCDS.toCharArray();
        String cdsFragment = "";
        int gStart = 0;
        int cStart = 0;
        int length = 0;
        int geneCursor = 0;
        for (int i = 0; i < cds.length; ) {
            for (int j = geneCursor; i < gene.length; j++) {
                String candidate = "";
                int increment = 0;
                while (cds[i + increment] == gene[j + increment]) {
                    candidate = candidate + gene[j + increment];
                    increment++;
                }
                if (candidate.length() > cdsFragment.length()) {
                    cdsFragment = candidate;
                    length = candidate.length();
                    gStart = j + length;
                    cStart = i + length;
                }
            }
            CDS.add(cdsFragment);
            cdsFragment = "";
            geneCursor = gStart;
            i = cStart;
        }
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

        //匹配成功
        return sindex + gs.length() - 1;
    }
}
