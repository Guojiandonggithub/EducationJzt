package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

// JPA实体类的标识
@Entity
public class SysEmployee {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String employeeName;
    public String mobilePhone;
    public Integer sex;
    public String card;
    public Integer employeeType;
    public Integer organId;
    public Integer isAttendClass;
    public String userName;
    public String password;
    public Integer isAllow;
    public String photo;

    public SysEmployee() {

    }

    public SysEmployee(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
