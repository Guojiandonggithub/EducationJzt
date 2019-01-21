package org.springside.examples.bootapi.dto;

// JPA实体类的标识

import java.util.ArrayList;
import java.util.List;

/**
 * 选择班级
 */
public class XbCoursePresetDto {

    public String totalReceivable;
    public String totalPeriodNum;
    public String courseName;
    public String choosecourseId;
    public List<XbClassesDto> xbClassList = new ArrayList<>();

}
