package org.springside.examples.bootapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.SysOrgans;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface OrgansDao extends PagingAndSortingRepository<SysOrgans, String> {

    Page<SysOrgans> findByParentId(Pageable pageable, String parentId);

    @Query(value = " select u.id,u.organ_name,u.adds,u.organ_type,u.phone1,u.phone2,u.campus_type,u.parent_id,u.lay_order from sys_organs u where u.parent_id=?1 order by u.lay_order asc ",nativeQuery = true)
    List<SysOrgans> findSysOrgansList(String parentId);
}
