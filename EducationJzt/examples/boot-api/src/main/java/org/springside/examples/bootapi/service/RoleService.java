package org.springside.examples.bootapi.service;

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
import org.springside.examples.bootapi.domain.SysRole;
import org.springside.examples.bootapi.repository.SysRoleDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

	private static Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private SysRoleDao roleDao;

	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute("sysEmployee", null);
		return "login";
	}

	@Transactional
	public SysRole saveRole(SysRole sysRole) {
		sysRole.deleteStatus = "1";
		return roleDao.save(sysRole);
	}

	@Transactional
	public Page<SysRole>  getRoleList(Pageable pageable,Map<String, Object> searchParams) {
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SysRole> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SysRole.class);
		return roleDao.findAll(spec,pageable);
	}

	@Transactional
	public List<SysRole> getRoleListPage(Map<String, Object> searchParams) {
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SysRole> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SysRole.class);
		return roleDao.findAll(spec);
	}

	@Transactional
	public SysRole getSysRole(String id) {
		return roleDao.findOne(id);
	}


	@Transactional
	public void delete(String id) {
		SysRole sysRole = roleDao.findOne(id);
		sysRole.deleteStatus="0";
		roleDao.save(sysRole);
	}
}
