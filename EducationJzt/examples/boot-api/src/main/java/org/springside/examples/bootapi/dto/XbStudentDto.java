package org.springside.examples.bootapi.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springside.examples.bootapi.domain.SysOrgans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 学员
 */
public class XbStudentDto {

    // JPA 主键标识, 策略为由数据库生成主键
    public String id;
    public String studentName;
    public String contactPhone;
    public String contactRelation;
    public String sex;
    public String photo;
    public String paymentType;
    public String handlePerson;
    public String salesPerson;
    public String deleteStatus;

    public XbStudentDto() {

    }

    public XbStudentDto(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
