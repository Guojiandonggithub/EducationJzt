package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 办理中心
 */
@Entity
public class XbSupplementFee {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    @OneToOne()
    @JoinColumn(name="studentId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbStudent xbStudent;
    public String studentId;
    public String studentName;
    public String paymentType;
    public BigDecimal paymentMoney;
    public BigDecimal surplusMoney;
    public Date paymentDate;
    public String handlePerson;
    public String remarks;
    public String orderNumber;
    public String type;

    @Transient
    public String classId;
    @Transient
    public String toClassId;
    @Transient
    public String receivable;
    @Transient
    public String balanceamount;
    @Transient
    public String choosecourseId;
    @Transient
    public String subsidyMoney;

    public XbSupplementFee() {

    }

    public XbSupplementFee(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
