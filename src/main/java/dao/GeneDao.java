package main.java.dao;

import main.java.model.Dna;

public interface GeneDao {

    /**
     *  ���ݻ���������������
     * @param type
     * @return
     */
    Dna searchGeneByType(String type);

    /**
     * ���ݻ���Ƭ��ƥ����������
     * @param fragment
     * @return
     */
    Dna matchGeneByFragment(String fragment);
}
