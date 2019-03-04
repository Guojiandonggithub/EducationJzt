package org.springside.examples.bootapi.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description:
 * @Author: vesus
 * @CreateDate: 2018/5/20 上午11:12
 * @Version: 1.0
 */
@Entity
public class SystemLog {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    private String id ;

    @Column(name = "requestip")
    private String requestip; //操作IP

    @Column(name = "type")
    private String type ;//  操作类型 1 操作记录 2异常记录

    @Column(name = "userid")
    private String userid ;// 操作人ID

    @Column(name = "username")
    private String username ;// 操作人名字

    @Column(name = "description")
    private String description;// 操作描述

    @Column(name = "actiondate")
    private Date actiondate ;// 操作时间

    @Column(name = "exceptioncode")
    private String exceptioncode ;// 异常code

    @Basic(fetch = FetchType.LAZY) @Column(columnDefinition = "text")
    private String exceptiondetail ;// 异常详情

    @Column(name = "actionmethod")
    private String actionmethod ;//请求方法

    @Lob
    @Basic(fetch = FetchType.LAZY) @Column(columnDefinition = "text")
    private String params;//请求参数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestip() {
        return requestip;
    }

    public void setRequestip(String requestip) {
        this.requestip = requestip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActiondate() {
        return actiondate;
    }

    public void setActiondate(Date actiondate) {
        this.actiondate = actiondate;
    }

    public String getExceptioncode() {
        return exceptioncode;
    }

    public void setExceptioncode(String exceptioncode) {
        this.exceptioncode = exceptioncode;
    }

    public String getExceptiondetail() {
        return exceptiondetail;
    }

    public void setExceptiondetail(String exceptiondetail) {
        this.exceptiondetail = exceptiondetail;
    }

    public String getActionmethod() {
        return actionmethod;
    }

    public void setActionmethod(String actionmethod) {
        this.actionmethod = actionmethod;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
