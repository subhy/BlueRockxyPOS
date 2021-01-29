package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.PurchaseBO;
import lk.ijse.dep.pos.business.custom.PurchaseOrderBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOTypes;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.dao.custom.PurOrderDetailDAO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.PurchaseDTO;
import lk.ijse.dep.pos.dto.PurchaseOrderDTO;
import lk.ijse.dep.pos.entity.Customer;
import lk.ijse.dep.pos.entity.Order;
import lk.ijse.dep.pos.entity.PurOrderDetail;
import org.hibernate.Session;

import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {
    private PurOrderDetailDAO poDetailDAO = DAOFactory.getInstance().getDAO(DAOTypes.PURORDERDETAIL);


    @Override
    public void savePDetail(PurchaseOrderDTO purchaseOrderDTO) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            poDetailDAO.setSession(session);
            session.beginTransaction();
            poDetailDAO.save(new PurOrderDetail(purchaseOrderDTO.getOrderId(),purchaseOrderDTO.getoNo(),purchaseOrderDTO.getiCode(),
                    purchaseOrderDTO.getNoOfItem(),purchaseOrderDTO.getPack(),purchaseOrderDTO.getPurPrice(),
                    purchaseOrderDTO.getDiscount(),purchaseOrderDTO.getMargin()));
            session.getTransaction().commit();

        }
    }

    @Override
    public void updatePDetail() throws Exception {

    }

    @Override
    public void deletePDetail() throws Exception {

    }

    @Override
    public List<PurchaseOrderDTO> findAllPDetail() throws Exception {
        return null;
    }

    @Override
    public PurchaseDTO findPOrder(String pOrderId) throws Exception {
        return null;
    }
}
