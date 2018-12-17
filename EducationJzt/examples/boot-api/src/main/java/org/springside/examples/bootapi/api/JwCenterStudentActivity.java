package org.springside.examples.bootapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 教务中心-学员
 * Created by ZhangLei on 2018/12/15 0015
 */
@Controller
@RequestMapping(value="/jwcenterstudent")
public class JwCenterStudentActivity {
    private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);

    /**
     * 跳转到学员列表
     * @return
     */
    @RequestMapping("/tostudent")
    public String toStudent(){
        logger.info("跳转到学生");
        return "student";
    }
}
