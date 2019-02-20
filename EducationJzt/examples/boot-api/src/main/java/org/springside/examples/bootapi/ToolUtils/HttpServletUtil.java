package org.springside.examples.bootapi.ToolUtils;

import org.json.simple.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.domain.SysEmployee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by ZhangLei on 2018/12/22 0022
 */
public class HttpServletUtil {
    /**
     * ajax回调处理
     * @param jsonObject
     * @param resp
     */
    public static void reponseWriter(JSONObject jsonObject, HttpServletResponse resp){
        resp.setContentType("text/html;charset=UTF-8");
        try {
            resp.getWriter().println(jsonObject.toJSONString());
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取管理员数据
     * @param searchParams
     */
    public static Map<String,Object> getRoleDate(Map<String,Object> searchParams){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
        if("管理员".equals(sysEmployee.sysRole.roleName)){
            searchParams.put("EQ_organId",sysEmployee.organId);
        }
        if("教师".equals(sysEmployee.sysRole.roleName)){
            searchParams.put("EQ_organId",sysEmployee.organId);
            searchParams.put("EQ_teacherId",sysEmployee.id);
        }
        return searchParams;
    }
    /**
     * 获取管理员数据
     * @param searchParams
     */
    public static Map<String,Object> getRoleDateForXbRecordClassdView(Map<String,Object> searchParams){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
        if("管理员".equals(sysEmployee.sysRole.roleName)){
            searchParams.put("EQ_orgid",sysEmployee.organId);
        }
        if("教师".equals(sysEmployee.sysRole.roleName)){
            searchParams.put("EQ_orgid",sysEmployee.organId);
            searchParams.put("EQ_teacherId",sysEmployee.id);
        }
        return searchParams;
    }
}
