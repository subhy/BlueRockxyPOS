package lk.ijse.dep.pos.dto;

public class PurOrderDetailDTO {
    private int orNo;
    private int Ono;
    private String itemCode;
    private double noOfItem;
    private double packs;
    private double purPrice;
    private double disc;
    private double marg;

    public PurOrderDetailDTO() {
    }

    public PurOrderDetailDTO(int orNo, int ono, String itemCode, double noOfItem, double packs, double purPrice, double disc, double marg) {
        this.orNo = orNo;
        Ono = ono;
        this.itemCode = itemCode;
        this.noOfItem = noOfItem;
        this.packs = packs;
        this.purPrice = purPrice;
        this.disc = disc;
        this.marg = marg;
    }

    public int getOrNo() {
        return orNo;
    }

    public void setOrNo(int orNo) {
        this.orNo = orNo;
    }

    public int getOno() {
        return Ono;
    }

    public void setOno(int ono) {
        Ono = ono;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getNoOfItem() {
        return noOfItem;
    }

    public void setNoOfItem(double noOfItem) {
        this.noOfItem = noOfItem;
    }

    public double getPacks() {
        return packs;
    }

    public void setPacks(double packs) {
        this.packs = packs;
    }

    public double getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(double purPrice) {
        this.purPrice = purPrice;
    }

    public double getDisc() {
        return disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    public double getMarg() {
        return marg;
    }

    public void setMarg(double marg) {
        this.marg = marg;
    }

    @Override
    public String toString() {
        return "PurOrderDetailDTO{" +
                "orNo=" + orNo +
                ", Ono=" + Ono +
                ", itemCode='" + itemCode + '\'' +
                ", noOfItem=" + noOfItem +
                ", packs=" + packs +
                ", purPrice=" + purPrice +
                ", disc=" + disc +
                ", marg=" + marg +
                '}';
    }
}
