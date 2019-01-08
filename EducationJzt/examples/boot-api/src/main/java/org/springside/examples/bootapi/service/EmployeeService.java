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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysEmployeeSub;
import org.springside.examples.bootapi.repository.EmployeeDao;
import org.springside.examples.bootapi.repository.EmployeeSubDao;
import org.springside.examples.bootapi.service.exception.ErrorCode;
import org.springside.examples.bootapi.service.exception.ServiceException;
import org.springside.modules.utils.text.EncodeUtil;
import org.springside.modules.utils.text.HashUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
	public String login(String username, String password,HttpServletRequest request) {
		SysEmployee sysEmployee = employeeDao.findByUserName(username);

		if (sysEmployee == null) {
			return "用户不存在";
			//throw new ServiceException("用户不存在", ErrorCode.UNAUTHORIZED);
		}

		if (!sysEmployee.password.equals(hashPassword(password))) {
			System.out.println(hashPassword(password));
			//throw new ServiceException("密码错误", ErrorCode.UNAUTHORIZED);
			return "密码错误";
		}

		if (sysEmployee.isAllow!=1) {
			return "您不允许登录,请联系管理员";
		}

		//String token = IdGenerator.uuid2();
		//counterService.increment("loginUser");
		request.getSession().setAttribute("sysEmployee", sysEmployee);
		return "登录成功";
	}

	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute("sysEmployee", null);
		return "login";
	}

	public SysEmployee checkUserName(String username) {
		SysEmployee sysEmployee = employeeDao.findByUserName(username);
		return sysEmployee;
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
		if(null!=sysEmployee.id&&!"".equals(sysEmployee.id)){
			SysEmployee sysEmployees = employeeDao.findOne(sysEmployee.id);
			if(!sysEmployees.password.equals(sysEmployee.password)){
				sysEmployee.password = hashPassword(sysEmployee.password);
			}
		}else{
			sysEmployee.password = hashPassword(sysEmployee.password);
		}
		sysEmployee.deleteStatus = "1";
		return employeeDao.save(sysEmployee);
	}

	@Transactional
	public SysEmployeeSub registerSub(SysEmployeeSub sysEmployeeSub) {
		return employeeSubDao.save(sysEmployeeSub);
	}

	@Transactional
	public Page<SysEmployee>  getAccountList(Pageable pageable,Map<String, Object> searchParams) {
		searchParams = HttpServletUtil.getRoleDate(searchParams);
		searchParams.put("EQ_deleteStatus","1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SysEmployee> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), SysEmployee.class);
		return employeeDao.findAll(spec,pageable);
	}
	@Transactional
	public List<SysEmployee> findSysEmployeeListAll(){
		return employeeDao.findByDeleteStatus("1");
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
		SysEmployee sysEmployee = employeeDao.findOne(id);
		sysEmployee.deleteStatus="0";
		employeeDao.save(sysEmployee);
		SysEmployeeSub sysEmployeeSub = employeeSubDao.findByUserId(id);
		if(null!=sysEmployeeSub){
			sysEmployeeSub.deleteStatus = "0";
			employeeSubDao.save(sysEmployeeSub);
			//employeeSubDao.delete(sysEmployeeSub);
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
