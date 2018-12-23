package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.repository.OrgansDao;

import java.util.List;

@Service
public class OrgansService {

	private static Logger logger = LoggerFactory.getLogger(OrgansService.class);

	@Autowired
	private OrgansDao organsDao;

	// guava cache
	private Cache<String, SysEmployee> loginUsers;

	@Transactional
	public SysOrgans register(SysOrgans organs) {
		organs.organType = 1;
		organs.parentId = "1";
		return organsDao.save(organs);
	}

	@Transactional
	public void delete(String id) {
		organsDao.delete(id);
	}

	@Transactional
	public Page<SysOrgans>  getOrgansList(Pageable pageable) {
		return organsDao.findByParentId(pageable,"1");
	}

	@Transactional
	public List<SysOrgans> getOrgansList() {
		return organsDao.findSysOrgansList("1");
	}
}
