package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbAttendClass;
import org.springside.examples.bootapi.repository.XbAttendClassDao;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class XbAttendClassService {

	private static Logger logger = LoggerFactory.getLogger(XbAttendClassService.class);
	@Autowired
	private XbAttendClassDao xbAttendClassDao;

	// 注入配置值
	@Value("${app.loginTimeoutSecs:600}")
	private int loginTimeoutSecs;

	private Cache<String, SysEmployee> loginUsers;

	@PostConstruct
	public void init() {
		loginUsers = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(loginTimeoutSecs, TimeUnit.SECONDS)
				.build();
	}

	@Transactional(readOnly = true)
	public List<XbAttendClass> findXbAttendClassAll(Map<String, Object> searchParams){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		if("管理员".equals(sysEmployee.sysRole.roleName)){
			searchParams.put("EQ_xbclass.organId",sysEmployee.organId);
		}
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbAttendClass> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbAttendClass.class);
		return xbAttendClassDao.findAll(spec);
	}

	@Transactional(readOnly = true)
	public Page<XbAttendClass> findXbAttendClassPageAll( Pageable pageable,Map<String, Object> searchParams){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		if("管理员".equals(sysEmployee.sysRole.roleName)){
			searchParams.put("EQ_xbclass.organId",sysEmployee.organId);
		}
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbAttendClass> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbAttendClass.class);
		return xbAttendClassDao.findAll(spec,pageable);
	}

	public List<XbAttendClass> findXbAttendConflictList(){
		return xbAttendClassDao.findXbAttendConflictList();
	}

	public XbAttendClass findById(String id){
		return xbAttendClassDao.findOne(id);
	}

	public XbAttendClass saveXbAttendClass(XbAttendClass xbattendclass){
		//xbattendclass.setCreateUserId(loginUsers.getIfPresent("qnjl-mylove-forevery").id);//添加创建人
		xbattendclass.setCreateUserId("4028818367cc62780167cc695f490000");//添加创建人
		xbattendclass.setCreateDate(DateUtil.getTodayDateStr());
		xbattendclass.createTime = DateUtil.getTodayDateTimeStr();
		return xbAttendClassDao.save(xbattendclass);
	}

	public boolean  removeXbAttendClass(XbAttendClass xbattendclass){
		try {
			xbAttendClassDao.delete(xbattendclass);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
