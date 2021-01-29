package lk.ijse.dep.pos.dao.custom;

import lk.ijse.dep.pos.dao.SuperDAO;
import lk.ijse.dep.pos.entity.CustomEntity;

import java.sql.Date;
import java.util.List;

public interface QueryDAO extends SuperDAO {


    /**
     *
     * @param query customerId, customerName, orderId, orderDate
     * @return
     * @throws Exception
     */
    List<CustomEntity> getOrdersInfo(String itemcode, Date vfrom, Date vto) throws Exception;

    List<CustomEntity> getOrdersInfo1(String itemcode) throws Exception;

    List<CustomEntity> getOrdersInfo3(Date vfrom, Date vto) throws Exception;

    List<CustomEntity> getOrdersInfo2() throws Exception;

    List<CustomEntity> getOrdersInfo4(String orderId) throws Exception;

    List<CustomEntity> getOrdersInfo5(Date datefrom, Date dateto) throws Exception;


}
