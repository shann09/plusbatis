package tech.shann;

import tech.shann.entity.Company;
import tech.shann.entity.User;
import tech.shann.entity.enums.Sex;
import tech.shann.entity.mapper.CompanyMapper;
import tech.shann.entity.mapper.UserMapper;
import com.baomidou.mybatisplus.MybatisSessionFactoryBuilder;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by shann on 17/7/20.
 */
public class PlusTest {


    @Test
    public void testDeleteManyByWrapper(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.delete(new EntityWrapper<User>().eq("mobile","555"));
            System.out.println(i);//

        }
    }

    @Test
    public void testDeleteManyByMap(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            Map<String,Object> m = new HashMap<>();
            m.put("mobile","777");//数据库

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.deleteByMap(m);
            System.out.println(i);//
        }
    }

    @Test
    public void testDeleteOneById(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            User u = new User();
            u.setId(9l);
            u.setMobile("22");

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.deleteById(u);
            System.out.println(i);//

        }
    }

    //updateBatch请使用注解方式，参考MapperTest.testUpdateBatch

    @Test
    public void testUpdateOneAllColumnById(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            User u = mapper.selectById(1l);
            u.setSex(Sex.FEMALE);
            u.setUserName("小乔");
            u.setCreateTime(null);
            //语句里有createTime
            // UPDATE sys_user SET user_name=?,mobile=?,sex=?,create_time=?,money=?,min_money=?,max_money=? WHERE id=?


            int i = mapper.updateAllColumnById(u);
            System.out.println(i);//
        }
    }

    @Test
    public void testUpdateOneById(){
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            User u = mapper.selectById(1l);
            u.setSex(Sex.FEMALE);
            u.setUserName("小乔");
            u.setCreateTime(null);
            //语句里没有createTime
            // UPDATE sys_user SET user_name=?, mobile=?, sex=?, money=?, min_money=?, max_money=? WHERE id=?


            int i = mapper.updateById(u);
            System.out.println(i);//
        }
    }

    /**
     * 不能用for循环，性能极低
     * 请使用xml方式的<foreach>，参考XmlTest.testInsertBatch
     */
    @Test
    public void testInsertBatchBad(){
        Long begin = System.currentTimeMillis();
        final int t = 5_0000;
        try(SqlSession session = sqlSessionFactory.openSession(true)) {
            List<User> list = new ArrayList<>();

            User u = new User();
            for(int i=0;i<t;i++){
                u = new User();
                u.setUserName("cc");
                u.setMobile("999"+i);
                u.setSex(Sex.FEMALE);
                u.setCreateTime(new Date());
                u.setMinMoney(10f);

                list.add(u);
            }

            UserMapper mapper = session.getMapper(UserMapper.class);
            for(User uu:list){
                Integer i = mapper.insert(uu);
                System.out.println(i);//
                System.out.println(uu.getId());
            }
        }
        System.out.println(t+"条 cost: "+(System.currentTimeMillis()-begin));//50000条 cost: die for this
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

            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.insert(u);
            System.out.println(i);//1
            System.out.println(u.getId());//888199297055592450
        }
    }

    @Test
    public void testSelectAggregate(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            CompanyMapper mapper = session.getMapper(CompanyMapper.class);
            List<Company> list = mapper.selectList(new EntityWrapper<Company>()
                    .setSqlSelect("id,company_name,length(company_name) as name_length"));
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
            List<String> names = (List)mapper.selectObjs(new EntityWrapper<User>().setSqlSelect("user_name"));
            for(String n : names){
                System.out.println(n);
            }
            List<Long> ids = (List)mapper.selectObjs(new EntityWrapper<User>().setSqlSelect("id"));
            for(Long id: ids){
                System.out.println(id);
            }
        }
    }

    /**
     *
     */
    @Test
    public void testSelectPage(){//自动添加limit
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            Page<User> page = new Page(1,2);
            List<User> users = mapper.selectPage(page,new EntityWrapper<User>()
                    .in("user_name",new ArrayList<String>(){{
                        add("刘备");
                        add("关羽");
                        add("张飞");
                    }}));
            page.setRecords(users);
            System.out.println("page.getSize()"+page.getSize());
            System.out.println("page.getTotal()"+page.getTotal());
            for(User u:page.getRecords()){
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
                System.out.println(u.getSex().getValue());
                System.out.println(u.getSex().name());
            }
        }
    }

    @Test
    public void testSelectListIn(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            List<User> l = mapper.selectList(new EntityWrapper<User>()
                    .in("user_name",new ArrayList<String>(){{
                        add("刘备");
                        add("关羽");
                        add("张飞");
                    }}));
            for(User u:l){
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
                System.out.println(u.getSex().getValue());
                System.out.println(u.getSex().name());
            }
        }
    }
    @Test
    public void testSelectListLike(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);

            List<User> l = mapper.selectList(new EntityWrapper<User>()
                    .like("user_name","刘"));//值自动转成"%孙%"
            for(User u:l){
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
                System.out.println(u.getSex().getValue());
                System.out.println(u.getSex().name());
            }
        }
    }

    @Test
    public void testSelectListByMap(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> l = mapper.selectByMap(new HashMap<String, Object>() {{
                put("id", 2l);
                put("sex", Sex.FEMALE);
                put("user_name", "孙尚香");
            }});
            // SELECT id,user_name AS userName,mobile,sex,create_time AS createTime FROM sys_user WHERE user_name = ? AND sex = ? AND id = ?
            for(User u:l){
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
                System.out.println(u.getSex().getValue());
                System.out.println(u.getSex().name());
            }
        }
    }

    @Test
    public void testSelectListByWrapper(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> l = mapper.selectList(new EntityWrapper<User>()
                    .eq("id",2l)
                    .eq("sex", Sex.FEMALE));
            for(User u:l){
                System.out.println(u.getUserName());
                System.out.println(u.getSex());
                System.out.println(u.getSex().getValue());
                System.out.println(u.getSex().name());
            }

        }
    }

    @Test
    public void testSelectCount(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int i = mapper.selectCount(new EntityWrapper<User>()
                    .eq("sex", Sex.FEMALE));
            System.out.println(i);
        }
    }

    @Test
    public void testSelectOneById(){
        try(SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User u = mapper.selectById(2l);
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
}
