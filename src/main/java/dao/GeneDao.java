package main.java.dao;

import main.java.model.Dna;

public interface GeneDao {

    /**
     *  根据基因种类搜索基因
     * @param type
     * @return
     */
    Dna searchGeneByType(String type);
}
