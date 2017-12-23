package main.java.dao;

import main.java.model.Analyse;
import main.java.model.AnalyseResult;
import main.java.model.Secret;

public interface AnalyseDao {

    /**
     * 保存识别结果
     * @param analyse
     */
    void saveAnalyseRes(Analyse analyse);

    /**
     * 保存分析结果
     * @param analyseResult
     */
    void saveAnalyseResultRes(AnalyseResult analyseResult);

    /**
     * 更新分析结果
     * @param analyse
     */
    void updateAnalyseRes(Analyse analyse);

    /**
     * 根据文件名查询分析结果
     * @param name
     * @return
     */
    Analyse searchAnalyseByFileName(String name);

    Secret getSecret(String secret);
}
