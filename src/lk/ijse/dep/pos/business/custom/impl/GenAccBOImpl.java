package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.GenAccBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOTypes;
import lk.ijse.dep.pos.dao.custom.GenAccDAO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.GenAccDTO;
import lk.ijse.dep.pos.entity.GenAcc;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GenAccBOImpl implements GenAccBO {
    private GenAccDAO genAccDAO = DAOFactory.getInstance().getDAO(DAOTypes.GENACC);

    @Override
    public void saveAcc(GenAccDTO genAcc) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            genAccDAO.setSession(session);
            session.beginTransaction();
            genAccDAO.save(new GenAcc(genAcc.getAccNo(), genAcc.getAccName(), genAcc.getAccType(), genAcc.getAccCate(),
                    genAcc.getAccSubCate(), genAcc.getAccOpenValue(), genAcc.getAccHolder(), genAcc.getAccDetail(),
                    genAcc.getAccCurValue()));
            session.getTransaction().commit();

        }
    }

    @Override
    public void updateAcc(GenAccDTO genAcc) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            genAccDAO.setSession(session);
            session.beginTransaction();
            genAccDAO.update(new GenAcc(genAcc.getAccNo(), genAcc.getAccName(), genAcc.getAccType(),
                    genAcc.getAccCate(), genAcc.getAccSubCate(), genAcc.getAccOpenValue(),
                    genAcc.getAccHolder(), genAcc.getAccDetail(), genAcc.getAccCurValue()));
            session.getTransaction().commit();

        }
    }

    @Override
    public void deleteAcc(String accNo) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            genAccDAO.setSession(session);
            session.beginTransaction();
            genAccDAO.delete(accNo);
            session.beginTransaction().commit();

        }
    }

    @Override
    public List<GenAccDTO> findAllAcc() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            genAccDAO.setSession(session);
            session.beginTransaction();
            List<GenAcc> allAcc = genAccDAO.findAll();
            List<GenAccDTO> allAccD = new ArrayList<>();
            for (GenAcc acc : allAcc) {
                allAccD.add(new GenAccDTO(acc.getAccNo(), acc.getAccName(), acc.getAccType(),
                        acc.getAccCate(), acc.getAccSubCate(), acc.getAccOpenValue(),
                        acc.getAccHolder(), acc.getAccDetail(), acc.getAccCurValue()));
            }
            session.getTransaction().commit();
            return allAccD;
        }
    }

    @Override
    public GenAccDTO findAcc(String accNo) throws Exception {
        try (Session session=HibernateUtil.getSessionFactory().openSession()){
            genAccDAO.setSession(session);
            session.beginTransaction();
            GenAcc acc = genAccDAO.find(accNo);
            session.getTransaction().commit();
            return new GenAccDTO(acc.getAccNo(), acc.getAccName(), acc.getAccType(),
                    acc.getAccCate(), acc.getAccSubCate(), acc.getAccOpenValue(),
                    acc.getAccHolder(), acc.getAccDetail(), acc.getAccCurValue());

        }
    }

    @Override
    public List<GenAccDTO> findAccByCate(String categoryCh) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            genAccDAO.setSession(session);
            session.beginTransaction();
            List<GenAcc> allAcc = genAccDAO.findAccByCate(categoryCh);
            List<GenAccDTO> allAccD = new ArrayList<>();
            for (GenAcc acc : allAcc) {
                allAccD.add(new GenAccDTO(acc.getAccNo(), acc.getAccName(), acc.getAccType(),
                        acc.getAccCate(), acc.getAccSubCate(), acc.getAccOpenValue(),
                        acc.getAccHolder(), acc.getAccDetail(), acc.getAccCurValue()));
            }
            session.getTransaction().commit();
            return allAccD;
        }
    }


}
