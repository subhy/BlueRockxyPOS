package lk.ijse.dep.pos.business.custom;

import lk.ijse.dep.pos.business.SuperBO;
import lk.ijse.dep.pos.dto.TransDetailDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface TransDetailBO extends SuperBO {

    void saveTransDetail(TransDetailDTO transDetail) throws Exception;

    void updateTransDetail(TransDetailDTO transDetail) throws Exception;

    void deleteTransDetail(String transNo) throws Exception;

    List<TransDetailDTO> findAllTransDetail() throws Exception;

    List<TransDetailDTO> findTransDetail(String transNo) throws Exception;

    Integer getLastOrderId() throws Exception;

    List<TransDetailDTO> findAllTransDetailByDate(String searchText, Date fromDate, Date toDate) throws Exception;

    List<TransDetailDTO> findAllTransDetailGroup(String selectCate, Date fromDate, Date toDate, String typeE) throws Exception;

    boolean existsByCateDate(String oilSale, String toDate,String pMethod,String subCate) throws Exception;

    boolean existsByDisDate(String disGiven, String toDate) throws Exception;

    Double getTotalAndDis(String oId, String oilSale, String toDate, String pMethod,String subCate) throws Exception;

    Double getTotal(String sDateFrom, String sDateTo, String pType, String subCate) throws Exception;

    void updateAmount(Double selAmount, String dpDate) throws Exception;

    void upDateVoidSale(String orderId, String dpDate,Double disAmount) throws Exception;

    String findTransDetailByDateCategory(String accNo, String dpDate, String accDesc) throws Exception;

    String getTransaNoByOrderId(String orderId) throws Exception;
}
