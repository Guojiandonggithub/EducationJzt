package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.XbCourse;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 *
         * CrudRepository默认有针对实体对象的CRUD方法.
 *
         * Spring Data JPA 还会解释新增方法名生成新方法的实现.
            */
    public interface XbCourseDao extends PagingAndSortingRepository<XbCourse, Long>,JpaSpecificationExecutor<XbCourse> {
        XbCourse findById(String id);

    @Query(value = " SELECT t.id,t.course_name,t.charging_mode,t.course_type_id,t.course_type_name,\n" +
            "t.create_date,t.create_time,t.opening_type,t.opening_types,t.period,t.pre_course_ids\n" +
            ",t.pre_course_names,t.remarks,t.state,t.subject_id,t.type,t.tuition_type,t.tuition_fee,t.subject_name FROM xb_course t " +
            "LEFT JOIN xb_course_preset t1 ON t.id= t1.course_id " +
            "LEFT JOIN sys_organs t2 ON t1.organ_ids = t2.id where t2.id=?1 ",nativeQuery = true)
        XbCourse findByOrganIds(String id);
}
