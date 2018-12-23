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
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.domain.XbCoursePreset;
import org.springside.examples.bootapi.repository.XbCourseDao;
import org.springside.examples.bootapi.repository.XbCoursePresetDao;

import javax.annotation.PostConstruct;
import java.util.List;
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

	public boolean  removeXbCoursePreset(XbCoursePreset xbcoursepreset){
		try {
			xbCoursePresetDao.delete(xbcoursepreset);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
