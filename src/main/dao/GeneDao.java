package main.dao;

import main.model.Dna;

public interface GeneDao {

    /**
     *  ���ݻ���������������
     * @param type
     * @return
     */
    Dna searchGeneByType(String type);
}
