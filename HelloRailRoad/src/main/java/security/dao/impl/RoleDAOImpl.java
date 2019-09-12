package security.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import security.dao.RoleDAO;
import security.model.security.Role;


@Repository
public class RoleDAOImpl implements RoleDAO {

    @Autowired
    SessionFactory sessionFactory;

    public Role getRoleById(Long id) {
        Query<Role> query = sessionFactory.getCurrentSession().createQuery("FROM Role r where r.id=:id", Role.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }
}
