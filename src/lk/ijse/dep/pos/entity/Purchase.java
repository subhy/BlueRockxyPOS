package lk.ijse.dep.pos.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Purchase implements SuperEntity {

    @Id
    private int pOrderId;
    private Date orderDate;
    private double discount;
    private String pMethod;
    private String pReference;
    private String vendorId;

    public Purchase() {
    }

    public Purchase(int pOrderId, Date orderDate, double discount, String pMethod, String pReference, String vendorId) {
        this.pOrderId = pOrderId;
        this.orderDate = orderDate;
        this.discount = discount;
        this.pMethod = pMethod;
        this.pReference = pReference;
        this.vendorId = vendorId;
    }

    public int getpOrderId() {
        return pOrderId;
    }

    public void setpOrderId(int pOrderId) {
        this.pOrderId = pOrderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getpMethod() {
        return pMethod;
    }

    public void setpMethod(String pMethod) {
        this.pMethod = pMethod;
    }

    public String getpReference() {
        return pReference;
    }

    public void setpReference(String pReference) {
        this.pReference = pReference;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
}
