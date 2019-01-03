package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

// JPA实体类的标识

/**
 * 课程类别表
 */
@Entity
public class XbCourseType {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String courseTypeName;
    public Integer layOrder;
    public String deleteStatus;
    public XbCourseType() {

    }
    public XbCourseType(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                ", layOrder=" + layOrder +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public Integer getLayOrder() {
        return layOrder;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public void setLayOrder(Integer layOrder) {
        this.layOrder = layOrder;
    }
}
