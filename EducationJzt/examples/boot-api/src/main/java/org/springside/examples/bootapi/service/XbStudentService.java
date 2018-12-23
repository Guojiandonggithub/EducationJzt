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
import org.springside.examples.bootapi.domain.XbClass;
import org.springside.examples.bootapi.domain.XbClassroom;
import org.springside.examples.bootapi.repository.XbClassDao;
import org.springside.examples.bootapi.repository.XbClassroomDao;

import java.util.Map;

@Service
public class XbStudentService {

	private static Logger logger = LoggerFactory.getLogger(XbStudentService.class);

	@Autowired
	private XbClassroomDao xbClassroomDao;
	@Autowired
	private XbClassDao xbClassDao;

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
		xbClassroomDao.delete(id);
	}

	@Transactional
	public XbClass getXbClass(String id) {
		return xbClassDao.findOne(id);
	}

}
