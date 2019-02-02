package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 学员课程班级
 */
@Entity
public class XbStudentRelationViewNew {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    @NotNull(message="用户不能为空")
    public String studentId;
    @NotNull(message="校区不能为空")
    public String organId;
    @NotNull(message="课程不能为空")
    public String courseId;
    @NotNull(message="班级不能为空")
    public String classId;
    public BigDecimal receivable;
    public String remarksIn;
    public BigDecimal periodNum;
    public Date enrollDate;
    public Integer studentStart;
    public BigDecimal totalReceivable;
    public BigDecimal totalPeriodNum;
    public Date validityDate;
    public Date classBeginDate;
    public Date classEndDate;
    public String className;
    public String teacherId;
    public String employeeName;
    public String xbStudentRalationId;
    public String deleteStatus;
    @Transient
    public String relationId;

    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;

    @OneToOne()
    @JoinColumn(name="studentId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbStudent xbStudent;

    @OneToOne()
    @JoinColumn(name="courseId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbCourse xbCourse;

    public XbStudentRelationViewNew() {
    }
    public XbStudentRelationViewNew(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
