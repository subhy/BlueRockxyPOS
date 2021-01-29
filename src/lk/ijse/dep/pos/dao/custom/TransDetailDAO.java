package lk.ijse.dep.pos.dao.custom;

import lk.ijse.dep.pos.dao.CrudDAO;
import lk.ijse.dep.pos.entity.TransDetail;

import java.sql.Date;
import java.util.List;

public interface TransDetailDAO extends CrudDAO<TransDetail,String> {
    Integer getLastOrderId() throws Exception;

    List<TransDetail> findAllTransByDate(String searchText, Date fromDate, Date toDate) throws Exception;

    List<TransDetail> findAllTransByGroup(String selectCate, Date fromDate, Date toDate, String typeE) throws Exception;

    boolean existsByCateDate(String oilSale,String toDate,String pMethod,String subCate) throws Exception;

    Double findTotalAndDisc(String oId,String oilSale, String toDate,String pMethod,String subCate) throws Exception;

    boolean existsByDisDate(String disGiven, String toDate) throws Exception;

    Double findTotal(String sDateFrom, String sDateTo, String pType, String subCate) throws Exception;

    void updateAmount(Double selAmount, String dpDate) throws Exception;

    void upDateVoidSale(String orderId, String dpDate,Double disAmount) throws Exception;

    String findTransDetailByDateCate(String accNo, String dpDate, String accDesc) throws Exception;

    String getTransNoByOrderId(String orderId) throws Exception;
}
