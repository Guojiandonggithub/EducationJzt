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
public class XbSupplementFeeView {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String studentId;
    public String studentName;
    public String organId;
    public String contactPhone;
    public String contactRelation;
    public String courseName;
    public BigDecimal paymentMoney;
    public BigDecimal surplusMoney;
    public BigDecimal feeMoney;
    public BigDecimal registratioFee;
    public Date paymentDate;
    public String type;

    public XbSupplementFeeView() {

    }

    public XbSupplementFeeView(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
