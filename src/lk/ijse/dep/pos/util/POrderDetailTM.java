package lk.ijse.dep.pos.util;

import javafx.scene.control.Button;

public class POrderDetailTM {

    private String code;
    private String description;
    private double qty;
    private double unitPrice;
    private double discount;
    private double total;
    private Button btnDelete;

    public POrderDetailTM() {
    }

    public POrderDetailTM(String code, String description, double qty, double unitPrice,double discount, double total, Button btnDelete) {
        this.setCode(code);
        this.setDescription(description);
        this.setQty(qty);
        this.setUnitPrice(unitPrice);
        this.setDiscount(discount);
        this.setTotal(total);
        this.setBtnDelete(btnDelete);

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "POrderDetailTM{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", discount=" + discount +
                ", total=" + total +
                ", btnDelete=" + btnDelete +
                '}';
    }
}
