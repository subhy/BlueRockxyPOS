package lk.ijse.dep.pos.dto;


import java.sql.Date;

public class OrderDTO2 {
    private Date orderDate;
    private String itemCode;
    private String description;
    private double unitPrice;
    private double qty;

    public OrderDTO2() {
    }

    public OrderDTO2(Date orderDate, String itemCode, String description, double qty, double unitPrice) {
        this.orderDate = orderDate;
        this.itemCode = itemCode;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
