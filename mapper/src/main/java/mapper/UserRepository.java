package mapper;

import com.domain.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public User findUserByUsername(String username) throws EmptyResultDataAccessException {
        try {
            String SQL = "select * from sys_user where name=?";
            Map<String, Object> user = jdbc.queryForMap(SQL, username);
            SQL = "select role from user_authority where user_id=?";
            List<Map<String, Object>> user_auths = jdbc.queryForList(SQL, user.get("id"));
            List<String> roles = new ArrayList<>();
            for (Map<String, Object> auth: user_auths) {
                roles.add((String) auth.get("role"));
            }

            return new User(
                    (int) user.get("id"),  // 数据库中 id 不为 unsigned int 才能取出为 int 类型，否则为 long
                    (String) user.get("name"),
                    (String) user.get("pwd"),
                    roles);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveWithoutId(User user) {
        try {
            findUserByUsername(user.getUsername());
        } catch (EmptyResultDataAccessException ignored) {
        }

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("insert into sys_user(name, pwd) values(?,?)", Statement.RETURN_GENERATED_KEYS);  // Statement.RETURN_GENERATED_KEYS) 确保能够使用 KeyHolder 返回值
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                return ps;
            }
        };

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        int userId = keyHolder.getKey().intValue();
        String SQL = "insert into user_authority values(?,?)";
        List<String> roles = user.getRoles();
        for (String role: roles) {
            jdbc.update(SQL, userId, role);
        }
    }
}