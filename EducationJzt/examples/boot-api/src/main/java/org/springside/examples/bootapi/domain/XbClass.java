package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

// JPA实体类的标识

/**
 * 班级
 */
@Entity
public class XbClass {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String className;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;
    public String organId;
    public String organName;
    @OneToOne()
    @JoinColumn(name="courseId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbCourse xbCourse;
    public String courseId;
    public String courseName;
    public Date classBeginDate;
    public Date classEndDate;
    public Integer preRecruitNum;
    public Integer establishNum;
    public String isUndetermined;
    public String recruitState;
    public String isEnd;
    public Integer studentNum;
    public Integer teacherNum;
    @OneToOne()
    @JoinColumn(name="teacherId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysEmployee teacher;
    public String teacherId;
    public String teacherName;
    /*@OneToOne()
    @JoinColumn(name="tutorId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysEmployee tutor;*/
    public String tutorId;
    public String tutorName;
    @OneToOne()
    @JoinColumn(name="classroomId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbClassroom xbClassroom;
    public String classroomId;
    public String classroomName;
    public String remarks;

    public XbClass() {

    }

    public XbClass(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
