package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ZhangLei on 2019/1/24 0024
 */
@Entity
//@Table(name="XB_STUDENT_RELATION_VIEW")
//@NamedQuery(name="XbStudentRelationView.findAll",query = "SELECT t FROM XbStudentRelationView t")
public class XbStudentRelationView {
    @Id
   // @Column(name="id")
    public String id;
   // @Column(name="student_name")
    public String studentName;
   // @Column(name="contact_phone")
    public String contactPhone;
   // @Column(name="student_id")
    public String studentId;
   // @Column(name="organ_id")
    public String organId;
   // @Column(name="course_id")
    public String courseId;
   // @Column(name="enroll_date")
    public Date enrollDate;
   // @Column(name="student_start")
    public int studentStart;
   // @Column(name="class_id")
    public String classId;
   // @Column(name="class_name")
    public String className;
   // @Column(name="teacher_id")
    public String teacherId;
   // @Column(name="employee_name")
    public String employeeName;
    public BigDecimal receivable;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;


    @OneToOne()
    @JoinColumn(name="courseId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbCourse xbCourse;
    public XbStudentRelationView() {
    }
    public XbStudentRelationView(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
