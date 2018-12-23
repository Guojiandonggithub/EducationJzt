package org.springside.examples.bootapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

// JPA实体类的标识
@Entity
public class SysEmployeeSub {

    // JPA 主键标识, 策略为由数据库生成主键
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    public String id;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name="userId",referencedColumnName = "id",insertable = false,updatable = false)
    public SysEmployee sysEmployee;
    public String subSfz;
    public Date subSr;
    public String subYwm;
    public String subYoux;
    public String subJg;
    public String subMz;
    public String subZz;
    public String subHy;
    public String subYuanx;
    public String subZy;
    public String subXl;
    public String subPxjl;
    public String subQt;
    public String subBj;
    public Date subHtrqks;
    public Date subHtrqjs;
    public Integer subLdht;
    public Integer subLdgx;
    public Date subZzrq;
    public String subGzkkhh;
    public String subGzkkh;
    public String subSb;
    public String userId;
    @Transient
    public String employeeSubid;

    public SysEmployeeSub() {

    }

    public SysEmployeeSub(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
