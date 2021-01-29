package lk.ijse.dep.pos.business.custom;

import lk.ijse.dep.pos.business.SuperBO;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.dto.OrderDTO;
import lk.ijse.dep.pos.dto.OrderDTO2;
import lk.ijse.dep.pos.entity.OrderDetailPK;

import java.sql.Date;
import java.util.List;

public interface OrderBO extends SuperBO {

    int getLastOrderId() throws Exception;

    void placeOrder(OrderDTO orderDTO) throws Exception;

    void update(OrderDTO orderDTO) throws Exception;

    List<OrderDTO2> getOrderInfo(String itemcode, Date vfrom, Date vto) throws Exception;

    List<OrderDTO2> getOrderInfo1(String itemcode) throws Exception;

    List<OrderDTO2> getOrderInfo3(Date vfrom, Date vto) throws Exception;

    List<OrderDTO2> getOrderInfo4(String orderId) throws Exception;

    void deleteOrderDetail(OrderDetailPK orderPK) throws Exception;

    Double getDisDetail(int orderId) throws Exception;

    void updateDis(int orderId,double disc) throws Exception;
}

