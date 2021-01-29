package lk.ijse.dep.pos.dto;

import java.sql.Date;
import java.util.List;

public class OrderDTO {

    private int id;
    private Date date;
    private String customerId;
    private List<OrderDetailDTO> orderDetails;
    private double disc;

    public OrderDTO() {
    }

    public OrderDTO(int id, Date date, String customerId, List<OrderDetailDTO> orderDetails) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.orderDetails = orderDetails;
    }
    public OrderDTO(int id, Date date, String customerId,double disc) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.disc=disc;
    }

    public OrderDTO(int id, Date date, String customerId, List<OrderDetailDTO> orderDetails, double disc) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.orderDetails = orderDetails;
        this.disc = disc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public double getDisc() {
        return disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", date=" + date +
                ", customerId='" + customerId + '\'' +
                ", orderDetails=" + orderDetails +
                ", disc=" + disc +
                '}';
    }
}
