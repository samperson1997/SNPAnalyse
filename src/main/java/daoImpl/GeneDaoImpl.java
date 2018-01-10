package main.java.daoImpl;

import main.java.dao.GeneDao;
import main.java.util.HibernateSessionFactory;
import main.java.model.Dna;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;

import java.util.List;

public class GeneDaoImpl implements GeneDao {

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

    public Dna searchGeneByType(String type) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx = session.beginTransaction();
        Dna dna = session.get(Dna.class, type);
        tx.commit();
        session.close();
        return dna;
    }

    public Dna matchGeneByFragment(String fragment) {
        Session session = HibernateSessionFactory.getSession();
        Query query = session.createQuery("FROM main.java.model.Dna D WHERE D.sort LIKE ?");
        query.setString(0, "%" + fragment + "%");
        List result = query.list();
        Dna dna = new Dna();

        for (int i = 0; i < result.size(); i++) {
            dna = (Dna) result.get(i);
        }
        session.getTransaction().commit();
        return dna;
    }

}
