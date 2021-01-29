package lk.ijse.dep.pos.dao.custom.impl;


import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.TransDetailDAO;
import lk.ijse.dep.pos.entity.TransDetail;
import org.hibernate.query.NativeQuery;


import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

public class TransDetailDAOImpl extends CrudDAOImpl<TransDetail,String> implements TransDetailDAO {
    @Override
    public Integer getLastOrderId() throws Exception {

        BigInteger laNo = (BigInteger) session.createNativeQuery("SELECT MAX(CAST(SUBSTRING(transNo,2) AS SIGNED)) FROM TransDetail ORDER BY transNo DESC LIMIT 1").uniqueResult();
        int lastNo = laNo.intValue();
        return lastNo;
    }

    @Override
    public List<TransDetail> findAllTransByDate(String searchText, Date fromDate, Date toDate) throws Exception {
        NativeQuery<TransDetail> nativeQuery = null;
        List<TransDetail> resultList = null;
        if (searchText.equals("") || searchText.length() == 0) {

            nativeQuery = session.createNativeQuery("SELECT * FROM TransDetail td WHERE td.transDate BETWEEN ?1 AND ?2 ", TransDetail.class);
            nativeQuery.setParameter(1, fromDate);
            nativeQuery.setParameter(2, toDate);
            resultList = nativeQuery.getResultList();
        } else {
            nativeQuery = session.createNativeQuery("SELECT * FROM TransDetail td WHERE td.transAccNo=?1 OR td.transCate=?4 OR td.transSubCate=?5 AND td.transDate BETWEEN ?2 AND ?3 ", TransDetail.class);
            nativeQuery.setParameter(1, searchText);
            nativeQuery.setParameter(4, searchText);
            nativeQuery.setParameter(5, searchText);

            nativeQuery.setParameter(2, fromDate);
            nativeQuery.setParameter(3, toDate);

            resultList = nativeQuery.getResultList();
        }
        return resultList;


    }

    @Override
    public List<TransDetail> findAllTransByGroup(String selectCate, Date fromDate,Date toDate, String typeE) throws Exception {
        System.out.println("From Date" + fromDate);
        System.out.println("To Date" + toDate);
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM TransDetail td WHERE td.transDate BETWEEN ?1 AND ?2 AND td.transType=?3", TransDetail.class);
        nativeQuery.setParameter(1, fromDate);
        nativeQuery.setParameter(2, toDate);
        nativeQuery.setParameter(3, typeE);
        List<TransDetail> resultList = nativeQuery.getResultList();
        return resultList;

    }

    @Override
    public boolean existsByCateDate(String oilSale, String toDate,String pMethod,String subCate) throws Exception {
      /*  return session.createNativeQuery("SELECT * FROM TransDetail WHERE transCate=?1 AND transDate=?2")
                .setParameter(1, customerId).uniqueResult() != null;*/

        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM TransDetail WHERE transCate=?1 AND transDate=?2 AND transType=?3 AND transSubCate=?4");
        nativeQuery.setParameter(1, oilSale);
        nativeQuery.setParameter(2, toDate);
        nativeQuery.setParameter(3, pMethod);
        nativeQuery.setParameter(4, subCate);
        List resultList = nativeQuery.getResultList();


        return resultList.size() >= 1;


    }

