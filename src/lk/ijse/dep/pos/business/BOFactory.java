package lk.ijse.dep.pos.business;

import lk.ijse.dep.pos.business.custom.impl.*;
import lk.ijse.dep.pos.entity.PurOrderDetail;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getInstance(){
        return (boFactory == null)? (boFactory = new BOFactory()): boFactory;
    }

    public <T extends SuperBO> T getBO(BOTypes boTypes){
        switch (boTypes){
            case CUSTOMER:
                return (T) new CustomerBOImpl();
            case ITEM:
                return (T) new ItemBOImpl();
            case ORDER:
                return (T) new OrderBOImpl();
            case TRANSDETAIL:
                return (T) new TransDetailBOImpl();
                case GENACC:
                return (T) new GenAccBOImpl();
            case PURCHASE:
                return (T) new PurchaseBOImpl();
            case PURCHASEORDER:
                return (T) new PurchaseOrderBOImpl();

            default:
                return null;
        }
    }

}
