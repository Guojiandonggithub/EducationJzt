package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 课程表
 */
@Entity
public class XbRecordClass {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String attendId;
    public String studentId;
    public String studentName;
    public String studentImg;
    public String state;
    public String discipline;
    public String actives;
    public BigDecimal deductPeriod;
    public String teacherLeave;
    public String studentPhoto;
    public Date record_time;
    @Transient
    public String classId;
    @Transient
    public String courseId;

    public XbRecordClass() {

    }

    public XbRecordClass(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
