package org.springside.examples.bootapi.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.SysEmployee;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface EmployeeDao extends PagingAndSortingRepository<SysEmployee, String>,JpaSpecificationExecutor<SysEmployee> {

	List<SysEmployee> findByUserName(String userName);
	List<SysEmployee> findByMobilePhone(String userName);
	SysEmployee getById(String id);
	List<SysEmployee> findByDeleteStatus(String deletestatus);
}
