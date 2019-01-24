package org.springside.examples.bootapi.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.domain.XbRecordClass;

import java.util.HashMap;
import java.util.Map;

/***
 * 首页
 */
@Controller
@RequestMapping(value = "/wechatLogin")
public class WechatLoginActivity {
	/*
	 * 查询按学员列表
	 * @return
	 */
	@RequestMapping("/toindex")
	public String toIndex(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		return "wechat_index";
	}
}
