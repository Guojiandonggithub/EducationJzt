package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbCourseType;
import org.springside.examples.bootapi.domain.XbSubject;
import org.springside.examples.bootapi.repository.XbCourseTypeDao;
import org.springside.examples.bootapi.repository.XbSubjectDao;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class XbSubjectService {

	private static Logger logger = LoggerFactory.getLogger(XbSubjectService.class);

	@Autowired
	private XbSubjectDao xbSubjectDao;

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
	public List<XbSubject> findSubjectAll(){
		List<XbSubject> list =  (List)xbSubjectDao.findAll();
		return list;
	}
	public XbSubject saveXbSubject(XbSubject xbsubject){
		return xbSubjectDao.save(xbsubject);
	}
    public XbSubject findById(String id){
         return xbSubjectDao.findById(id);
    }

    public boolean removeXbSubject(XbSubject xbsubject){
		try {
			xbSubjectDao.delete(xbsubject);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
