package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.entity.Item;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class ItemDAOImpl extends CrudDAOImpl<Item, String> implements ItemDAO {

    @Override
    public String getLastItemCode() throws Exception {
        return (String) session.createNativeQuery("SELECT code FROM Item ORDER BY code DESC LIMIT 1").uniqueResult();
    }

    @Override
    public List<Item> getSearchList(String value) throws Exception {
        NativeQuery<Item> nativeQuery = session.createNativeQuery("SELECT * FROM Item i WHERE i.code LIKE ?1 OR i.description LIKE ?2", Item.class);
        nativeQuery.setParameter(1,value);
        nativeQuery.setParameter(2,value);
        List<Item> resultList = nativeQuery.getResultList();
        return resultList;

    }

}
