package lk.ijse.dep.pos.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class TransDetail implements SuperEntity {

    @Id
    private String transNo;
    private Date transDate;
    private String transAccNo;
    private String transCate;
    private String transSubCate;
    private String transDesc;
    private String transType;
    private Double transAmount;
    private String linkTransNo;

    public TransDetail() {
    }

    public TransDetail(String transNo, Date transDate, String transAccNo, String transCate, String transSubCate, String transDesc, String transType, Double transAmount) {
        this.transNo = transNo;
        this.transDate = transDate;
        this.transAccNo = transAccNo;
        this.transCate = transCate;
        this.transSubCate = transSubCate;
        this.transDesc = transDesc;
        this.transType = transType;
        this.transAmount = transAmount;
    }

 public TransDetail(String transNo, Date transDate, String transAccNo, String transCate, String transSubCate, String transDesc, String transType, Double transAmount,String linkTransNo) {
        this.transNo = transNo;
        this.transDate = transDate;
        this.transAccNo = transAccNo;
        this.transCate = transCate;
        this.transSubCate = transSubCate;
        this.transDesc = transDesc;
        this.transType = transType;
        this.transAmount = transAmount;
        this.linkTransNo=linkTransNo;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getTransAccNo() {
        return transAccNo;
    }

    public void setTransAccNo(String transAccNo) {
        this.transAccNo = transAccNo;
    }

    public String getTransCate() {
        return transCate;
    }

    public void setTransCate(String transCate) {
        this.transCate = transCate;
    }

    public String getTransSubCate() {
        return transSubCate;
    }

    public void setTransSubCate(String transSubCate) {
        this.transSubCate = transSubCate;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public String getlinkTransNo() {
        return linkTransNo;
    }

    public void setlinkTransNo(String linkTransNo) {
        this.linkTransNo = linkTransNo;
    }

    @Override
    public String toString() {
        return "TransDetail{" +
                "transNo='" + transNo + '\'' +
                ", transDate=" + transDate +
                ", transAccNo='" + transAccNo + '\'' +
                ", transCate='" + transCate + '\'' +
                ", transSubCate='" + transSubCate + '\'' +
                ", transDesc='" + transDesc + '\'' +
                ", transType='" + transType + '\'' +
                ", transAmount=" + transAmount +
                ", linkTransNo='" + linkTransNo + '\'' +
                '}';
    }
}
