package org.springside.examples.bootapi.dto;

// JPA实体类的标识

/**
 * 课程表
 */
public class XbCourseDto {

    public String id;
    public String courseName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
