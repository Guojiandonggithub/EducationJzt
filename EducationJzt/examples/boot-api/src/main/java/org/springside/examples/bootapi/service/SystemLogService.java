package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SystemLog;
import org.springside.examples.bootapi.repository.SystemLogRepository;

import java.util.Map;

@Service
public class SystemLogService {

	private static Logger logger = LoggerFactory.getLogger(SystemLogService.class);

	@Autowired
	private SystemLogRepository systemLogRepositoryDao;

	// guava cache
	private Cache<String, SysEmployee> loginUsers;

	@Transactional
	public SystemLog save(SystemLog organs) {
		return systemLogRepositoryDao.save(organs);
	}

	@Transactional
	public Page<SystemLog>  getOrgansList(Pageable pageable,Map<String,Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SystemLog> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SystemLog.class);
		return systemLogRepositoryDao.findAll(spec,pageable);
	}
}
