package lk.ijse.dep.pos.dto;

public class PurchaseOrderDTO {
    private int oNo;
    private int orderId;
    private String iCode;
    private Double noOfItem;
    private Double pack;
    private Double purPrice;
    private Double discount;
    private Double margin;


    public PurchaseOrderDTO() {
    }

    public PurchaseOrderDTO(int oNo, int orderId, String iCode, Double noOfItem, Double pack, Double purPrice, Double discount, Double margin) {
        this.oNo = oNo;
        this.orderId = orderId;
        this.iCode = iCode;
        this.noOfItem = noOfItem;
        this.pack = pack;
        this.purPrice = purPrice;
        this.discount = discount;
        this.margin = margin;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getoNo() {
        return oNo;
    }

    public void setoNo(int oNo) {
        this.oNo = oNo;
    }

    public String getiCode() {
        return iCode;
    }

    public void setiCode(String iCode) {
        this.iCode = iCode;
    }

    public Double getNoOfItem() {
        return noOfItem;
    }

    public void setNoOfItem(Double noOfItem) {
        this.noOfItem = noOfItem;
    }

    public Double getPack() {
        return pack;
    }

    public void setPack(Double pack) {
        this.pack = pack;
    }

    public Double getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(Double purPrice) {
        this.purPrice = purPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        return "PurOrderDetail{" +
                "orderId=" + orderId +
                ", oNo=" + oNo +
                ", iCode='" + iCode + '\'' +
                ", noOfItem=" + noOfItem +
                ", pack=" + pack +
                ", purPrice=" + purPrice +
                ", discount=" + discount +
                ", margin=" + margin +
                '}';
    }
}
