package lk.ijse.dep.pos.dao.custom;

import lk.ijse.dep.pos.dao.CrudDAO;
import lk.ijse.dep.pos.dto.ItemDTO;
import lk.ijse.dep.pos.entity.Item;

import java.util.List;

public interface ItemDAO extends CrudDAO<Item, String> {

    String getLastItemCode() throws Exception;

    List<Item> getSearchList(String value) throws Exception;

}
