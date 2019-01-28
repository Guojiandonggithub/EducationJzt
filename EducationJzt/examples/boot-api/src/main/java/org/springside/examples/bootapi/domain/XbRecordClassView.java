package org.springside.examples.bootapi.domain;

import javax.persistence.*;
import java.math.BigDecimal;
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
    public Date recordTime;
    //public String id;
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
    @Column(name="periodnum")
    public BigDecimal periodnum;
    @Column(name="sknum")
    public BigDecimal sknum;
    @Column(name="qjnum")
    public BigDecimal qjnum;
    @Column(name="kknum")
    public BigDecimal kknum;
    @Column(name="bknum")
    public BigDecimal bknum;
    public Integer establishNum;
    public String classId;
    public long studentCount;
    @Transient
    public BigDecimal totalReceivable;
}
