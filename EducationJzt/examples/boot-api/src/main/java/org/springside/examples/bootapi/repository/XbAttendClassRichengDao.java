package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.XbAttendClass;
import org.springside.examples.bootapi.domain.XbAttendClassRicheng;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface XbAttendClassRichengDao extends PagingAndSortingRepository<XbAttendClassRicheng, String>,JpaSpecificationExecutor<XbAttendClassRicheng> {

    @Query(value="SELECT * FROM xb_attend_class_richeng t WHERE t.start_date_time>=?1 AND t.start_date_time<?2 AND t.delete_status = '1' ",nativeQuery = true)
    List<XbAttendClassRicheng> findXbAttendListRiCheng(String start, String end);
}
