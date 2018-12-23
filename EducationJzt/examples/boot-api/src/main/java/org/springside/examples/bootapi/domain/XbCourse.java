package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

// JPA实体类的标识

/**
 * 课程表
 */
@Entity
public class XbCourse {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String courseTypeId;
    public String courseTypeName;
    public String subjectId;
    public String subjectName;
    public String courseName;
    @Column(name = "type", nullable = true, columnDefinition ="char(1)")
    public String type;
    @Column(name = "state", nullable = true, columnDefinition ="char(1)")
    public String state;
    @Column(name = "charging_mode", nullable = true, columnDefinition ="char(1)")
    public String chargingMode;
    public BigDecimal tuitionFee;
    public String tuitionType;
    public Integer period;
    public String remarks;
    @Column(name = "opening_type", nullable = true, columnDefinition ="char(1)")
    public String openingType;
    public String preCourseIds;
    public String preCourseNames;
    public String createDate;
    public String createTime;
    public XbCourse() {

    }

    public XbCourse(String id) {
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



    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }



    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChargingMode() {
        return chargingMode;
    }

    public void setChargingMode(String chargingMode) {
        this.chargingMode = chargingMode;
    }

    public BigDecimal getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(BigDecimal tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public String getTuitionType() {
        return tuitionType;
    }

    public void setTuitionType(String tuitionType) {
        this.tuitionType = tuitionType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOpeningType() {
        return openingType;
    }

    public void setOpeningType(String openingType) {
        this.openingType = openingType;
    }

    public String getPreCourseIds() {
        return preCourseIds;
    }

    public void setPreCourseIds(String preCourseIds) {
        this.preCourseIds = preCourseIds;
    }

    public String getPreCourseNames() {
        return preCourseNames;
    }

    public void setPreCourseNames(String preCourseNames) {
        this.preCourseNames = preCourseNames;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
