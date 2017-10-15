package main;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class AnalyseDao {
	
	public void saveAnalyseRes(Analyse analyse){
		Session session=HibernateSessionFactory.getSession();
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
