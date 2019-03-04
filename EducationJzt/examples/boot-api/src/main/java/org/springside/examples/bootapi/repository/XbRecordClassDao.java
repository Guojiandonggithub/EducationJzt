package org.springside.examples.bootapi.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.bootapi.domain.XbRecordClass;

import java.util.List;

/**
 * 基于Spring Data JPA的Dao接口, 自动根据接口生成实现.
 * 
 * CrudRepository默认有针对实体对象的CRUD方法.
 * 
 * Spring Data JPA 还会解释新增方法名生成新方法的实现.
 */
public interface XbRecordClassDao extends PagingAndSortingRepository<XbRecordClass, String>,JpaSpecificationExecutor<XbRecordClass> {
    @Query(value = " select x.attend_id \"attendId\",\n" +
            "c.class_name \"className\",\n" +
            "s.course_name \"courseName\",\n" +
            "e.employee_name \"employeeName\",\n" +
            "t.course_type_name \"courseTypeName\",\n" +
            "x.record_time \"recordTime\"\n" +
            ",count(x.deduct_period) as periodnum\n" +
            ",count(case when x.state='0' then 0 else null end) as sknum\n" +
            ",count(case when x.state='1' then 0 else null end) as qjnum\n" +
            ",count(case when x.state='2' then 0 else null end) as kknum\n" +
            ",count(case when x.state='3' then 0 else null end) as bknum\n" +
            " from xb_record_class x  \n" +
            "left join xb_class c on c.id = x.attend_id\n" +
            "left join xb_course s on s.id = c.course_id\n" +
            "left join sys_employee e on e.id = c.teacher_id\n" +
            "left join xb_course_type t on t.id = s.course_type_id\n" +
            " where x.delete_status = '1' group by x.attend_id,c.class_name,s.course_name,e.employee_name,t.course_type_name,x.record_time limit ?1,?2",nativeQuery = true)
    List findRecordLists(int page,int pagesize);


    @Query(value = " select count(*) from (select x.attend_id  from xb_record_class x   GROUP BY x.attend_id,x.record_time)as a ",nativeQuery = true)
    int findRecordTotalCount();

    List<XbRecordClass> findByStudentId(String studentId);
}
