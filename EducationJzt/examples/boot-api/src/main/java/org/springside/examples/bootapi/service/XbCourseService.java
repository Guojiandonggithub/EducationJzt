package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.org.apache.xpath.internal.objects.XBooleanStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.repository.EmployeeDao;
import org.springside.examples.bootapi.repository.Result;
import org.springside.examples.bootapi.repository.XbCourseDao;
import org.springside.examples.bootapi.service.exception.ErrorCode;
import org.springside.examples.bootapi.service.exception.ServiceException;
import org.springside.modules.utils.misc.IdGenerator;
import org.springside.modules.utils.text.EncodeUtil;
import org.springside.modules.utils.text.HashUtil;

import javax.annotation.PostConstruct;
import javax.naming.spi.DirStateFactory;
import java.util.List;
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

}
