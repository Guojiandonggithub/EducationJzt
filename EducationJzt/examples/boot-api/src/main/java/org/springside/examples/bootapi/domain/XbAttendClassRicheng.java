package org.springside.examples.bootapi.domain;




// JPA实体类的标识

import org.springframework.stereotype.Controller;

import javax.persistence.*;

/**
 * 科目表
 */
@Entity
@Table(name="XB_ATTEND_CLASS_RICHENG")
@NamedQuery(name="XbAttendClassRicheng.findAll",query = "SELECT t FROM XbAttendClassRicheng t")
public class XbAttendClassRicheng {
    @Id
    @Column(name="id")
    public String id;
    @Column(name="start_date_time")
    public String startDateTime;
    @Column(name="time_interval")
    public String timeInterval;
    @Column(name="delete_status")
    public String deleteStatus;
    @Column(name="classroom_name")
    public String classroomName;
    @Column(name="organ_id")
    public String organId;
    @Column(name="organ_name")
    public String organName;
    @Column(name="course_type_name")
    public String courseTypeName;
    @Column(name="class_name")
    public String className;
    @Column(name="employee_name")
    public String employeeName;
    @Column(name="subject_id")
    public String subjectId;
    @Column(name="xb_class_id")
    public String xbClassId;
    @Column(name="class_theme")
    public String classTheme;




}
