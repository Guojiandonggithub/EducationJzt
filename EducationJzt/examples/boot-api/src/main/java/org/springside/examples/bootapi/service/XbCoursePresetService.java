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
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbCoursePreset;
import org.springside.examples.bootapi.repository.XbCoursePresetDao;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class XbCoursePresetService {

	private static Logger logger = LoggerFactory.getLogger(XbCoursePresetService.class);

	@Autowired
	private XbCoursePresetDao xbCoursePresetDao;

	// 注入配置值
	@Value("${app.loginTimeoutSecs:600}")
	private int loginTimeoutSecs;
	// guava cache
	private Cache<String, SysEmployee> loginUsers;

	@PostConstruct
	public void init() {
		loginUsers = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(loginTimeoutSecs, TimeUnit.SECONDS)
				.build();
	}
	public XbCoursePreset saveXbCoursePreset(XbCoursePreset xbcoursepreset){
		return xbCoursePresetDao.save(xbcoursepreset);
	}
	public List<XbCoursePreset> findXbCoursePresetListByCourseid(String courseid){
		return xbCoursePresetDao.findByCourseId(courseid);
	}
	public boolean  removeXbCoursePreset(XbCoursePreset xbcoursepreset){
		try {
			xbCoursePresetDao.delete(xbcoursepreset);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Transactional
	public Page<XbCoursePreset> getXbCoursePresetList(Pageable pageable, Map<String, Object> searchParams) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		if("管理员".equals(sysEmployee.sysRole.roleName)){
			searchParams.put("EQ_organIds",sysEmployee.organId);
		}
		searchParams.put("EQ_xbCourse.deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbCoursePreset> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbCoursePreset.class);
		return xbCoursePresetDao.findAll(spec,pageable);
	}

	@Transactional
	public List<XbCoursePreset> getXbCoursePresets(Map<String, Object> searchParams) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		if("管理员".equals(sysEmployee.sysRole.roleName)){
			searchParams.put("EQ_organIds",sysEmployee.organId);
		}
		searchParams.put("EQ_xbCourse.deleteStatus","1");
		searchParams.put("EQ_xbCourse.state","0");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbCoursePreset> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbCoursePreset.class);
		return xbCoursePresetDao.findAll(spec);
	}

	public XbCoursePreset getXbCoursePreset(String id){
		return xbCoursePresetDao.findOne(id);
	}
	public List<XbCoursePreset> getXbCourseListByOrgid(String orgid){
		return xbCoursePresetDao.findCourseListByOrginid(orgid);
	}
}
