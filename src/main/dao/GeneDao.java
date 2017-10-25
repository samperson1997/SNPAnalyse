package main.dao;

import main.model.Dna;

public interface GeneDao {

    Dna searchGeneByType(String type);
}
