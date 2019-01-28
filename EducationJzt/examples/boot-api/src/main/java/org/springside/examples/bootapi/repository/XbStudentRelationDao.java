package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.domain.XbStudentRelation;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface XbStudentRelationDao extends PagingAndSortingRepository<XbStudentRelation, String>,JpaSpecificationExecutor<XbStudentRelation> {
    @Query(value = " select count(*) from xb_student_relation r where r.class_id = ?1 ",nativeQuery = true)
    public Long findAllDataByClassCount(String classId);
}
