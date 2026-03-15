package org.xiaoyu.micro.jdbc.with.tx;

import org.junit.jupiter.api.Test;
import org.xiaoyu.micro.context.AnnotationConfigApplicationContext;
import org.xiaoyu.micro.exception.DataAccessException;
import org.xiaoyu.micro.jdbc.JdbcTemplate;
import org.xiaoyu.micro.jdbc.JdbcTestBase;
import org.xiaoyu.micro.jdbc.without.tx.JdbcWithoutTxApplication;
import org.xiaoyu.micro.jdbc.without.tx.User;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcWithTxTest extends JdbcTestBase {

    @Test
    public void testJdbcWithTx() {
        try (var ctx = new AnnotationConfigApplicationContext(JdbcWithoutTxApplication.class, createPropertyResolver())) {
            JdbcTemplate jdbcTemplate = ctx.getBean(JdbcTemplate.class);
            jdbcTemplate.update(CREATE_USER);
            jdbcTemplate.update(CREATE_ADDRESS);

            // insert user:
            int userId1 = jdbcTemplate.updateAndReturnGeneratedKey(INSERT_USER, "xiaoyu", 12).intValue();
            int userId2 = jdbcTemplate.updateAndReturnGeneratedKey(INSERT_USER, "yuxie", 15).intValue();
            int userId3 = jdbcTemplate.updateAndReturnGeneratedKey(INSERT_USER, "world", null).intValue();
            assertEquals(1, userId1);
            assertEquals(2, userId2);
            assertEquals(3,userId3);

            // query user:
            org.xiaoyu.micro.jdbc.without.tx.User xiaoyu = jdbcTemplate.queryForObject(SELECT_USER, org.xiaoyu.micro.jdbc.without.tx.User.class, userId1);
            org.xiaoyu.micro.jdbc.without.tx.User yuxie = jdbcTemplate.queryForObject(SELECT_USER, org.xiaoyu.micro.jdbc.without.tx.User.class, userId2);
            org.xiaoyu.micro.jdbc.without.tx.User world = jdbcTemplate.queryForObject(SELECT_USER, org.xiaoyu.micro.jdbc.without.tx.User.class, userId3);
            assertEquals(1, xiaoyu.id);
            assertEquals("xiaoyu", xiaoyu.name);
            assertEquals(12, xiaoyu.theAge);
            assertEquals(2, yuxie.id);
            assertEquals("yuxie", yuxie.name);
            assertEquals(15, yuxie.theAge);
            assertEquals(3, world.id);
            assertEquals("world", world.name);
            assertNull(world.theAge);

            // query name:
            assertEquals("xiaoyu", jdbcTemplate.queryForObject(SELECT_USER_NAME, String.class, userId1));
            assertEquals(12, jdbcTemplate.queryForObject(SELECT_USER_AGE, int.class, userId1));

            // update user:
            int n1 = jdbcTemplate.update(UPDATE_USER, "xiaoyu jackson", 18, xiaoyu.id);
            assertEquals(1, n1);

            // delete user:
            int n2 = jdbcTemplate.update(DELETE_USER, yuxie.id);
            assertEquals(1, n2);
        }

        // re-open db and query:
        try (var ctx = new AnnotationConfigApplicationContext(JdbcWithoutTxApplication.class, createPropertyResolver())) {
            JdbcTemplate jdbcTemplate = ctx.getBean(JdbcTemplate.class);
            org.xiaoyu.micro.jdbc.without.tx.User xiaoyu = jdbcTemplate.queryForObject(SELECT_USER, org.xiaoyu.micro.jdbc.without.tx.User.class, 1);
            assertEquals("xiaoyu jackson", xiaoyu.name);
            assertEquals(18, xiaoyu.theAge);
            assertThrows(DataAccessException.class, () -> {
                // alice was deleted:
                jdbcTemplate.queryForObject(SELECT_USER, User.class, 2);
            });
        }
    }
}
