package org.springside.examples.bootapi.api;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.domain.XbCourseType;
import org.springside.examples.bootapi.repository.Result;
import org.springside.examples.bootapi.service.XbCourseService;
import org.springside.examples.bootapi.service.XbCourseTypeService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
    public XbCourseTypeService xbCourseTypeService;
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
        return "course";
    }
    /**
     * 跳转到新建课程
     * @return
     */
    @RequestMapping("/goaddcourse")
    public String goAddCourse(){
        logger.info("跳转到新建课程");
        return "newLesson";
    }
    @PostMapping("/savecoursetype")
    public void saveCourseType(@RequestBody XbCourseType xbcoursetype,HttpServletResponse resp){
        logger.info("新建课程");
        XbCourseType entity =  xbCourseTypeService.saveXbCourseType(xbcoursetype);
        //renderData(response,entity.toString());
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
        resp.setContentType("text/html;charset=UTF-8");
        try {
            resp.getWriter().println(jsonObject.toJSONString());
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        resp.setContentType("text/html;charset=UTF-8");
        try {
            resp.getWriter().println(jsonObject.toJSONString());
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("新建课程结束");
    }
}
