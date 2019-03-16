package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.XbStudentRelation;

import java.util.Date;
import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface XbStudentRelationDao extends PagingAndSortingRepository<XbStudentRelation, String>,JpaSpecificationExecutor<XbStudentRelation> {
    @Query(value = " select count(*) from xb_student_relation r where r.class_id = ?1 and r.student_start!=1 and r.student_start!=3 and r.student_start!=4 ",nativeQuery = true)
    public Long findAllDataByClassCount(String classId);
    @Query(value="SELECT * FROM xb_student_relation o WHERE o.enroll_date = ?1 GROUP BY o.student_id",nativeQuery = true)
    List findAllStudentList(Date enroll_date);
    @Query(value="SELECT * FROM xb_student_relation o WHERE o.enroll_date >= ?1 AND o.enroll_date <= ?2 GROUP BY o.student_id",nativeQuery = true)
    List findAllStudentListStartAndEnd(Date enrollStart,Date enrollEnd);
    @Query(value="SELECT * FROM xb_student_relation o  GROUP BY o.student_id",nativeQuery = true)
    List findAllStudentListNoDate();

    List<XbStudentRelation> findByStudentId(String studentId);

}
