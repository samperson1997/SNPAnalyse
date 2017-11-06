package main.dao;

import main.model.Analyse;
import main.model.Secret;

public interface AnalyseDao {

    void saveAnalyseRes(Analyse analyse);

    void updateAnalyseRes(Analyse analyse);

    Analyse searchAnalyseByFileName(String name);

    Secret getSecret(String secret);
}
