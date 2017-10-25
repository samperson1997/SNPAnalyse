package main.daoImpl;

import main.dao.AnalyseDao;
import main.util.HibernateSessionFactory;
import main.model.Analyse;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AnalyseDaoImpl implements AnalyseDao{
	
	public void saveAnalyseRes(Analyse analyse){
		Session session= HibernateSessionFactory.getSession();
		Transaction tx=session.beginTransaction();
		session.saveOrUpdate(analyse);
		tx.commit();
		session.close();
	}
	
	public void updateAnalyseRes(Analyse analyse){
		Session session=HibernateSessionFactory.getSession();
		Transaction tx=session.beginTransaction();
		session.update(analyse);
		tx.commit();
		session.close();
	}
	
	public Analyse searchAnalyseByFileName(String name){
		Session session=HibernateSessionFactory.getSession();
		Transaction tx=session.beginTransaction();
		Analyse analys=session.get(Analyse.class, name);
		tx.commit();
		session.close();
		return analys;
	}

}
