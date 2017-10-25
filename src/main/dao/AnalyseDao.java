package main.dao;

import main.model.Analyse;

public interface AnalyseDao {

    void saveAnalyseRes(Analyse analyse);

    void updateAnalyseRes(Analyse analyse);

    Analyse searchAnalyseByFileName(String name);
}
