package tech.shann.entity.mapper;

import tech.shann.entity.Company;
import tech.shann.entity.User;
import tech.shann.entity.enums.Sex;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by shann on 17/7/3.
 */
public interface UserMapper extends BaseMapper<User>{

    @Delete("delete from sys_user where mobile=#{mobile}")
    int deleteUser1(User u);

    @Delete("delete from sys_user where mobile=#{mobile}")
    int deleteUser0(Map<String, Object> m);

    @Update("update sys_user set" +
            "    user_name = concat(user_name,'_',#{suffix})" +
            "    where sex = ${sex.value}")
    int batchUpdateUser(Map<String, Object> m);

    @Update("update sys_user set" +
            "    user_name = #{userName}," +
            "    sex = ${sex.value}" +
            "    where id = #{id}")
    int updateUser(User u);

    @Insert("insert into sys_user (user_name,mobile,create_time,sex)" +
            " values (#{userName},#{mobile},#{createTime},${sex.value})")
    int insertUser(User u);

    @Select("select u.*,uc.company_id " +
            "from sys_user u " +
            "  left join user_company uc ON (u.id = uc.user_id) " +
            "where u.id = #{id}")
    User selectUserModel1(Long id);

    @Select("select c.id,c.company_name,count(u.id) as staff_count,length(c.company_name) as name_length " +
            "from sys_user u " +
            "  left join user_company uc ON (u.id = uc.user_id) " +
            "  left join company c ON (c.id = uc.company_id) " +
            "group by c.id,c.company_name")
    List<Company> selectCompanyModel1();

    @Select("select * from sys_user where id = #{id} and sex = ${sex.value}")
    User selectUser6(User user);

    @Select("select * from sys_user where id = #{id} and sex = ${sex.value}")
    User selectUser5(@Param("id") Long id, @Param("sex") Sex sex);

    @Select("select * from sys_user where id = #{id}")
    User selectUser4(Long id);

    User selectUser3(@Param("id") Long id);

    @Select("select * from sys_user where sex = ${sex.value}")
    List<User> selectPage1(Page page, Map<String,Object> m);

    @Select("select user_name from sys_user")
    List<String> selectSingleColumn0();

    @Select("select id from sys_user")
    List<Long> selectSingleColumn1();
}
