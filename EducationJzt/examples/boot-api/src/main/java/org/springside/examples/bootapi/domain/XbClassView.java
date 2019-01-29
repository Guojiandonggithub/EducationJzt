package org.springside.examples.bootapi.domain;

import javax.persistence.*;

/**
 * Created by ZhangLei on 2019/1/24 0024
 */
@Entity
@Table(name="XB_CLASS_VIEW")
@NamedQuery(name="XbClassView.findAll",query = "SELECT t FROM XbClassView t")
public class XbClassView {
    @Id
    @Column(name="id")
    public String id;
    @Column(name="organ_id")
    public String organId;
    @Column(name="teacher_id")
    public String teacherId;
    @Column(name="class_name")
    public String className;
    @Column(name="course_name")
    public String courseName;
    @Column(name="delete_status")
    public String deleteStatus;
}
