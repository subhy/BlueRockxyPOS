package lk.ijse.dep.pos.business.custom;

import lk.ijse.dep.pos.business.SuperBO;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.dto.PurOrderDetailDTO;
import lk.ijse.dep.pos.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseBO  extends SuperBO {
    void savePurchase(PurchaseDTO purchaseDTO) throws Exception;

   void updatePurchase() throws  Exception;

    void deletePurchase() throws Exception;

    List<PurchaseDTO> findAllPurchase() throws Exception;

    PurchaseDTO findPurchase(String pOrderId) throws Exception;

}
