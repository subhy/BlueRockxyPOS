package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.PurchaseBO;
import lk.ijse.dep.pos.dao.DAOFactory;
import lk.ijse.dep.pos.dao.DAOTypes;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dao.custom.PurOrderDetailDAO;
import lk.ijse.dep.pos.dao.custom.PurchaseDAO;
import lk.ijse.dep.pos.db.HibernateUtil;
import lk.ijse.dep.pos.dto.PurOrderDetailDTO;
import lk.ijse.dep.pos.dto.PurchaseDTO;
import lk.ijse.dep.pos.entity.Item;
import lk.ijse.dep.pos.entity.PurOrderDetail;
import lk.ijse.dep.pos.entity.Purchase;
import org.hibernate.Session;

import java.util.List;

public class PurchaseBOImpl implements PurchaseBO {
    private PurchaseDAO purchaseDAO= DAOFactory.getInstance().getDAO(DAOTypes.PURCHASE);
    private ItemDAO itemDAO=DAOFactory.getInstance().getDAO(DAOTypes.ITEM);
    @Override
    public void savePurchase(PurchaseDTO purchase) throws Exception {
   try (Session session= HibernateUtil.getSessionFactory().openSession()){
       purchaseDAO.setSession(session);
       session.beginTransaction();
       purchaseDAO.save(new Purchase(purchase.getpOrderId(),purchase.getOrderDate(),
               purchase.getDiscount(),purchase.getpMethod(),purchase.getpReference(),purchase.getVendorId()));
       session.getTransaction().commit();
   }
    }

    @Override
    public void updatePurchase() throws Exception {

    }

    @Override
    public void deletePurchase() throws Exception {

    }

    @Override
    public List<PurchaseDTO> findAllPurchase() throws Exception {
        return null;
    }

    @Override
    public PurchaseDTO findPurchase(String pOrderId) throws Exception {
        return null;
    }
}
