package org.xiaoyu.hello.service;

import org.xiaoyu.hello.User;
import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.annotation.Transactional;
import org.xiaoyu.micro.jdbc.JdbcTemplate;

import java.util.List;

@Component
@Transactional
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void initDb() {
        String sql = """
                CREATE TABLE IF NOT EXISTS user (
                    email VARCHAR(50) PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    password VARCHAR(50) NOT NULL,
                )
                """;

        jdbcTemplate.update(sql);
    }

    public User getUser(String email) {
        return jdbcTemplate.queryForObject("SELECT * FROM user WHERE email = ?", User.class, email);
    }

    public List<User> getUsers() {
        return jdbcTemplate.queryForList("SELECT email, name FROM user", User.class );
    }

    public User createUser(String email, String name, String password) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.password = password;

        jdbcTemplate.update("INSERT INTO user (email, name, password) VALUES (?, ?, ?)", user.email, user.name, user.password);
        return user;
    }
}
