package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

// JPA实体类的标识

/**
 * 课程表
 */
@Entity
public class XbCourse {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public Integer courseTypeId;
    public String courseTypeName;
    public Integer subjectId;
    public String subjectName;
    public String courseName;
    @Column(name = "type", nullable = true, columnDefinition ="char(1)")
    public String type;
    @Column(name = "state", nullable = true, columnDefinition ="char(1)")
    public String state;
    @Column(name = "charging_mode", nullable = true, columnDefinition ="char(1)")
    public String chargingMode;
    public BigDecimal tuitionFee;
    public String tuitionType;
    public Integer period;
    public String remarks;
    @Column(name = "opening_type", nullable = true, columnDefinition ="char(1)")
    public String openingType;
    public String preCourseIds;
    public String preCourseNames;

    public XbCourse() {

    }

    public XbCourse(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
