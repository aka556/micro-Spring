package org.xiaoyu.micro.jdbc.with.tx;

import org.xiaoyu.micro.annotation.Autowired;
import org.xiaoyu.micro.annotation.Component;
import org.xiaoyu.micro.annotation.Transactional;
import org.xiaoyu.micro.jdbc.JdbcTemplate;
import org.xiaoyu.micro.jdbc.JdbcTestBase;

import java.util.List;

@Component
@Transactional
public class AddressService {

    @Autowired
    UserService userService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addAddress(Address... addresses) {
        for (Address address : addresses) {
            // check if userId is exist:
            userService.getUser(address.userId);
            jdbcTemplate.update(JdbcTestBase.INSERT_ADDRESS, address.userId, address.address, address.zip);
        }
    }

    public List<Address> getAddresses(int userId) {
        return jdbcTemplate.queryForList(JdbcTestBase.SELECT_ADDRESS_BY_USERID, Address.class, userId);
    }

    public void deleteAddress(int userId) {
        jdbcTemplate.update(JdbcTestBase.DELETE_ADDRESS_BY_USERID, userId);
        if (userId == 1) {
            throw new RuntimeException("Rollback delete for user id = 1");
        }
    }
}
