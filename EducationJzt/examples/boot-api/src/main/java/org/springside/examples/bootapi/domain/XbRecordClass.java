package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull(message="班级不能为空")
    public String attendId;

    @OneToOne()
    @JoinColumn(name="attendId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbClass xbClass;

    @NotNull(message="学生不能为空")
    public String studentId;

    @OneToOne()
    @JoinColumn(name="studentId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbStudent xbStudent;

    public String studentName;
    public String studentImg;
    public String state;
    public String discipline;
    public String actives;
    public BigDecimal deductPeriod;
    public String teacherLeave;
    public String studentPhoto;
    public String studentRelationId;

    @OneToOne()
    @JoinColumn(name="studentRelationId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbStudentRelation xbStudentRelation;

    public String deleteStatus;
    public Date recordTime;
    @Transient
    public String recordTimeTemp;
    @Transient
    public String classId;
    @Transient
    public String courseId;
    @Transient
    public String organId;

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
