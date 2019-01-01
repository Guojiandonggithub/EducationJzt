package org.springside.examples.bootapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.repository.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class XbStudentService {

	private static Logger logger = LoggerFactory.getLogger(XbStudentService.class);
	@Autowired
	private XbStudentDao studentDao;
	@Autowired
	private XbStudentRelationDao xbStudentRelationDao;
	@Autowired
	private XbClassroomDao xbClassroomDao;
	@Autowired
	private XbClassDao xbClassDao;
	@Autowired
	private XbSupplementFeeDao xbSupplementFeeDao;

	@Transactional
	public Page<XbSupplementFee>  getXbSupplementFeeList(Pageable pageable, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbSupplementFee> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbSupplementFee.class);
		return xbSupplementFeeDao.findAll(spec,pageable);
	}

	@Transactional
	public XbSupplementFee saveXbSupplementFee(XbSupplementFee xbSupplementFee) {
		xbSupplementFee.paymentDate = new Date();
		xbSupplementFee.orderNumber = UUID.randomUUID().toString();
		return xbSupplementFeeDao.save(xbSupplementFee);
	}

	@Transactional
	public Page<XbStudent>  getXbStudentList(Pageable pageable, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbStudent> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbStudent.class);
		return studentDao.findAll(spec,pageable);
	}


	@Transactional
	public Page<XbStudentRelation>  getXbStudentRelationList(Pageable pageable, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbStudentRelation> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbStudentRelation.class);
		return xbStudentRelationDao.findAll(spec,pageable);
	}

	@Transactional
	public XbStudent saveXbStudent(XbStudent student) {
		return studentDao.save(student);
	}

	@Transactional
	public XbStudentRelation saveXbStudentRelation(XbStudentRelation studentRelation) {
		studentRelation.enrollDate = new Date();
		return xbStudentRelationDao.save(studentRelation);
	}

	@Transactional
	public void deleteXbStudent(String id) {
		studentDao.delete(id);
	}

	@Transactional
	public XbStudent getXbStudent(String id) {
		return studentDao.findOne(id);
	}





	@Transactional
	public Page<XbClassroom>  getXbClassroomList(Pageable pageable,Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbClassroom> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbClassroom.class);
		return xbClassroomDao.findAll(spec,pageable);
	}

	@Transactional
	public Page<XbClass>  getXbClassList(Pageable pageable,Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<XbClass> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), XbClass.class);
		return xbClassDao.findAll(spec,pageable);
	}
	@Transactional
	public List<XbClassroom> findXbClassRoomListAll(){
		return (List<XbClassroom>)xbClassroomDao.findAll();
	}
	@Transactional
	public List<XbClass> findXbClassListAll(){
		return (List<XbClass>)xbClassDao.findAll();
	}
	@Transactional
	public XbClassroom saveXbClassroom(XbClassroom classroom) {
		return xbClassroomDao.save(classroom);
	}

	@Transactional
	public void delete(String id) {
		xbClassroomDao.delete(id);
	}

	@Transactional
	public XbClassroom getXbClassroom(String id) {
		return xbClassroomDao.findOne(id);
	}

	@Transactional
	public XbClass saveXbClass(XbClass classes) {
		return xbClassDao.save(classes);
	}

	@Transactional
	public void deleteClass(String id) {
		xbClassDao.delete(id);
	}

	@Transactional
	public XbClass getXbClass(String id) {
		return xbClassDao.findOne(id);
	}
	@Transactional
	public XbClass getXbClassById(String id){
		return xbClassDao.getById(id);
	}
	@Transactional
	public XbClassroom getXbClassroomById(String id){
		return xbClassroomDao.getById(id);
	}
}
