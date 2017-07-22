package tech.shann.entity;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Created by shann on 17/7/21.
 */
public class Company {

    private Long id;
    private String companyName;

    @TableField(exist = false)
    private Integer nameLength;

    @TableField(exist = false)
    private Integer staffCount;


    public Integer getNameLength() {
        return nameLength;
    }

    public void setNameLength(Integer nameLength) {
        this.nameLength = nameLength;
    }

    public Integer getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(Integer staffCount) {
        this.staffCount = staffCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
