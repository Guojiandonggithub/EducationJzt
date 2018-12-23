package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.repository.XbClassDao;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.XbAttendClassService;
import org.springside.examples.bootapi.service.XbStudentService;
import org.springside.examples.bootapi.service.XbSubjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 排课
 * Created by ZhangLei on 2018/12/15 0015
 */
@Controller
@RequestMapping(value="/jw_center_arranging_course")
public class JwCenterArrangingCoursesActivity {
    private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);
    @Autowired
    public XbAttendClassService xbAttendClassService;
    @Autowired
    public XbSubjectService xbSubjectService;
    @Autowired
    public XbStudentService xbStudentService;
    @Autowired
    public EmployeeService employeeService;

    /**
     * 跳转到排课
     * @return
     */
    @RequestMapping("/to_arranging_course")
    public String toArrangingCourse(ModelMap model){
        logger.info("跳转到排课");
        model.addAttribute("xbAttendList",xbAttendClassService.findXbAttendClassAll());
        model.addAttribute("xbSubjectList",xbSubjectService.findSubjectAll());
        model.addAttribute("xbClassList",xbStudentService.findXbClassListAll());
        model.addAttribute("xbClassroomList",xbStudentService.findXbClassRoomListAll());
        model.addAttribute("sysEmployeeList",employeeService.findSysEmployeeListAll());
        return "courseArray";
    }
    /**
     * 保存排课
     * @return
     */
    @PostMapping("/save_xbAttend_class")
    public void saveXbAttendClass(@RequestBody XbAttendClass xbAttendClass,HttpServletResponse resp){
        logger.info("保存排课");
        JSONObject jsonObject = new JSONObject();
        XbAttendClass rsxc = xbAttendClassService.saveXbAttendClass(xbAttendClass);
        XbSubject xbsubject = xbSubjectService.findById(rsxc.getSubjectId());
        rsxc.setXbsubject(xbsubject!=null?xbsubject:new XbSubject());
       XbClass findxbclass = xbStudentService.getXbClassById(rsxc.getClassId());
        XbClass responxbclass = new XbClass();
        responxbclass.setId(findxbclass.getId());
        responxbclass.setClassName(findxbclass.getClassName());
        rsxc.setXbclass(responxbclass!=null?responxbclass:new XbClass());
        XbClassroom xbclassroom = xbStudentService.getXbClassroomById(rsxc.getClassRoomId());
        rsxc.setXbclassroom(xbclassroom!=null?xbclassroom:new XbClassroom());
        SysEmployee sysemployee = employeeService.getSysEmployeeById(rsxc.getTeacherId());
        rsxc.setSysemployee(sysemployee!=null?sysemployee:new SysEmployee());
        if(!StringUtils.isEmpty(rsxc.getId())){
            jsonObject.put("msg", "新建排课成功");
            jsonObject.put("status","0");
            jsonObject.put("rsxbAttendClass",com.alibaba.fastjson.JSONObject.toJSON(rsxc));
        }else{
            jsonObject.put("msg", "新建排课失败");
            jsonObject.put("status","1");
        }
        HttpServletUtil.reponseWriter(jsonObject,resp);
    }
    /**
     * 跳转到排课日程表
     * @return
     */
    @PostMapping("/to_arranging_course_fullcalendar")
    public String toArrangingCourseFullcalendar(HttpServletRequest req,
                                                HttpServletResponse resp,
            @RequestParam(value="start",defaultValue = "") String start,
            @RequestParam(value="fend",defaultValue = "") String fend){
        logger.info("跳转到排课");
        List<XbAttendClass> listentity = xbAttendClassService.findXbAttendClassAll();
        for(XbAttendClass xba:listentity){
            XbClass xbclass = xbStudentService.getXbClassById(xba.getClassId());
            XbClass responxbclass = new XbClass();
            responxbclass.setId(xbclass.getId());
            responxbclass.setClassName(xbclass.getClassName());
            xba.setXbclass(responxbclass);
        }
        JSONObject jsonObject = new JSONObject();
        if(listentity.size()>0){
            jsonObject.put("msg", "查询排课成功");
            jsonObject.put("listentity", com.alibaba.fastjson.JSONObject.toJSON(listentity));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "查询排课失败");
        }
        HttpServletUtil.reponseWriter(jsonObject,resp);
        return "courseArray";
    }
    @PostMapping("/remove_xbAttend_class")
    public void removeCourse(@RequestBody XbAttendClass xbattendclass, HttpServletResponse resp){
        logger.info("删除课程");
        boolean rs = xbAttendClassService.removeXbAttendClass(xbattendclass);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除课程成功");
            jsonObject.put("xbattendclass", com.alibaba.fastjson.JSONObject.toJSON(xbattendclass));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除课程失败");
        }
        logger.info("删除课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
}
