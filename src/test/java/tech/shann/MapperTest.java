package tech.shann;

import tech.shann.entity.Company;
import tech.shann.entity.User;
import tech.shann.entity.enums.Sex;
import tech.shann.entity.mapper.UserMapper;
import com.baomidou.mybatisplus.MybatisSessionFactoryBuilder;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shann on 17/7/3.
 */
public class MapperTest {

    @Test
    public void testDeleteByPojo(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            User u = new User();
            u.setMobile("22");

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.deleteUser1(u);
            System.out.println(i);//

        }
    }

    @Test
    public void testDeleteByMap(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<>();
            m.put("mobile",33);//数据库

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.deleteUser0(m);
            System.out.println(i);//
        }
    }

    @Test
    public void testUpdateBatch(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<>();
            m.put("sex", Sex.FEMALE);
            m.put("suffix","女");

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.batchUpdateUser(m);
            System.out.println(i);//
        }
    }

    @Test
    public void testUpdateOneById(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User u = mapper.selectUser4(1l);
            u.setSex(Sex.MALE);
            u.setUserName("周瑜");

            int i = mapper.updateUser(u);
            System.out.println(i);//
        }
    }

    //insertBatch请使用xml的<foreach>

    @Test
    public void testInsertOne(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {

            User u = new User();
            u.setUserName("曹操");
            u.setMobile("44");
            u.setSex(Sex.MALE);
            u.setCreateTime(new Date());

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.insertUser(u);
            System.out.println(i);

        }
    }

    @Test
    public void testSelectAggregate(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<Company> list = mapper.selectCompanyModel1();
            for(Company c : list){
                System.out.println(c.getCompanyName());
                System.out.println(c.getStaffCount());
                System.out.println(c.getNameLength());
            }
        }
    }

    @Test
    public void testSelectSingleColumn(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<String> names = mapper.selectSingleColumn0();
            for(String n : names){
                System.out.println(n);
            }
            List<Long> ids = (List)mapper.selectSingleColumn1();
            for(Long id: ids){
                System.out.println(id);
            }
        }
    }
    @Test
    public void testSelectPage(){//自动添加limit
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            Page<User> page = new Page(1,2);
            List<User> users = mapper.selectPage1(page, new HashMap<String,Object>(){{
                put("sex",Sex.MALE);
            }});
            page.setRecords(users);

//            System.out.println("p.getTotal(): "+users.getTotal());

            System.out.println("p.getSize(): "+page.getSize());//
            System.out.println("p.getTotal(): "+page.getTotal());//
            for(User u : page.getRecords()){
                System.out.println(u.getId());
                System.out.println(u.getMobile());
                System.out.println(u.getSex());
                System.out.println(u.getUserName());
            }
        }
    }

    @Test
    public void testSelectOneModelJoin(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User u = mapper.selectUserModel1(1l);
            System.out.println(u.getUserName());
            System.out.println(u.getCompanyId());

        }
    }

    @Test
    public void testSelectOneByPojo(){

        try(SqlSession session = sqlSessionFactory.openSession()) {

            UserMapper mapper = session.getMapper(UserMapper.class);

            User up = new User();
            up.setId(2l);
            up.setSex(Sex.FEMALE);
            User u = mapper.selectUser6(up);
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().name());
        }
    }
    @Test
    public void testSelectOneByParam(){

        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User u = mapper.selectUser5(2l, Sex.FEMALE);
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().name());
        }
    }
    @Test
    public void testSelectOneById(){
        try(SqlSession session = sqlSessionFactory.openSession()) {

            UserMapper mapper = session.getMapper(UserMapper.class);
            User u = mapper.selectUser4(2l);
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().name());
        }
    }
    @Test
    public void testSelectOneByIdXml(){
        try(SqlSession session = sqlSessionFactory.openSession()) {

            UserMapper mapper = session.getMapper(UserMapper.class);
            User u = mapper.selectUser3(2l);
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().name());
        }
    }

    private SqlSessionFactory sqlSessionFactory;

    //mybatis-plus载入
    @Before
    public void before() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

        GlobalConfiguration globalConf = new GlobalConfiguration();
        MybatisSessionFactoryBuilder factoryBuilder = new MybatisSessionFactoryBuilder();
        factoryBuilder.setGlobalConfig(globalConf);

        sqlSessionFactory = factoryBuilder.build(inputStream);//必须用inputStream，不能传原生的Configuration
    }

    //原生mybatis载入
//    @Before
//    public void before() throws IOException {
//        String resource = "mybatis-config.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//    }
}
