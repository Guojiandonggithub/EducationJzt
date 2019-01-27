package org.springside.examples.bootapi.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ZhangLei on 2019/1/24 0024
 */
@Entity
@Table(name="XB_RECORD_CLASS_VIEW")
@NamedQuery(name="XbRecordClassView.findAll",query = "SELECT t FROM XbRecordClassView t")
public class XbRecordClassView {
    @Id
    /*@Column(name="id")*/
    public String id;
    @Column(name="orgid")
    public String orgid;
    @Column(name="organ_name")
    public String organName;
    @Column(name="class_name")
    public String className;
    @Column(name="course_name")
    public String courseName;
    @Column(name="employee_name")
    public String employeeName;
    @Column(name="course_type_name")
    public String courseTypeNname;
    @Column(name="record_time")
    public Date recordTime;
    @Column(name="periodnum")
    public long periodnum;
    @Column(name="sknum")
    public long sknum;
    @Column(name="qjnum")
    public long qjnum;
    @Column(name="kknum")
    public long kknum;
    @Column(name="bknum")
    public long bknum;

}
