package org.springside.examples.bootapi.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

// JPA实体类的标识

/**
 * 科目表
 */
@Entity
public class XbSubject {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String subjectName;
    @Column(name = "state", nullable = true, columnDefinition ="char(1)")
    public String state;
    //@OneToOne(mappedBy = "xb_attend_class", fetch=FetchType.EAGER)
    //public XbAttendClass xbattendclass;
    public XbSubject() {

    }
    public XbSubject(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "XbSubject{" +
                "id='" + id + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setState(String state) {
        this.state = state;
    }
}
