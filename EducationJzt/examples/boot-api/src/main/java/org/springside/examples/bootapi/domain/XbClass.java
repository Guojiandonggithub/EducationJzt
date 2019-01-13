package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @NotNull(message="名称不能为空")
    public String className;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;
    @NotNull(message="校区不能为空")
    public String organId;
    public String organName;
    @OneToOne()
    @JoinColumn(name="courseId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbCourse xbCourse;
    @NotNull(message="课程不能为空")
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
    @NotNull(message="教师不能为空")
    public String teacherId;
    public String teacherName;
    public String deleteStatus;
    /*@OneToOne()
    @JoinColumn(name="tutorId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysEmployee tutor;*/
    public String tutorId;
    public String tutorName;
    @OneToOne()
    @JoinColumn(name="classroomId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbClassroom xbClassroom;
    @NotNull(message="教室不能为空")
    public String classroomId;
    public String classroomName;
    public String remarks;
    @Transient
    public List<XbCoursePreset> xbCoursePresetList = new ArrayList<>();

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
