package main;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class GeneDao {
	
//	public void saveAnalyseRes(Analyse analyse){
//		Session session=HibernateSessionFactory.getSession();
//		Transaction tx=session.beginTransaction();
//		session.saveOrUpdate(analyse);
//		tx.commit();
//		session.close();
//	}
//	
//	public void updateAnalyseRes(Analyse analyse){
//		Session session=HibernateSessionFactory.getSession();
//		Transaction tx=session.beginTransaction();
//		session.update(analyse);
//		tx.commit();
//		session.close();
//	}
	
	public Dna searchGeneByType(String type){
		Session session=HibernateSessionFactory.getSession();
		Transaction tx=session.beginTransaction();
		Dna dna=session.get(Dna.class,type);
		tx.commit();
		session.close();
		return dna;
	}

}
