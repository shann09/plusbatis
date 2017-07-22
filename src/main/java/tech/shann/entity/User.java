package tech.shann.entity;

import tech.shann.entity.enums.Sex;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shann on 17/7/3.
 */
@TableName("sys_user")
public class User extends Model<User>{

    private Long id;
    private String userName;
    private String mobile;
    private Sex sex;
    private Date createTime;
    private Float money;
    private Float minMoney;
    private Float maxMoney;

    @TableField(exist = false)
    private Long companyId;

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private Integer sexCount;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getSexCount() {
        return sexCount;
    }

    public void setSexCount(Integer sexCount) {
        this.sexCount = sexCount;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(Float minMoney) {
        this.minMoney = minMoney;
    }

    public Float getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(Float maxMoney) {
        this.maxMoney = maxMoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
