package main.daoImpl;

import main.dao.AnalyseDao;
import main.model.Analyse;
import main.model.Secret;
import main.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AnalyseDaoImpl implements AnalyseDao {

    public void saveAnalyseRes(Analyse analyse) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(analyse);
        tx.commit();
        session.close();
    }

    public void updateAnalyseRes(Analyse analyse) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx = session.beginTransaction();
        session.update(analyse);
        tx.commit();
        session.close();
    }

    public Analyse searchAnalyseByFileName(String name) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx = session.beginTransaction();
        Analyse analyse = session.get(Analyse.class, name);
        tx.commit();
        session.close();
        return analyse;
    }

    @Override
    public Secret getSecret(String character) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx = session.beginTransaction();
        Secret secret = session.get(Secret.class, character);
        tx.commit();
        session.close();
        return secret;
    }

}
