package main.java.dao;

import main.java.model.Analyse;
import main.java.model.Secret;

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

    Secret getSecret(String secret);
}
