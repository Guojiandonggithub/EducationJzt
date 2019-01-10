package org.springside.examples.bootapi.api;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.XbAttendClassService;
import org.springside.examples.bootapi.service.XbStudentService;
import org.springside.examples.bootapi.service.XbSubjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * 点击班级级联查询
     * @return
     */
    @RequestMapping("/classToFindAll")
    public String classToFindAll(@RequestParam String id,ModelMap model){
       XbClass xbc = xbStudentService.getXbClass(id);
       //SysEmployee sse = xbc.teacher;
       //model.addAttribute("SysEmployee",sse);
       model.addAttribute("XbClass",xbc);
       return "courseArray::teacherfra";
    }
    /**
     * 跳转到排课
     * @return
     */
    @RequestMapping("/to_arranging_course")
    public String toArrangingCourse(ModelMap model, @RequestParam(required=false) String data,
                                    @PageableDefault(value = 10) Pageable pageable){
        logger.info("跳转到排课");
        findXbAttendClassPageListAll(model,pageable,data);
        findXbAttendConflicListi(model,pageable);
        model.addAttribute("xbSubjectList",xbSubjectService.findSubjectAll());
        model.addAttribute("xbClassList",xbStudentService.findXbClassListAll());
        model.addAttribute("xbClassroomList",xbStudentService.findXbClassRoomListAll());
        model.addAttribute("sysEmployeeList",employeeService.findSysEmployeeListAll());
        return "courseArray";
    }
    public void findXbAttendConflicListi(ModelMap model,@PageableDefault(value = 10) Pageable pageable){
         Map<String,Object> searmap = new HashMap<>();

         List<String> list = xbAttendClassService.findXbAttendConflictIdList();
         Page<XbAttendClass> xbAttendConflicList = null;
         if(list.size()>0){
             searmap.put("IN_id",list);
            xbAttendConflicList = xbAttendClassService.findXbAttendClassPageAll(pageable,searmap);
         }
        model.addAttribute("xbAttendConflicList",xbAttendConflicList);
         if(null != xbAttendConflicList &&xbAttendConflicList.getSize()>0){
             model.addAttribute("xbAttendConflicListsize",xbAttendConflicList.getSize());
         }

    }
    @RequestMapping("/findXbAttendConflicList")
    public String findXbAttendConflicList(ModelMap model,@PageableDefault(value = 10) Pageable pageable){
         Map<String,Object> searmap = new HashMap<>();

         List<String> list = xbAttendClassService.findXbAttendConflictIdList();
        Page<XbAttendClass> xbAttendConflicList = null;
        if(list.size()>0){
            searmap.put("IN_id",list);
             xbAttendConflicList = xbAttendClassService.findXbAttendClassPageAll(pageable,searmap);
        }
        model.addAttribute("xbAttendConflicList",xbAttendConflicList);
        if(null != xbAttendConflicList &&xbAttendConflicList.getSize()>0){
            model.addAttribute("xbAttendConflicListsize",xbAttendConflicList.getSize());
        }
        return "courseArray::xbAttendConflicListFra";
    }
    /**
     * 查询所有的排课信息
     */
    private void findXbAttendClassPageListAll(ModelMap model,Pageable pageable,String data){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> searhMap = new HashMap<>();
        if(null!=data){
            resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,searhMap.getClass());
        }
       String type = (String)resultMap.get("type");//下拉查询
        if(StringUtils.isEmpty(type)){
            type = "class_search";
        }
       String searhname = (String)resultMap.get("searhname");//下拉查询
        if(StringUtils.isNotEmpty(searhname)){
            if(type.equals("class_search")){
                searhMap.put("LIKE_xbclass.className",searhname);
            }else if(type.equals("less_search")){
                searhMap.put("LIKE_xbclass.xbCourse.courseName",searhname);
            }else if(type.equals("teacher_search")){
                searhMap.put("LIKE_sysemployee.employeeName",searhname);
            }else if(type.equals("class_room_search")){
                searhMap.put("LIKE_xbclassroom.classroomName",searhname);
            }
        }
        //授课模式
        String courtype = (String)resultMap.get("courtype");
        if(null==courtype){
            courtype = "-1";
        }else if(!courtype.equals("-1")){
            searhMap.put("EQ_wayOfTeaching",courtype);
        }
        //授课模式
        String subjectId = (String)resultMap.get("subjectId");
        if(null==subjectId){
            subjectId = "0";
        }else if(!subjectId.equals("0")){
            searhMap.put("EQ_subjectId",subjectId);
        }
       Page<XbAttendClass> xbAttendList = xbAttendClassService.findXbAttendClassPageAll(pageable,searhMap);
        for(XbAttendClass xbattendclass : xbAttendList){
            xbattendclass.ydstudentnum = xbAttendClassService.getYdstudentnum(xbattendclass.classId,xbattendclass.startDateTime);
            xbattendclass.sdstudentnum = xbAttendClassService.getSdstudentnum(xbattendclass.classId,xbattendclass.startDateTime);
        }
        model.addAttribute("xbAttendList",xbAttendList);
        model.addAttribute("xbAttendListsize",xbAttendList.getSize());
        model.addAttribute("searhname",searhname);
        model.addAttribute("type",type);
        model.addAttribute("courtype",courtype);
        model.addAttribute("subjectId",subjectId);
    }
    private boolean checkCourseClassAndTimeInterval(String classId,String timeInterval,String startDateTime,String id){
        Map<String,Object> searmap = new HashMap<>();
        searmap.put("EQ_classId",classId);
        searmap.put("EQ_timeInterval",timeInterval);
        searmap.put("EQ_startDateTime",startDateTime);
        searmap.put("EQ_deleteStatus","1");
        if(StringUtils.isNotEmpty(id)){
            searmap.put("NEQ_id",id);
        }
        List<XbAttendClass> xbalist = xbAttendClassService.findXbAttendClassAll(searmap);
        if(xbalist!=null && xbalist.size()>0){
            return true;
        }
        return false;
    }
    /**
     * 保存排课
     * @return,
     */
    @PostMapping("/save_xbAttend_class")
    public void saveXbAttendClass(@RequestBody XbAttendClass xbAttendClass,HttpServletResponse resp,Model model){
        logger.info("保存排课");
        String msg = "";
        String status = "";
        XbAttendClass rsxc = new XbAttendClass();
        if(xbAttendClass.getScheduleMode().equals("1") || StringUtils.isNotEmpty(xbAttendClass.getId())){
            //编辑
            if(xbAttendClass.getScheduleMode().equals("1") && StringUtils.isNotEmpty(xbAttendClass.getId())
                    && checkCourseClassAndTimeInterval(xbAttendClass.classId,xbAttendClass.timeInterval,xbAttendClass.startDateTime,xbAttendClass.getId())){
                XbClass xbc = xbStudentService.getXbClass(xbAttendClass.classId);
                msg = xbAttendClass.startDateTime+"的班级【"+xbc.getClassName()+"】时间段【"+xbAttendClass.timeInterval+"】已存在，不能重复";
                status = "01";
            //新增验证
            }else if(xbAttendClass.getScheduleMode().equals("1") && StringUtils.isEmpty(xbAttendClass.getId())
                    && checkCourseClassAndTimeInterval(xbAttendClass.classId,xbAttendClass.timeInterval,xbAttendClass.startDateTime,"")){
                XbClass xbc = xbStudentService.getXbClass(xbAttendClass.classId);
                msg = xbAttendClass.startDateTime+"的班级【"+xbc.getClassName()+"】时间段【"+xbAttendClass.timeInterval+"】已存在，不能重复";
                status = "01";
            }else {
                xbAttendClass.setWeekDay(DateUtil.dayForWeekChinses(xbAttendClass.getStartDateTime()));
                xbAttendClass.deleteStatus="1";
                rsxc = xbAttendClassService.saveXbAttendClass(xbAttendClass);
                //新增
                if(StringUtils.isEmpty(xbAttendClass.getId())){
                    //失败
                    if(StringUtils.isEmpty(rsxc.getId())){
                        msg = "新建日程失败";
                        status = "01";
                    }else {
                        msg = "新建日程成功";
                        status = "00";
                    }
                }else{
                    //失败
                    if(StringUtils.isEmpty(rsxc.getId())){
                        msg = "编辑日程失败";
                        status = "01";
                    }else {
                        msg = "编辑日程成功";
                        status = "00";
                    }
                }
            }

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
                    boolean is = true;
                    for (Date date : lDate)
                    {
                        if(DateUtil.doesItIncludeAWeek(xbAttendClass.weekType,sdf.format(date))){
                            if(checkCourseClassAndTimeInterval(xbAttendClass.classId,xbAttendClass.timeInterval,sdf.format(date),"")){
                                XbClass xbc = xbStudentService.getXbClass(xbAttendClass.classId);
                                msg = sdf.format(date)+"的班级【"+xbc.getClassName()+"】时间段【"+xbAttendClass.timeInterval+"】已存在，不能重复";
                                status = "01";
                                is = false;
                                break;
                            }
                        }
                    }
                    if(is){
                        for (Date date : lDate)
                        {
                            if(DateUtil.doesItIncludeAWeek(xbAttendClass.weekType,sdf.format(date))){

                                System.out.println("重复排课日期："+sdf.format(date));
                                xbAttendClass.setStartDateTime(sdf.format(date));
                                XbAttendClass xbAttendClassnew = new XbAttendClass();
                                BeanUtils.copyProperties(xbAttendClassnew, xbAttendClass);
                                xbAttendClassnew.setStartDateTime(sdf.format(date));
                                xbAttendClassnew.setWeekDay(DateUtil.dayForWeekChinses(xbAttendClass.getStartDateTime()));
                                xbAttendClassnew.deleteStatus = "1";
                                rsxc = xbAttendClassService.saveXbAttendClass(xbAttendClassnew);
                            }
                        }
                        if(StringUtils.isNotEmpty(rsxc.getId())){
                            msg = "新建日程成功";
                            status = "00";
                        }else{
                            msg = "新建日程失败";
                            status = "01";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        jsonObject.put("msg",msg);
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
        Map<String, Object> searchParams = new HashMap<>();
        List<XbAttendClass> listentity = xbAttendClassService.findXbAttendClassAll(searchParams);
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
    @RequestMapping("/remove_xbAttend_class")
    public String removeCourse(@RequestParam String id, HttpServletResponse resp,ModelMap model,@PageableDefault(value=10) Pageable pageable){
        logger.info("删除排课开始");
        XbAttendClass xbac = xbAttendClassService.findById(id);
        xbac.deleteStatus = "0";
        xbAttendClassService.saveXbAttendClass(xbac);
        Map<String,Object> searmap = new HashMap<>();
        Page<XbAttendClass> xbAttendList = xbAttendClassService.findXbAttendClassPageAll(pageable,searmap);
        for(XbAttendClass xbattendclass : xbAttendList){
            xbattendclass.ydstudentnum = xbAttendClassService.getYdstudentnum(xbattendclass.classId,xbattendclass.startDateTime);
            xbattendclass.sdstudentnum = xbAttendClassService.getSdstudentnum(xbattendclass.classId,xbattendclass.startDateTime);
        }
        model.addAttribute("xbAttendList",xbAttendList);
        logger.info("删除排课结束");
        return "courseArray::xbAttendListFra";
    }

    @RequestMapping("/remove_xbAttendconfilic_class")
    public String removeXbAttendConfilic(@RequestParam String id, HttpServletResponse resp,ModelMap model,@PageableDefault(value=10) Pageable pageable){
        logger.info("删除排课冲突开始");
        XbAttendClass xbac = xbAttendClassService.findById(id);
        xbac.deleteStatus = "0";
        xbAttendClassService.saveXbAttendClass(xbac);
        Map<String,Object> searmap = new HashMap<>();
        model.addAttribute("xbAttendConflicList",xbAttendClassService.findXbAttendConflictList());
        logger.info("删除排课冲突结束");
        return "courseArray::xbAttendConflicListFra";
    }
}
