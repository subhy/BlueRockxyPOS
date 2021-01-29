package lk.ijse.dep.pos.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`Order`")
public class Order implements SuperEntity{

    @Id
    private int id;
    private Date date;
   @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;
   @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private double disc;

    public Order() {
    }

    public Order(int id, Date date, Customer customer,double disc) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.disc=disc;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void addOrderDetail(OrderDetail orderDetail){
        this.orderDetails.add(orderDetail);
    }

    public double getDisc() {
        return disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", customer=" + customer +
                ", orderDetails=" + orderDetails +
                ", disc=" + disc +
                '}';
    }
}
