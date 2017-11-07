package main.dao;

import main.model.Analyse;

public interface AnalyseDao {

    /**
     * 保存分析结果
     * @param analyse
     */
    void saveAnalyseRes(Analyse analyse);

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
}
