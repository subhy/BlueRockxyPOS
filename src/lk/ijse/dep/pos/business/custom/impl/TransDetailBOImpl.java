package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.TransDetailBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOTypes;
import lk.ijse.dep.pos.dao.custom.TransDetailDAO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.TransDetailDTO;
import lk.ijse.dep.pos.entity.TransDetail;
import org.hibernate.Session;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class TransDetailBOImpl implements TransDetailBO {

    private TransDetailDAO transDetailDAO = DAOFactory.getInstance().getDAO(DAOTypes.TRANSDETAIL);

    @Override
    public void saveTransDetail(TransDetailDTO transDetail) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            transDetailDAO.save(new TransDetail(transDetail.getTransNo(), transDetail.getTransDate(),
                    transDetail.getTransAccNo(), transDetail.getTransCate(), transDetail.getTransSubCate(),
                    transDetail.getTransDesc(), transDetail.getTransType(), transDetail.getTransAmount(), transDetail.getLinkTransNo()));
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateTransDetail(TransDetailDTO transDetail) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            transDetailDAO.update(new TransDetail(transDetail.getTransNo(), transDetail.getTransDate(),
                    transDetail.getTransAccNo(), transDetail.getTransCate(), transDetail.getTransSubCate(),
                    transDetail.getTransDesc(), transDetail.getTransType(), transDetail.getTransAmount(), transDetail.getLinkTransNo()));
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteTransDetail(String transNo) throws Exception {

    }

    @Override
    public List<TransDetailDTO> findAllTransDetail() throws Exception {
        return null;
    }

    @Override
    public List<TransDetailDTO> findTransDetail(String transNo) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            TransDetail transDetail = transDetailDAO.find(transNo);
            List<TransDetailDTO> items = new ArrayList<>();
            items.add(new TransDetailDTO(transDetail.getTransNo(), transDetail.getTransDate(),
                    transDetail.getTransAccNo(), transDetail.getTransCate(), transDetail.getTransSubCate(),
                    transDetail.getTransDesc(), transDetail.getTransType(), transDetail.getTransAmount()));
            session.getTransaction().commit();
            return items;
        }
    }

    @Override
    public Integer getLastOrderId() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            Integer lastOrderId = transDetailDAO.getLastOrderId();
            session.getTransaction().commit();
            return lastOrderId;
        }

    }

    @Override
    public List<TransDetailDTO> findAllTransDetailByDate(String searchText, Date fromDate, Date toDate) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            List<TransDetail> aTBDate = transDetailDAO.findAllTransByDate(searchText, fromDate, toDate);
            List<TransDetailDTO> items = new ArrayList<>();
            for (TransDetail item : aTBDate) {
                items.add(new TransDetailDTO(item.getTransNo(), item.getTransDate(), item.getTransAccNo(), item.getTransCate(),
                        item.getTransSubCate(), item.getTransDesc(), item.getTransType(), item.getTransAmount()));
            }
            session.getTransaction().commit();
            return items;
        }
    }

    @Override
    public List<TransDetailDTO> findAllTransDetailGroup(String selectCate, Date fromDate, Date toDate, String typeE) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            List<TransDetail> aTBDate = transDetailDAO.findAllTransByGroup(selectCate, fromDate, toDate, typeE);
            List<TransDetailDTO> items = new ArrayList<>();
            for (TransDetail item : aTBDate) {
                items.add(new TransDetailDTO(item.getTransNo(), item.getTransDate(),
                        item.getTransAccNo(), item.getTransCate(), item.getTransSubCate(),
                        item.getTransDesc(), item.getTransType(),
                        item.getTransAmount()));
            }
            session.getTransaction().commit();
            return items;
        }
    }

    @Override
    public boolean existsByCateDate(String oilSale, String toDate, String pMethod, String subCate) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            boolean existData = transDetailDAO.existsByCateDate(oilSale, toDate, pMethod, subCate);

            session.getTransaction().commit();
            return existData;
        }
    }

    @Override
    public boolean existsByDisDate(String disGiven, String toDate) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            boolean existDisc = transDetailDAO.existsByDisDate(disGiven, toDate);

            session.getTransaction().commit();
            return existDisc;
        }
    }

    @Override
    public Double getTotalAndDis(String oId, String oilSale, String toDate, String pMethod, String subCate) throws Exception {
        Double transAmount = 0.00;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            transAmount = transDetailDAO.findTotalAndDisc(oId, oilSale, toDate, pMethod, subCate);

            session.getTransaction().commit();
            return transAmount;
        }
    }

    @Override
    public Double getTotal(String sDateFrom, String sDateTo, String pType, String subCate) throws Exception {
        Double transAmount = 0.00;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            transAmount = transDetailDAO.findTotal(sDateFrom, sDateTo, pType, subCate);

            session.getTransaction().commit();
            return transAmount;
        }
    }

    @Override
    public void updateAmount(Double selAmount, String dpDate) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            transDetailDAO.updateAmount(selAmount, dpDate);

            session.getTransaction().commit();

        }
    }

    @Override
    public void upDateVoidSale(String orderId, String dpDate, Double disAmount) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            transDetailDAO.upDateVoidSale(orderId, dpDate, disAmount);

            session.getTransaction().commit();

        }
    }

    @Override
    public String findTransDetailByDateCategory(String accNo, String dpDate, String accDesc) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            String transNo = transDetailDAO.findTransDetailByDateCate(accNo, dpDate, accDesc);
            session.getTransaction().commit();
            return transNo;

        }
    }

    @Override
    public String getTransaNoByOrderId(String orderId) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transDetailDAO.setSession(session);
            session.beginTransaction();
            String transNo = transDetailDAO.getTransNoByOrderId(orderId);
            session.getTransaction().commit();
            if (transNo == null) {
                return "null";
            } else {
                return transNo;
            }
        }
    }
}
