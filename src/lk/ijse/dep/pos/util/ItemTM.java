package lk.ijse.dep.pos.util;

public class ItemTM implements Cloneable{

    private String code;
    private String description;
    private double qtyOnHand;
    private double unitPrice;
    private double totalValue;

    public ItemTM() {
    }

    public ItemTM(String code, String description, double qtyOnHand, double unitPrice,double totalValue) {
        this.code = code;
        this.description = description;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
        this.totalValue=totalValue;
    }

    public ItemTM clone(){
        return new ItemTM(this.code, this.description, this.qtyOnHand, this.unitPrice,this.totalValue);
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

    public double getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(double qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "ItemTM{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", qtyOnHand=" + qtyOnHand +
                ", unitPrice=" + unitPrice +
                ", totalValue=" + totalValue +
                '}';
    }
}
