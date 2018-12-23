package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbAttendClass;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.repository.XbAttendClassDao;
import org.springside.examples.bootapi.repository.XbCourseDao;

import javax.annotation.PostConstruct;
import java.util.List;
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
	public List<XbAttendClass> findXbAttendClassAll(){
		List<XbAttendClass> list =  (List)xbAttendClassDao.findAll();
		return list;
	}
	public XbAttendClass findById(String id){
		return xbAttendClassDao.findById(id);
	}
	public XbAttendClass saveXbAttendClass(XbAttendClass xbattendclass){
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
