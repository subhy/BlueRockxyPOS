package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.entity.CustomEntity;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import java.sql.Date;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

   @Override
    public List<CustomEntity> getOrdersInfo(String itemcode, Date vfrom, Date vto) throws Exception {
       NativeQuery nativeQuery = session.createNativeQuery("SELECT o.date AS orderDate,i.code AS itemCode,i.description AS description,od.qty AS qty,i.unitPrice AS unitPrice FROM  `Order` o INNER JOIN OrderDetail od ON o.id = od.order_id INNER JOIN Item i ON od.item_code = i.code WHERE i.code = ?1 And o.date between ?2 and ?3");
       nativeQuery.setParameter(1,itemcode);
       nativeQuery.setParameter(2,vfrom);
       nativeQuery.setParameter(3,vto);

       Query<CustomEntity> query = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
       List<CustomEntity> list = query.list();
       System.out.println(list.toString());
       return list;
    }

    @Override
    public List<CustomEntity> getOrdersInfo1(String itemcode) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT o.date AS orderDate,i.code AS itemCode,i.description AS description,od.qty AS qty,i.unitPrice AS unitPrice FROM  `Order` o INNER JOIN OrderDetail od ON o.id = od.order_id INNER JOIN Item i ON od.item_code = i.code WHERE i.code = ?1");
        nativeQuery.setParameter(1,itemcode);


        Query<CustomEntity> query = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
        List<CustomEntity> list = query.list();
        System.out.println(list.toString());
        return list;
    }

    @Override
    public List<CustomEntity> getOrdersInfo3(Date vfrom, Date vto) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT DISTINCT(i.code) AS itemCode,i.description AS description,SUM(od.qty) AS qty,i.unitPrice AS unitPrice FROM  `Order` o INNER JOIN OrderDetail od ON o.id = od.order_id INNER JOIN Item i ON od.item_code = i.code WHERE o.date between ?1 and ?2 GROUP BY i.code");
        nativeQuery.setParameter(1,vfrom);
        nativeQuery.setParameter(2,vto);

        Query<CustomEntity> query = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
        List<CustomEntity> list = query.list();
        return list;
    }

    @Override
    public List<CustomEntity> getOrdersInfo2() throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT o.date AS orderDate,i.code AS itemCode,i.description AS description,od.qty AS qty,i.unitPrice AS unitPrice FROM  `Order` o INNER JOIN OrderDetail od ON o.id = od.order_id INNER JOIN Item i ON od.item_code = i.code");


        Query<CustomEntity> query = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
        List<CustomEntity> list = query.list();
        System.out.println(list.toString());
        return list;
    }

    @Override
    public List<CustomEntity> getOrdersInfo4(String orderId) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT o.date AS orderDate,i.code AS itemCode,i.description AS description,od.qty AS qty,i.unitPrice AS unitPrice FROM  `Order` o INNER JOIN OrderDetail od ON o.id = od.order_id INNER JOIN Item i ON od.item_code = i.code WHERE order_id = ?1");
        nativeQuery.setParameter(1,orderId);


        Query<CustomEntity> query = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
        List<CustomEntity> list = query.list();
        System.out.println(list.toString());
        return list;

    }

    @Override
    public List<CustomEntity> getOrdersInfo5(Date datefrom, Date dateto) throws Exception {
        NativeQuery nativeQuery = session.createNativeQuery("SELECT DISTINCT(i.code) AS itemCode,i.description AS description,SUM(od.qty) AS qty,SUM(i.unitPrice) AS unitPrice FROM  `Order` o INNER JOIN OrderDetail od ON o.id = od.order_id INNER JOIN Item i ON od.item_code = i.code WHERE o.date between ?1 and ?2 GROUP BY i.code");
        nativeQuery.setParameter(1,datefrom);
        nativeQuery.setParameter(2,dateto);

        Query<CustomEntity> query = nativeQuery.setResultTransformer(Transformers.aliasToBean(CustomEntity.class));
        List<CustomEntity> list = query.list();
        System.out.println(list);
        return list;
    }



}
