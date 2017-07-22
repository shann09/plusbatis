package tech.shann;

import tech.shann.base.BaseTest;
import tech.shann.entity.User;
import tech.shann.entity.mapper.CompanyMapper;
import tech.shann.entity.mapper.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by shann on 17/7/21.
 */

public class SpringTest extends BaseTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Test
    public void test(){
        User u = userMapper.selectById(2l);
        System.out.println(u.getUserName());
        System.out.println(u.getSex());
        System.out.println(u.getSex().getValue());
        System.out.println(u.getSex().name());
    }

}
