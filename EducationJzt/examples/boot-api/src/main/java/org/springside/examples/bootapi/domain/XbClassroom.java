package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

// JPA实体类的标识

/**
 * 课程表
 */
@Entity
public class XbClassroom {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    public String classroomName;
    @OneToOne()
    @JoinColumn(name="organId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysOrgans sysOrgans;
    public String organId;
    public String organName;

    public XbClassroom() {

    }

    public XbClassroom(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
