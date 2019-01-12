package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.domain.XbAttendClass;
import org.springside.examples.bootapi.domain.XbCoursePreset;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface XbAttendClassDao extends PagingAndSortingRepository<XbAttendClass, String>,JpaSpecificationExecutor<XbAttendClass> {
    XbAttendClass findById(String id);
    @Query(value="SELECT * FROM xb_attend_class a WHERE (a.id) IN  (SELECT t.id FROM xb_attend_class t WHERE t.delete_status = 1 GROUP BY t.class_id,t.start_date_time,t.time_interval HAVING COUNT(*) > 1)",nativeQuery = true)
    List<XbAttendClass> findXbAttendConflictList();
    /*@Query(value="SELECT t.id FROM xb_attend_class t WHERE t.delete_status = 1 GROUP BY t.class_id,t.start_date_time,t.time_interval HAVING COUNT(*) > 1",nativeQuery = true)
    List<String> findXbAttendConflictIdList();*/
    @Query(value="SELECT t.id FROM xb_attend_class t\n" +
            "INNER JOIN \n" +
            "(SELECT t.class_room_id,t.start_date_time,t.time_interval FROM xb_attend_class t \n" +
            "WHERE t.delete_status = 1 GROUP BY t.class_room_id,t.start_date_time,t.time_interval HAVING COUNT(*) > 1) t1\n" +
            "ON (t.class_room_id = t1.class_room_id AND t.start_date_time = t1.start_date_time AND t.time_interval = t1.time_interval)",nativeQuery = true)
    List<String> findXbAttendConflictIdList();
    @Query(value="SELECT *   FROM xb_record_class t WHERE t.attend_id = ? AND t.record_time = ?",nativeQuery = true)
    List findYDStudentNum(String classId,String startDateTime);
    @Query(value="SELECT *  FROM xb_record_class t WHERE t.attend_id = ? AND t.record_time = ? AND t.state = '0';",nativeQuery = true)
    List findSDStudentNum(String classId,String startDateTime);

}
