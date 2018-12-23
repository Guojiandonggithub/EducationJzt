package org.springside.examples.bootapi.ToolUtils;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
}
