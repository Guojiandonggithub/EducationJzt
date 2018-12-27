package org.springside.examples.bootapi.api;

import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 教务中心-学员
 * Created by ZhangLei on 2018/12/15 0015
 */
@Controller
@RequestMapping(value="/jwcentercourse")
public class JwCenterCourseActivity {
    private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);
    @Autowired
    public XbCourseService xbCourseService;
    @Autowired
    public XbSubjectService xbSubjectService;
    @Autowired
    public XbCourseTypeService xbCourseTypeService;
    @Autowired
    public OrgansService organsService;
    @Autowired
    public XbCoursePresetService xbCoursePresetService;
    /**
     * 跳转到课程列表
     * @return
     */
    @RequestMapping("/tocourse")
    public String toCourse(ModelMap model){
        logger.info("跳转到课程");
        List<XbCourse> list = xbCourseService.findCourseAll();
        logger.info("查询到课程list.size="+list.size());
        model.addAttribute("courselist",list);
        List<XbCourseType> coursetypelist = xbCourseTypeService.findCourseTypeAll();
        model.addAttribute("coursetypelist",coursetypelist);
        List<XbSubject> subjectlist = xbSubjectService.findSubjectAll();
        model.addAttribute("subjectlist",subjectlist);
        return "course";
    }
    /**
     * 跳转到新建课程
     * @return
     */
    @RequestMapping("/goaddcourse")
    public String goAddCourse(HttpServletRequest req,
            @RequestParam(value="courseId",defaultValue = "") String courseId, ModelMap model){
        logger.info("跳转到新建课程");
        List<XbCourseType> coursetypelist = xbCourseTypeService.findCourseTypeAll();
        model.addAttribute("coursetypelist",coursetypelist);
        List<XbSubject> subjectlist = xbSubjectService.findSubjectAll();
        model.addAttribute("subjectlist",subjectlist);
        model.addAttribute("xbCourse",null);
        model.addAttribute("presetlist",null);
        if(!courseId.equals("")){
            XbCourse xbc = xbCourseService.findById(courseId);
            model.addAttribute("xbCourse",xbc);
            List<XbCoursePreset> persetList = xbCoursePresetService.findXbCoursePresetListByCourseid(courseId);
            if(persetList.size()>0){
                model.addAttribute("presetlist",persetList);
                model.addAttribute("preset",persetList.get(0));
            }
        }
        List<SysOrgans> sorganList = organsService.getOrgansListAll();
        model.addAttribute("sorganList",sorganList);
        return "newLesson";
    }

    /**
     * 录入课程表和指定校区表
     * @param dataentity
     * @param resp
     */
    @RequestMapping("/savecourse")
    public void saveCourse(@RequestParam String dataentity,
                           HttpServletResponse resp){
        logger.info("新建课程");
        boolean app = true;
        String rsmsg = "新建课程成功";
        XbCourse xbcourse = com.alibaba.fastjson.JSONObject.parseObject(dataentity,XbCourse.class);
        boolean isadd = true;
        if(StringUtils.isNotEmpty(xbcourse.getId())){
            isadd=false;
        }
        com.alibaba.fastjson.JSONObject obj= com.alibaba.fastjson.JSONObject.parseObject(dataentity);//获取jsonobject对象
        List<Map<String,String>> list=(List)obj.getJSONArray("xbcoursepresetlist");//获取的结果集合转换成数组
        Date date  = new Date();
        xbcourse.setCreateDate(DateUtil.getDateStr(date));
        xbcourse.setCreateTime(DateUtil.getTimeStr(date));
        XbCourse entity =  xbCourseService.saveXbCourse(xbcourse);
        if(StringUtils.isEmpty(entity.getId())){
            app = false;
            rsmsg ="录入课程表为空";
        }else{
            //指定校区save
            if(entity.getOpeningType().equals("1")){
                logger.info("指定校区插入entity.getId()="+entity.getId()+",list.size()="+list.size());
                app = designatedCampus(list,entity.getId(),app);
                if(!app){
                    rsmsg = "指定校区保存失败";
                }

            //全部校区
            }else{
                logger.info("全部校区插入entity.getId()="+entity.getId()+",list.size()="+list.size());
                app = allSchool(list,entity.getId(),app,isadd);
                if(!app){
                    rsmsg = "全部校区保存失败";
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        if(app){
            jsonObject.put("status","0");
            jsonObject.put("cousrtype",com.alibaba.fastjson.JSONObject.toJSON(entity));

        }else{
            jsonObject.put("status","1");
        }
        jsonObject.put("msg", rsmsg);
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建结束");
    }

    /**
     * 全部校区
     * @return
     */
    private boolean allSchool(List<Map<String,String >> list,String xbcid,boolean app,boolean isadd){
        Map allmap = list.get(0);
        //查出所有校区信息
        if(isadd){
            List<SysOrgans> sorganlistlist = organsService.getOrgansListAll();
            for(SysOrgans sysorg: sorganlistlist){
                XbCoursePreset pre = new XbCoursePreset();
                String courid = (String)allmap.get("courseId");
                if(StringUtils.isEmpty(courid) || courid.equals("null")){
                    courid = xbcid;
                }
                pre.setCourseId(courid);
                pre.setOrganIds(sysorg.id );
                pre.setMoney(BigDecimal.valueOf(Double.parseDouble((String)allmap.get("money"))));
                pre.setPeriodNum(Integer.parseInt((String)allmap.get("periodNum")));
                XbCoursePreset rs= xbCoursePresetService.saveXbCoursePreset(pre);
                if(StringUtils.isEmpty(rs.getId())){
                    app = false;
                    break;
                }
            }
        }else{
            List<XbCoursePreset> list1 = xbCoursePresetService.findXbCoursePresetListByCourseid(xbcid);
            app = designatedCampusEdit(list1,app,allmap);
        }

        return app;
    }
    /**
     * 编辑校区
     */
    private boolean designatedCampusEdit(List<XbCoursePreset> list,boolean app,Map allmap){
        for(XbCoursePreset map:list){
            XbCoursePreset pre = new XbCoursePreset();
            String courid = map.getCourseId();
                pre.setId(map.getId());
            pre.setCourseId(courid);
            pre.setMoney(BigDecimal.valueOf(Double.parseDouble((String)allmap.get("money"))));
            pre.setPeriodNum(Integer.parseInt((String)allmap.get("periodNum")));
            XbCoursePreset rs= xbCoursePresetService.saveXbCoursePreset(pre);
            if(StringUtils.isEmpty(rs.getId())){
                app = false;
                return app;
            }
        }
        return app;
    }
    /**
     * 指定校区
     */
    private boolean designatedCampus(List<Map<String,String >> list,String xbcid,boolean app){
        for(Map map:list){
            XbCoursePreset pre = new XbCoursePreset();
            String courid = (String)map.get("courseId");
            if(StringUtils.isEmpty(courid) || courid.equals("null")){
                courid = xbcid;
            }else {
                pre.setId((String)map.get("id"));

            }
            pre.setCourseId(courid);
            pre.setOrganIds((String)map.get("organIds") );
            pre.setMoney(BigDecimal.valueOf(Double.parseDouble((String)map.get("money"))));
            pre.setPeriodNum(Integer.parseInt((String)map.get("periodNum")));
            XbCoursePreset rs= xbCoursePresetService.saveXbCoursePreset(pre);
            if(StringUtils.isEmpty(rs.getId())){
                app = false;
                return app;
            }
        }
        return app;
    }
    /**
     * String 转List
     * @param result
     * @param key
     * @param t
     * @return
     */
    public static List  parseArray(String result,String key,Class t){
        com.alibaba.fastjson.JSONObject obj= com.alibaba.fastjson.JSONObject.parseObject(result);//获取jsonobject对象
        com.alibaba.fastjson.JSONArray arr=obj.getJSONArray(key);//获取的结果集合转换成数组
        String js= com.alibaba.fastjson.JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);//将array数组转换成字符串
        List  collection = com.alibaba.fastjson.JSONArray.parseArray(js, t);//把字符串转换成集合
            return collection;
    }
    @PostMapping("/savecoursetype")
    public void saveCourseType(@RequestBody XbCourseType xbcoursetype,HttpServletResponse resp){
        logger.info("新建课程");
        XbCourseType entity =  xbCourseTypeService.saveXbCourseType(xbcoursetype);
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(entity.getId())){
            jsonObject.put("msg", "新建课程类别成功");
            jsonObject.put("status","0");
            jsonObject.put("cousrtype",com.alibaba.fastjson.JSONObject.toJSON(entity));
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "新建课程类别失败");
        }
        logger.info("新建课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
    @PostMapping("/savesubject")
    public void saveSubject(@RequestBody XbSubject xbsubject,HttpServletResponse resp){
        logger.info("新建课程");
        xbsubject.setState("0");
        XbSubject entity =  xbSubjectService.saveXbSubject(xbsubject);
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(entity.getId())){
            jsonObject.put("msg", "新建科目成功");
            jsonObject.put("status","0");
            jsonObject.put("cousrtype",com.alibaba.fastjson.JSONObject.toJSON(entity));
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "新建科目失败");
        }
        logger.info("新建科目返回json参数="+jsonObject.toString());
        resp.setContentType("text/html;charset=UTF-8");
        try {
            resp.getWriter().println(jsonObject.toJSONString());
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("新建课程结束");
    }
    @PostMapping("/removecourse")
    public void removeCourse(@RequestBody XbCourse xbcourse,HttpServletResponse resp){
        logger.info("删除课程");
        boolean rs = xbCourseService.removeXbCourse(xbcourse);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除课程成功");
            jsonObject.put("course", com.alibaba.fastjson.JSONObject.toJSON(xbcourse));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除课程失败");
        }
        logger.info("删除课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
    @PostMapping("/removecoursetype")
    public void removeCourseType(@RequestBody XbCourseType xbcoursetype,HttpServletResponse resp){
        logger.info("删除课程");
        boolean rs = xbCourseTypeService.removeXbCourseType(xbcoursetype);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除课程类别成功");
            jsonObject.put("cousrtype", com.alibaba.fastjson.JSONObject.toJSON(xbcoursetype));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除课程类别失败");
        }
        logger.info("删除课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
    @PostMapping("/removesubject")
    public void removeSubject(@RequestBody XbSubject xbsubject,HttpServletResponse resp){
        logger.info("删除科目");
        boolean rs = xbSubjectService.removeXbSubject(xbsubject);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除科目成功");
            jsonObject.put("cousrtype", com.alibaba.fastjson.JSONObject.toJSON(xbsubject));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除科目失败");
        }
        logger.info("删除科目返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }

}
