package tech.shann;

import tech.shann.entity.User;
import tech.shann.entity.enums.Sex;
import tech.shann.entity.mapper.UserMapper;
import com.baomidou.mybatisplus.MybatisSessionFactoryBuilder;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by shann on 17/7/3.
 */
public class XmlTest {
    
    @Test
    public void testDeleteByPojo(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            User u = new User();
            u.setMobile("22");

            int i = session.delete("tech.shann.entity.mapper.UserMapper.deleteUser", u);
            System.out.println(i);//

        }
    }

    @Test
    public void testDeleteByMap(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<>();
            m.put("mobile",11);

            int i = session.delete("tech.shann.entity.mapper.UserMapper.deleteUser", m);
            System.out.println(i);//
        }
    }

    @Test
    public void testUpdatePojo(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            User u = session.selectOne("tech.shann.entity.mapper.UserMapper.selectUser0",1);
            u.setSex(Sex.FEMALE);
            u.setUserName("小乔");
            u.setCreateTime(new Date());
            u.setMobile("111");

//            int i = session.update("tech.shann.entity.mapper.UserMapper.updateUser3", u);
            int i = session.update("tech.shann.entity.mapper.UserMapper.updateUser4", u);//和上面那个sql等价
            System.out.println(i);//
        }
    }

    @Test
    public void testUpdateBatch(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<>();
            m.put("sex", Sex.FEMALE);
            m.put("suffix","女");

            int i = session.update("tech.shann.entity.mapper.UserMapper.updateUser2", m);
            System.out.println(i);//
        }
    }

    @Test
    public void testUpdateOneById(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            User u = session.selectOne("tech.shann.entity.mapper.UserMapper.selectUser0",1);
            u.setSex(Sex.FEMALE);
            u.setUserName("小乔");

            int i = session.update("tech.shann.entity.mapper.UserMapper.updateUser1", u);
            System.out.println(i);//
        }
    }

    @Test
    public void testInsertBatch(){
        Long begin = System.currentTimeMillis();
        final int t = 5_0000;//如果是50万条，直接内存不足，不会抛sql too long的异常
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            List<User> list = new ArrayList<>();

            User u = new User();
//            u.setUserName("曹操");
//            u.setMobile("44");
//            u.setSex(Sex.MALE);
//            u.setCreateTime(new Date());
//
//            list.add(u);
            for(int i=0;i<t;i++){
                u = new User();
                u.setUserName("cc");
                u.setMobile("999"+i);
                u.setSex(Sex.FEMALE);
                u.setCreateTime(new Date());
                u.setMinMoney(10f);

                list.add(u);
            }

            int i = session.insert("tech.shann.entity.mapper.UserMapper.insertUser1", list);
            System.out.println(i);//2
        }
        System.out.println(t+"条 cost: "+(System.currentTimeMillis()-begin));//50000条 cost: 5725ms
    }


    @Test
    public void testInsertOne(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            User u = new User();
//            u.setId(1l);
            u.setUserName("曹操");
            u.setMobile("44");
            u.setSex(Sex.MALE);
            u.setCreateTime(new Date());
            int i = session.insert("tech.shann.entity.mapper.UserMapper.insertUser0", u);
            System.out.println(i);//1
        }
    }

    @Test
    public void testSelectSingleColumn(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<String> names = session.selectList("tech.shann.entity.mapper.UserMapper.selectSingleColumn2", null);
            for(String n : names){
                System.out.println(n);
            }
            List<Long> ids = session.selectList("tech.shann.entity.mapper.UserMapper.selectSingleColumn3", null);
            for(Long id: ids){
                System.out.println(id);
            }
        }
    }

    @Test
    public void testSelectPage(){//自动添加limit
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Page<User> page = new Page(1,2);
            List<User> users = session.selectList("tech.shann.entity.mapper.UserMapper.selectPage0",
                    null,page);
            page.setRecords(users);

            System.out.println("page.getSize(): "+page.getSize());//
            System.out.println("page.getTotal(): "+page.getTotal());//
            for(User u : page.getRecords()){
                System.out.println(u.getId());
                System.out.println(u.getMobile());
                System.out.println(u.getSex());
                System.out.println(u.getUserName());
            }
        }
    }

    @Test
    public void testSelectPageOfFromSubSelect(){//自动添加limit
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Page<User> page = new Page(2,2);
            List<User> users = session.selectList("tech.shann.entity.mapper.UserMapper.selectPage3", null,page);
            page.setRecords(users);

            System.out.println("page.getSize(): "+page.getSize());//
            System.out.println("page.getTotal(): "+page.getTotal());//
            for(User u : page.getRecords()){
                System.out.println(u.getId());
                System.out.println(u.getMobile());
                System.out.println(u.getSex());
                System.out.println(u.getUserName());
                System.out.println(u.getSexCount());
            }
        }
    }

    @Test
    public void testSelectPageOfWhereSubSelect(){//自动添加limit
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Page<User> page = new Page(2,2);
            List<User> users = session.selectList("tech.shann.entity.mapper.UserMapper.selectPage2", null,page);
            page.setRecords(users);

            System.out.println("page.getSize(): "+page.getSize());//
            System.out.println("page.getTotal(): "+page.getTotal());//
            for(User u : page.getRecords()){
                System.out.println(u.getId());
                System.out.println(u.getMobile());
                System.out.println(u.getSex());
                System.out.println(u.getUserName());
                System.out.println(u.getSexCount());
            }
        }
    }

    @Test
    public void testSelectListBind(){//使用<bind>标签
        testSelectListLike();
    }

    @Test
    public void testSelectListLike(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<String,Object>(){{
                put("namePart","香");
            }};
            List<User> users = session.selectList("tech.shann.entity.mapper.UserMapper.selectBind",m);
            System.out.println(users.size());//
            for(User u : users){
                System.out.println(u.getMobile());
                System.out.println(u.getId());
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
            }

        }
    }

    @Test
    public void testSelectListIn(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            List<Integer> l = new ArrayList<Integer>(){{
                add(1);
                add(2);
            }};

            List<User> users = session.selectList("tech.shann.entity.mapper.UserMapper.selectIn",l);
            System.out.println(users.size());//
            for(User u : users){
                System.out.println(u.getId());
                System.out.println(u.getMobile());
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
            }

        }
    }

    @Test
    public void testSelectListIf(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<>();
            m.put("sex", Sex.FEMALE);
            m.put("mobile","222333444");

            List<User> users = session.selectList("tech.shann.entity.mapper.UserMapper.selectIf",m);
            System.out.println(users.size());//
            for(User u : users){
                System.out.println(u.getId());
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
                System.out.println(u.getMobile());
            }

        }
    }

    @Test
    public void testSelectOneModelJoin(){
        try(SqlSession session = sqlSessionFactory.openSession()) {

            User u = session.selectOne("tech.shann.entity.mapper.UserMapper.selectUserModel0",1);
            System.out.println(u.getUserName());
            System.out.println(u.getCompanyId());

        }
    }

    @Test
    public void testSelectOneByPojo(){
        try(SqlSession session = sqlSessionFactory.openSession()) {

            User up = new User();
            up.setId(2l);
            up.setSex(Sex.FEMALE);

            User u = session.selectOne("tech.shann.entity.mapper.UserMapper.selectUser2",up);
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().getDesc());

        }
    }
    @Test
    public void testSelectOneByMap(){
        try(SqlSession session = sqlSessionFactory.openSession()) {

            User u = session.selectOne("tech.shann.entity.mapper.UserMapper.selectUser1",new HashMap<String,Object>(){{
                put("id",2);
                put("sex", Sex.FEMALE);
            }});
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().getDesc());

        }
    }

    @Test
    public void testSelectOneById(){
        try(SqlSession session = sqlSessionFactory.openSession()) {

            User u = session.selectOne("tech.shann.entity.mapper.UserMapper.selectUser0",1);
            System.out.println(u.getUserName());
            System.out.println(u.getSex());
            System.out.println(u.getSex().getValue());
            System.out.println(u.getSex().getDesc());

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
