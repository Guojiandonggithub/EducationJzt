package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 学员课程班级
 */
@Entity
public class XbStudentRelation {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    @OneToOne()
    @JoinColumn(name="studentId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbStudent xbStudent;
    public String studentId;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;
    public String organId;
    @OneToOne()
    @JoinColumn(name="courseId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbCourse xbCourse;
    public String courseId;
    @OneToOne()
    @JoinColumn(name="classId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbClass xbClass;
    public String classId;
    public BigDecimal receivable;
    public String remarksIn;
    public String remarksOut;
    public Date enrollDate;
    @Transient
    public String relationId;

    public XbStudentRelation() {

    }

    public XbStudentRelation(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
