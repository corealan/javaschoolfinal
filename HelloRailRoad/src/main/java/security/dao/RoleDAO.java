package security.dao;

import security.model.security.Role;
/**
 * DAO-interface to provide operations with {@link Role} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface RoleDAO {
    Role getRoleById(Long id);
}
