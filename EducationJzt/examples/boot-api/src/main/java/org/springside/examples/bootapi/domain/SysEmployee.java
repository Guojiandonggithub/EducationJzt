package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

// JPA实体类的标识
@Entity
public class SysEmployee {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    @NotNull(message="名称不能为空")
    public String employeeName;
    public String mobilePhone;
    public Integer sex;
    public String card;
    public Integer employeeType;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;
    @NotNull(message="是否教室不能为空")
    public Integer isAttendClass;
    @NotNull(message="用户名不能为空")
    public String userName;
    @NotNull(message="密码不能为空")
    public String password;
    public Integer isAllow;
    public String photo;
    @NotNull(message="校区不能为空")
    public String organId;
    public String deleteStatus;

    public SysEmployee() {

    }

    public SysEmployee(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
