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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.repository.OrgansDao;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		organs.deleteStatus = "1";
		return organsDao.save(organs);
	}

	@Transactional
	public void delete(String id) {
		SysOrgans sysOrgans = organsDao.findOne(id);
		sysOrgans.deleteStatus = "0";
		organsDao.save(sysOrgans);
		//organsDao.delete(id);
	}

	@Transactional
	public Page<SysOrgans>  getOrgansList(Pageable pageable) {
		Map<String, Object> searchParams = new HashMap<>();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		if(!"超级管理员".equals(sysEmployee.sysRole.roleName)&&!"总校教务".equals(sysEmployee.sysRole.roleName)){
			searchParams.put("EQ_id",sysEmployee.organId);
		}
		searchParams.put("EQ_parentId","1");
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SysOrgans> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SysOrgans.class);
		return organsDao.findAll(spec,pageable);
		//return organsDao.findByParentIdAndDeleteStatus(pageable,"1","1");
	}

	@Transactional
	public List<SysOrgans> getOrgansList() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		/*if(!"超级管理员".equals(sysEmployee.sysRole.roleName)&&!"总校教务".equals(sysEmployee.sysRole.roleName)){*/
		if(!"超级管理员".equals(sysEmployee.sysRole.roleName)&&!"总校教务".equals(sysEmployee.sysRole.roleName)){
			return organsDao.findSysOrgansListById("1",sysEmployee.organId);
		}
		return organsDao.findSysOrgansList("1");
	}

	public List<SysOrgans> getOrgansListAll(Map<String,Object> searchParams){
		searchParams.put("EQ_deleteStatus","1");
		searchParams.put("EQ_parentId","1");
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		if(!"超级管理员".equals(sysEmployee.sysRole.roleName)&&!"总校教务".equals(sysEmployee.sysRole.roleName)){
			searchParams.put("EQ_id",sysEmployee.organId);
		}
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SysOrgans> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SysOrgans.class);
		return organsDao.findAll(spec);
	};

	public SysOrgans checkOrganName(String name){return organsDao.findAllByOrganName(name);};
	public SysOrgans findOrganbyId(String id){return organsDao.findById(id);};

}
