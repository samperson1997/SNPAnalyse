package main.java.dao;

import main.java.model.Dna;

public interface GeneDao {

    /**
     *  ���ݻ���������������
     * @param type
     * @return
     */
    Dna searchGeneByType(String type);
}
