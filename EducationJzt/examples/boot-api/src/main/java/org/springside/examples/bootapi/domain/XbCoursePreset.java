package org.springside.examples.bootapi.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

// JPA实体类的标识

/**
 * 课程表
 */
@Entity
public class XbCoursePreset {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String courseId;
    public Integer periodNum;
    public BigDecimal money;

    public String organIds;
    public String organNames;
    @OneToOne()
    @JoinColumn(name="organIds",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysorgans;
    public XbCoursePreset() {
    }
    public XbCoursePreset(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public Integer getPeriodNum() {
        return periodNum;
    }

    public void setPeriodNum(Integer periodNum) {
        this.periodNum = periodNum;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOrganIds() {
        return organIds;
    }

    public void setOrganIds(String organIds) {
        this.organIds = organIds;
    }

    public String getOrganNames() {
        return organNames;
    }

    public void setOrganNames(String organNames) {
        this.organNames = organNames;
    }
}
