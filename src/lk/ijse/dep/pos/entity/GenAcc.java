package lk.ijse.dep.pos.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GenAcc implements SuperEntity {

    @Id
    private String accNo;
    private String accName;
    private String accType;
    private String accCate;
    private String accSubCate;
    private double accOpenValue;
    private String accHolder;
    private String accDetail;
    private double accCurValue;

    public GenAcc() {
    }

    public GenAcc(String accNo, String accName, String accType, String accCate, String accSubCate, double accOpenValue, String accHolder, String accDetail, double accCurValue) {
        this.accNo = accNo;
        this.accName = accName;
        this.accType = accType;
        this.accCate = accCate;
        this.accSubCate = accSubCate;
        this.accOpenValue = accOpenValue;
        this.accHolder = accHolder;
        this.accDetail = accDetail;
        this.accCurValue = accCurValue;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccCate() {
        return accCate;
    }

    public void setAccCate(String accCate) {
        this.accCate = accCate;
    }

    public String getAccSubCate() {
        return accSubCate;
    }

    public void setAccSubCate(String accSubCate) {
        this.accSubCate = accSubCate;
    }

    public double getAccOpenValue() {
        return accOpenValue;
    }

    public void setAccOpenValue(double accOpenValue) {
        this.accOpenValue = accOpenValue;
    }

    public String getAccHolder() {
        return accHolder;
    }

    public void setAccHolder(String accHolder) {
        this.accHolder = accHolder;
    }

    public String getAccDetail() {
        return accDetail;
    }

    public void setAccDetail(String accDetail) {
        this.accDetail = accDetail;
    }

    public double getAccCurValue() {
        return accCurValue;
    }

    public void setAccCurValue(double accCurValue) {
        this.accCurValue = accCurValue;
    }

    @Override
    public String toString() {
        return "GenAcc{" +
                "accNo='" + accNo + '\'' +
                ", accName='" + accName + '\'' +
                ", accType='" + accType + '\'' +
                ", accCate='" + accCate + '\'' +
                ", accSubCate='" + accSubCate + '\'' +
                ", accOpenValue=" + accOpenValue +
                ", accHolder='" + accHolder + '\'' +
                ", accDetail='" + accDetail + '\'' +
                ", accCurValue=" + accCurValue +
                '}';
    }
}
