package org.springside.examples.bootapi.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysEmployeeSub;
import org.springside.examples.bootapi.domain.XbClassroom;
import org.springside.examples.bootapi.repository.EmployeeDao;
import org.springside.examples.bootapi.repository.EmployeeSubDao;
import org.springside.examples.bootapi.service.exception.ErrorCode;
import org.springside.examples.bootapi.service.exception.ServiceException;
import org.springside.modules.utils.misc.IdGenerator;
import org.springside.modules.utils.text.EncodeUtil;
import org.springside.modules.utils.text.HashUtil;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {

	private static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeDao employeeDao;

	// 注入配置值
	@Value("${app.loginTimeoutSecs:600}")
	private int loginTimeoutSecs;

	// codehale metrics
	@Autowired
	private CounterService counterService;

	@Autowired
	private EmployeeSubDao employeeSubDao;

	// guava cache
	private Cache<String, SysEmployee> loginUsers;

	@PostConstruct
	public void init() {
		loginUsers = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(loginTimeoutSecs, TimeUnit.SECONDS)
				.build();
	}

	@Transactional(readOnly = true)
	public SysEmployee login(String username, String password) {
		SysEmployee SysEmployee = employeeDao.findByUserName(username);

		if (SysEmployee == null) {
			throw new ServiceException("用户不存在", ErrorCode.UNAUTHORIZED);
		}

		if (!SysEmployee.password.equals(hashPassword(password))) {
			System.out.println(hashPassword(password));
			throw new ServiceException("密码错误", ErrorCode.UNAUTHORIZED);
		}

		String token = IdGenerator.uuid2();
		loginUsers.put("qnjl-mylove-forevery", SysEmployee);
		loginUsers.put(token, SysEmployee);
		loginUsers.put("employee", SysEmployee);
		counterService.increment("loginUser");
		return SysEmployee;
	}

	public void logout(String token) {
		SysEmployee SysEmployee = loginUsers.getIfPresent(token);
		if (SysEmployee == null) {
			logger.warn("logout an alreay logout token:" + token);
		} else {
			loginUsers.invalidate(token);
			counterService.decrement("loginUser");
		}
	}

	public SysEmployee getLoginUser(String token) {

		SysEmployee SysEmployee = loginUsers.getIfPresent(token);

		if (SysEmployee == null) {
			throw new ServiceException("用户没登录", ErrorCode.UNAUTHORIZED);
		}

		return SysEmployee;
	}

	@Transactional
	public SysEmployee register(SysEmployee sysEmployee) {
		SysEmployee sysEmployees = employeeDao.findByUserName(sysEmployee.userName);
		if(null!=sysEmployees){
			if(!sysEmployees.password.equals(sysEmployee.password)){
				sysEmployee.password = hashPassword(sysEmployee.password);
			}
		}else{
			sysEmployee.password = hashPassword(sysEmployee.password);
		}
		return employeeDao.save(sysEmployee);
	}

	@Transactional
	public SysEmployeeSub registerSub(SysEmployeeSub sysEmployeeSub) {
		return employeeSubDao.save(sysEmployeeSub);
	}

	@Transactional
	public Page<SysEmployee>  getAccountList(Pageable pageable,Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SysEmployee> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SysEmployee.class);
		return employeeDao.findAll(spec,pageable);
	}
	@Transactional
	public List<SysEmployee> findSysEmployeeListAll(){
		return (List<SysEmployee>)employeeDao.findAll();
	}
	@Transactional
	public SysEmployee getSysEmployee(String id) {
		return employeeDao.findOne(id);
	}

	@Transactional
	public SysEmployeeSub getSysEmployeeSub(String id) {
		return employeeSubDao.findByUserId(id);
	}

	@Transactional
	public void delete(String id) {
		employeeDao.delete(id);
		SysEmployeeSub sysEmployeeSub = employeeSubDao.findByUserId(id);
		if(null!=sysEmployeeSub){
			employeeSubDao.delete(sysEmployeeSub);
		}
	}

	protected static String hashPassword(String password) {
		return EncodeUtil.encodeBase64(HashUtil.sha1(password));
	}
	@Transactional
	public SysEmployee getSysEmployeeById(String id){
		return employeeDao.getById(id);
	}
}
