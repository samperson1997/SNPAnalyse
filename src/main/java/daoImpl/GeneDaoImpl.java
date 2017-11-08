package main.java.daoImpl;

import main.java.dao.GeneDao;
import main.java.util.HibernateSessionFactory;
import main.java.model.Dna;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GeneDaoImpl implements GeneDao{
	
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
		Session session= HibernateSessionFactory.getSession();
		Transaction tx=session.beginTransaction();
		Dna dna=session.get(Dna.class,type);
		tx.commit();
		session.close();
		return dna;
	}

}