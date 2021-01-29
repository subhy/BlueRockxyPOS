package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.GenAccDAO;
import lk.ijse.dep.pos.entity.GenAcc;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class GenAccDAOImpl extends CrudDAOImpl<GenAcc,String> implements GenAccDAO {

    @Override
    public List<GenAcc> findAccByCate(String categoryCh) throws Exception {
        NativeQuery<GenAcc> nativeQuery = session.createNativeQuery("SELECT * FROM GenAcc ga WHERE ga.accCate = ?1 ", GenAcc.class);
        nativeQuery.setParameter(1,categoryCh);

        List<GenAcc> resultList = nativeQuery.getResultList();
        return resultList;
    }
}
