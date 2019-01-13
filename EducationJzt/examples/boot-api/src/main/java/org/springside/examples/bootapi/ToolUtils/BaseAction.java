package org.springside.examples.bootapi.ToolUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ZhangLei on 2019/1/13 0013
 */
@Controller
public class BaseAction {

    /**
     * 以JSON格式输出
     * @param response
     */
    /*public void responseOutWithJson(HttpServletResponse response,
                                    Object responseObject) {
        //将实体对象转换为JSON Object转换
        JSONObject responseJSONObject = JSONObject.fromObject(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }*/
    /**
     * 将对象转换成JSON字符串，并响应回前台
     *
     * @param object
     * @author 李彬
     */
    public void writeJson(HttpServletResponse servletActionContext, Object object) {
        try {
            // SerializeConfig serializeConfig = new SerializeConfig();
            // serializeConfig.setAsmEnable(false);
            // String json = JSON.toJSONString(object);
            // String json = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
            String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
            //String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd");
            // String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
            servletActionContext.setContentType("application/json;charset=utf-8");
            servletActionContext.getWriter().write(json);
            servletActionContext.getWriter().flush();
            servletActionContext.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeJson(HttpServletResponse servletActionContext, Object object,String dateFormatFlag) {
        try {
            // SerializeConfig serializeConfig = new SerializeConfig();
            // serializeConfig.setAsmEnable(false);
            // String json = JSON.toJSONString(object);
            // String json = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
            String json = "";
            if(dateFormatFlag.equals("long-date")){
                json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
            }else if(dateFormatFlag.equals("short-date")){
                json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd");
            }else if(dateFormatFlag.equals("time-date")){
                json = JSON.toJSONStringWithDateFormat(object, "HH:mm:ss");
            }else if(dateFormatFlag.equals("date")){
                json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm");
            }else{
                json = JSON.toJSONString(object);
            }
            // String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
            servletActionContext.setContentType("text/html;charset=utf-8");
            servletActionContext.getWriter().write(json);
            servletActionContext.getWriter().flush();
            servletActionContext.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
