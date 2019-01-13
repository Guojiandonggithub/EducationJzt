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
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.repository.XbCourseDao;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class XbCourseService {

	private static Logger logger = LoggerFactory.getLogger(XbCourseService.class);

	@Autowired
	private XbCourseDao xbCourseDao;

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
	public List<XbCourse> findCourseAll(){
		List<XbCourse> list =  (List)xbCourseDao.findAll();
		return list;
	}
	public XbCourse findById(String id){
		return xbCourseDao.findById(id);
	}
	public XbCourse saveXbCourse(XbCourse xbcourse){
		return xbCourseDao.save(xbcourse);
	}

	public boolean  removeXbCourse(XbCourse xbcourse){
		try {
			xbCourseDao.delete(xbcourse);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Transactional
	public Page<XbCourse> getXbCourseList(Pageable pageable, Map<String, Object> searchParams) {
        searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbCourse> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbCourse.class);
		return xbCourseDao.findAll(spec,pageable);
	}

	@Transactional
	public List<XbCourse> getXbCourseAllList(Map<String, Object> searchParams) {
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbCourse> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbCourse.class);
		return xbCourseDao.findAll(spec);
	}

	@Transactional(readOnly = true)
	public List<XbCourse> findCourseList(String organId){
		List<XbCourse> list =  (List)xbCourseDao.findByOrganIds(organId);
		return list;
	}

	@Transactional(readOnly = true)
	public List<XbCourse> findAllDataByOrganId(String organId){
		List<XbCourse> list =  (List)xbCourseDao.findAllDataByOrganId(organId);
		return list;
	}

}
