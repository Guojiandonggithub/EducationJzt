package org.springside.examples.bootapi.dto;

// JPA实体类的标识

/**
 * 上课记录
 */
public class XbRecordClassDto {

    public String attendId;
    public String className;
    public String courseName;
    public String employeeName;
    public String courseTypeName;
    public String recordTime;
    public String periodnum;
    public String sknum;
    public String qjnum;
    public String kknum;
    public String bknum;

    public String getAttendId() {
        return attendId;
    }

    public void setAttendId(String attendId) {
        this.attendId = attendId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getPeriodnum() {
        return periodnum;
    }

    public void setPeriodnum(String periodnum) {
        this.periodnum = periodnum;
    }

    public String getSknum() {
        return sknum;
    }

    public void setSknum(String sknum) {
        this.sknum = sknum;
    }

    public String getQjnum() {
        return qjnum;
    }

    public void setQjnum(String qjnum) {
        this.qjnum = qjnum;
    }

    public String getKknum() {
        return kknum;
    }

    public void setKknum(String kknum) {
        this.kknum = kknum;
    }

    public String getBknum() {
        return bknum;
    }

    public void setBknum(String bknum) {
        this.bknum = bknum;
    }
}
