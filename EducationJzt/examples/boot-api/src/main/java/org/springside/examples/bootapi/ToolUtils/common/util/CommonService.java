package org.springside.examples.bootapi.ToolUtils.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.DynamicSpecifications;
import org.springside.examples.bootapi.ToolUtils.common.modules.persistence.SearchFilter;

import java.util.Map;

public class CommonService<T> {

	@SuppressWarnings("rawtypes")
	Class entityClass = GenericSuperClass.getGenericSuperClass(this.getClass());

	/**
	 * 创建动态查询条件组合.
	 */
	@SuppressWarnings("unchecked")
	protected Specification<T> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), entityClass);
		return spec;
	}

	/**
	 * 创建动态查询条件组合.
	 * pageNumber:页码
	 * pageSize：每页数据数量
	 * sortBy：排序字段
	 * orderType：排序方式
	 */
	// 原来的
	// protected PageRequest buildPageRequest(int pageNumber, int pageSize,
	// String sortBy, String order) {
	// Sort sort = null;
	// sort = new Sort(Direction.DESC, sortBy);
	// sort = new Sort(Direction.ASC, sortBy);
	// return new PageRequest(pageNumber - 1, pageSize, sort);
	// }
	// 改过的
	protected PageRequest buildPageRequest(int pageNumber,int pageSize,
			String sortBy,String sortType){
		Sort sort = null ;
		if("auto".equals(sortType)){
			sort = new Sort(Direction.DESC,sortBy);
		}else if("asc".equals(sortType)) {
			sort = new Sort(Direction.ASC,sortBy);
		}
		return new PageRequest(pageNumber - 1,pageSize,sort);
	}
}