    @Override
    public Double findTotalAndDisc(String oId, String oilSale, String toDate,String pMethod,String subCate) throws Exception {


        Double transAmount = 0.00;
        if (oilSale == "Oil Sale" && oId=="") {


            NativeQuery nativeQuery = session.createNativeQuery("SELECT SUM(transAmount) FROM TransDetail WHERE transCate=?1 AND transDate=?2 AND transType=?3 AND transSubCate=?4");
            nativeQuery.setParameter(1, oilSale);
            nativeQuery.setParameter(2, toDate);
            nativeQuery.setParameter(3, pMethod);
            nativeQuery.setParameter(4, subCate);


            transAmount = (Double) nativeQuery.uniqueResult();

        } else if (oilSale == "Discount Given" && oId=="") {
            NativeQuery nativeQuery = session.createNativeQuery("SELECT transAmount FROM TransDetail WHERE transSubCate=?1 AND transDate=?2 ");
            nativeQuery.setParameter(1, oilSale);
            nativeQuery.setParameter(2, toDate);



            transAmount = (Double) nativeQuery.uniqueResult();

        }else if (oilSale == "Discount Given" && oId=="On"){
            NativeQuery nativeQuery = session.createNativeQuery("SELECT transNo FROM TransDetail WHERE transSubCate=?1 AND transDate=?2");
            nativeQuery.setParameter(1, oilSale);
            nativeQuery.setParameter(2, toDate);

            System.out.println("Fu");
            String transAmount1 = String.valueOf(nativeQuery.uniqueResult());
            if (transAmount1 == null) {
                transAmount = 0.00;
            } else {

                transAmount = Double.parseDouble(transAmount1.replace("T", ""));
            }
        }else if (oilSale == "Oil Sale" && oId == "On") {

            NativeQuery nativeQuery = session.createNativeQuery("SELECT transNo FROM TransDetail WHERE transCate=?1 AND transDate=?2 AND transType=?3 AND transSubCate=?4");
            nativeQuery.setParameter(1, oilSale);
            nativeQuery.setParameter(2, toDate);
            nativeQuery.setParameter(3, pMethod);
            nativeQuery.setParameter(4, subCate);

            String transAmount1 = String.valueOf(nativeQuery.uniqueResult());
            if (transAmount1 == null) {
                transAmount = 0.00;
            } else {

                transAmount = Double.parseDouble(transAmount1.replace("T", ""));
            }
        }
        return transAmount;
    }

    @Override
    public boolean existsByDisDate(String disGiven, String toDate) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM TransDetail WHERE transSubCate=?1 AND transDate=?2");
        nativeQuery.setParameter(1,disGiven);
        nativeQuery.setParameter(2,toDate);

        return nativeQuery.getResultList().size()>0;


    }

    @Override
    public Double findTotal(String sDateFrom, String sDateTo, String pType, String subCate) throws Exception {
        Double transAmount = 0.00;
        NativeQuery nativeQuery = session.createNativeQuery("SELECT (transAmount) FROM TransDetail WHERE transDate BETWEEN ?1 AND ?2 AND transType=?3 AND transSubCate=?4");
        nativeQuery.setParameter(1, sDateFrom);
        nativeQuery.setParameter(2, sDateTo);
        nativeQuery.setParameter(3, pType);
        nativeQuery.setParameter(4, subCate);


        transAmount = (Double) nativeQuery.uniqueResult();
        return transAmount;

    }

    @Override
    public void updateAmount(Double selAmount, String dpDate) throws Exception {
        String oilSale="Oil Sale";
      //   NativeQuery nativeQuery = session.createNativeQuery("UPDATE TransDetail td SET td.transAmount=transAmount-?1 WHERE td.transCate=?2 AND td.transAccNo=?3 AND td.transSubCate=?4");
         NativeQuery nativeQuery = session.createNativeQuery("UPDATE TransDetail td SET td.transAmount=transAmount-?1 WHERE td.transCate=?2 AND td.transDate=?3 AND td.transAccNo=?4 AND td.transSubCate=?5");


         nativeQuery.setParameter(1, selAmount);
          nativeQuery.setParameter(2, oilSale);
          nativeQuery.setParameter(3, dpDate);
          nativeQuery.setParameter(4, oilSale);
          nativeQuery.setParameter(5, oilSale);


    }

    @Override
    public void upDateVoidSale(String orderId, String dpDate, Double disAmount) throws Exception {
//if (orderId.equals(null)){
        NativeQuery nativeQuery = session.createNativeQuery("UPDATE TransDetail td SET td.transAmount =td.transAmount-?1 WHERE td.transDate=?2 AND td.transAccNo=?3 AND td.transSubCate=?4");
        nativeQuery.setParameter(1, disAmount);
        nativeQuery.setParameter(2, dpDate);
        nativeQuery.setParameter(3, "Oil Sale");
        nativeQuery.setParameter(4, "Discount Given");

//}
    }

    @Override
    public String findTransDetailByDateCate(String accNo, String dpDate, String accDesc) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT td.transNo FROM TransDetail td WHERE td.transAccNo=?1 AND td.transDate=?2 AND td.transDesc=?3");
        nativeQuery.setParameter(1,accNo);
        nativeQuery.setParameter(2,dpDate);
        nativeQuery.setParameter(3,accDesc);
        String transNo = (String) nativeQuery.uniqueResult();
        return transNo;

    }

    @Override
    public String getTransNoByOrderId(String orderId) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT td.transNo FROM TransDetail td WHERE td.linkTransNo=?1");
        nativeQuery.setParameter(1,orderId);
        String transNo = (String) nativeQuery.uniqueResult();
        return transNo;
    }


}
