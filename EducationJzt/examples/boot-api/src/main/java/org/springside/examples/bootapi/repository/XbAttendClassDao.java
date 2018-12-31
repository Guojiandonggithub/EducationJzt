package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.domain.XbAttendClass;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface XbAttendClassDao extends PagingAndSortingRepository<XbAttendClass, String> {
    XbAttendClass findById(String id);
    @Query(value="SELECT * FROM xb_attend_class a WHERE (a.id) IN  (SELECT t.id FROM xb_attend_class t GROUP BY t.class_id,t.start_date_time,t.time_interval HAVING COUNT(*) > 1)",nativeQuery = true)
    List<XbAttendClass> findXbAttendConflictList();
}
