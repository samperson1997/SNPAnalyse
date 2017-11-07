package main.dao;

import main.model.Analyse;

public interface AnalyseDao {

    /**
     * ����������
     * @param analyse
     */
    void saveAnalyseRes(Analyse analyse);

    /**
     * ���·������
     * @param analyse
     */
    void updateAnalyseRes(Analyse analyse);

    /**
     * �����ļ�����ѯ�������
     * @param name
     * @return
     */
    Analyse searchAnalyseByFileName(String name);
}
