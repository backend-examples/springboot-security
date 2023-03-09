package mapper;

import com.domain.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Role findRoleById (int id) {
        try {
            String SQL = "select * from sys_role where id=?";
            Map<String, Object> role = jdbcTemplate.queryForMap(SQL, id);

            return new Role((Integer) role.get("id"), (String) role.get("name"));
        } catch (Exception e) {
            return null;
        }
    }
}
