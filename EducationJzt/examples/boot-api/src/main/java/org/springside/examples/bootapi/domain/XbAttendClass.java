package org.springside.examples.bootapi.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

// JPA实体类的标识

/**
 * 排课表
 */
@Entity
public class XbAttendClass {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String classId;
    public String className;
    public String wayOfTeaching;
    public String startDateTime;
    public String weekDay;
    public String endDateTime;
    public String teacherId;
    public String teacherName;
    public String tutorId;
    public String tutorName;
    public String classRoomId;
    public String classRoomName;
    public String subjectId;
    public String subjectName;
    public String classTheme;
    public String content;
    public String remarks;
    public String classPhoto;
    public String createUserId;
    public String createUserName;
    public String createDate;
    public String timeInterval;
    @Transient
    public String weekType;
    @Column(name = "schedule_mode", nullable = true, columnDefinition ="char(1)")
    public String scheduleMode;
    @OneToOne()
    @JoinColumn(name="subjectId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbSubject xbsubject;
    @OneToOne()
    @JoinColumn(name="classId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbClass xbclass;
    @OneToOne()
    @JoinColumn(name="classRoomId",referencedColumnName = "id",insertable = false,updatable = false)
    public XbClassroom xbclassroom;
    @OneToOne()
    @JoinColumn(name="teacherId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysEmployee sysemployee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getWayOfTeaching() {
        return wayOfTeaching;
    }

    public void setWayOfTeaching(String wayOfTeaching) {
        this.wayOfTeaching = wayOfTeaching;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(String classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassTheme() {
        return classTheme;
    }

    public void setClassTheme(String classTheme) {
        this.classTheme = classTheme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClassPhoto() {
        return classPhoto;
    }

    public void setClassPhoto(String classPhoto) {
        this.classPhoto = classPhoto;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getScheduleMode() {
        return scheduleMode;
    }

    public void setScheduleMode(String scheduleMode) {
        this.scheduleMode = scheduleMode;
    }

    public XbSubject getXbsubject() {
        return xbsubject;
    }

    public void setXbsubject(XbSubject xbsubject) {
        this.xbsubject = xbsubject;
    }

    public XbClass getXbclass() {
        return xbclass;
    }

    public void setXbclass(XbClass xbclass) {
        this.xbclass = xbclass;
    }

    public XbClassroom getXbclassroom() {
        return xbclassroom;
    }

    public void setXbclassroom(XbClassroom xbclassroom) {
        this.xbclassroom = xbclassroom;
    }

    public SysEmployee getSysemployee() {
        return sysemployee;
    }

    public void setSysemployee(SysEmployee sysemployee) {
        this.sysemployee = sysemployee;
    }

}
