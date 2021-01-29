package lk.ijse.dep.pos.dao.custom;

import lk.ijse.dep.pos.dao.CrudDAO;
import lk.ijse.dep.pos.entity.GenAcc;

import java.util.List;

public interface GenAccDAO extends CrudDAO<GenAcc,String> {
    List<GenAcc> findAccByCate(String categoryCh) throws Exception;
}
