package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 学员
 */
@Entity
public class XbStudent {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String studentName;
    public String contactPhone;
    public String contactRelation;
    public String secondaryPhone;
    public String secondaryRelation;
    public String otherPhone;
    public String otherRelation;
    public Date birthday;
    public String sex;
    public String photo;
    public String paymentType;
    public BigDecimal paymentMoney;
    public BigDecimal surplusMoney;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;
    @NotNull(message="校区不能为空")
    public String organId;
    public String organName;
    public String salesSource;
    public Date handleDate;
    public String handlePerson;
    public String salesPerson;
    public String deleteStatus;

    public XbStudent() {

    }

    public XbStudent(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
