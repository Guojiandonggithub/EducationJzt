package org.springside.examples.bootapi.api;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.repository.XbClassDao;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.XbAttendClassService;
import org.springside.examples.bootapi.service.XbStudentService;
import org.springside.examples.bootapi.service.XbSubjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        List<XbAttendClass> xbAttendClassList = xbAttendClassService.findXbAttendClassAll();
        model.addAttribute("xbAttendList",xbAttendClassList);
        model.addAttribute("xbattendTotal",xbAttendClassList.size());
        model.addAttribute("xbSubjectList",xbSubjectService.findSubjectAll());
        model.addAttribute("xbClassList",xbStudentService.findXbClassListAll());
        model.addAttribute("xbClassroomList",xbStudentService.findXbClassRoomListAll());
        model.addAttribute("sysEmployeeList",employeeService.findSysEmployeeListAll());
        return "courseArray";
    }

    public static void main(String[] args) {
        try {
            Calendar cal = Calendar.getInstance();
            String start = "2018-09-20";
            String end = "2019-09-30";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dBegin = sdf.parse(start);
            Date dEnd = sdf.parse(end);
            List<Date> lDate = DateUtil.findDates(dBegin, dEnd);
            String[] str = {"4","7"};
            for (Date date : lDate)
            {
               /* if(DateUtil.doesItIncludeAWeek(str,sdf.format(date))){
                    System.out.println(sdf.format(date));
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存排课
     * @return
     */
    @PostMapping("/save_xbAttend_class")
    public void saveXbAttendClass(@RequestBody XbAttendClass xbAttendClass,HttpServletResponse resp,Model model){
        logger.info("保存排课");
        XbAttendClass rsxc = new XbAttendClass();
        if(xbAttendClass.getScheduleMode().equals("1") || StringUtils.isNotEmpty(xbAttendClass.getId())){
             rsxc = xbAttendClassService.saveXbAttendClass(xbAttendClass);
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
        }else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = null;
            try {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dBegin = sdf.parse(xbAttendClass.getStartDateTime());
                Date dEnd = sdf.parse(xbAttendClass.getEndDateTime());
                List<Date> lDate = DateUtil.findDates(dBegin, dEnd);
                if(xbAttendClass.weekType.indexOf("0")>-1){
                    xbAttendClass.weekType = "1,2,3,4,5,6,7";
                }
                for (Date date : lDate)
                {
                    if(DateUtil.doesItIncludeAWeek(xbAttendClass.weekType,sdf.format(date))){
                        System.out.println("重复排课日期："+sdf.format(date));
                        xbAttendClass.setStartDateTime(sdf.format(date));
                        XbAttendClass xbAttendClassnew = new XbAttendClass();
                        BeanUtils.copyProperties(xbAttendClassnew, xbAttendClass);
                        xbAttendClassnew.setStartDateTime(sdf.format(date));
                         rsxc = xbAttendClassService.saveXbAttendClass(xbAttendClassnew);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(rsxc.getId())){
            if(StringUtils.isNotEmpty(xbAttendClass.getId())){
                jsonObject.put("msg", "编辑排课成功");
                jsonObject.put("status","01");
            }else{
                jsonObject.put("status","00");
                jsonObject.put("msg", "新建排课成功");
            }

            jsonObject.put("rsxbAttendClass",com.alibaba.fastjson.JSONObject.toJSON(rsxc));
        }else{
            if(StringUtils.isNotEmpty(xbAttendClass.getId())){
                jsonObject.put("msg", "新建排课失败");
            }else{
                jsonObject.put("msg", "编辑排课失败");
            }

            jsonObject.put("status","1");
        }
        HttpServletUtil.reponseWriter(jsonObject,resp);
       /* model.addAttribute("xbAttendList",xbAttendClassService.findXbAttendClassAll());
        return "courseArray::attendClassList";*/

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
