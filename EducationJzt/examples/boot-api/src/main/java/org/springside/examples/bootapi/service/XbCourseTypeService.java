package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.domain.XbCourseType;
import org.springside.examples.bootapi.repository.XbCourseDao;
import org.springside.examples.bootapi.repository.XbCourseTypeDao;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class XbCourseTypeService {

	private static Logger logger = LoggerFactory.getLogger(XbCourseTypeService.class);

	@Autowired
	private XbCourseTypeDao xbCourseTypeDao;

	// 注入配置值
	@Value("${app.loginTimeoutSecs:600}")
	private int loginTimeoutSecs;

	// codehale metrics
	@Autowired
	private CounterService counterService;

	// guava cache
	private Cache<String, SysEmployee> loginUsers;

	@PostConstruct
	public void init() {
		loginUsers = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(loginTimeoutSecs, TimeUnit.SECONDS)
				.build();
	}
	@Transactional(readOnly = true)
	public List<XbCourseType> findCourseTypeAll(){
		List<XbCourseType> list =  (List)xbCourseTypeDao.findAllByOrderByLayOrderAsc();
		return list;
	}
    public XbCourseType saveXbCourseType(XbCourseType xbcoursetype){
         return xbCourseTypeDao.save(xbcoursetype);
    }

    public boolean removeXbCourseType(XbCourseType xbcoursetype){
		try {
			xbCourseTypeDao.delete(xbcoursetype);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
