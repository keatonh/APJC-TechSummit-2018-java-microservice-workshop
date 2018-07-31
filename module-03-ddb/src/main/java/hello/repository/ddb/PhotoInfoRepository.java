package hello.repository.ddb;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import hello.model.ddb.PhotoInfo;

/* DyanmoDB Repository*/
@EnableScan
public interface PhotoInfoRepository extends CrudRepository<PhotoInfo, String> {
	List<PhotoInfo> findAll();
}

// public interface PhotoInfoRepository extends PagingAndSortingRepository<, String> {

//  	@EnableScan
// 	List<PhotoInfo> findById(String id);
// //	@EnableScan 
// //	List<PhotoInfo> findByImg_id(String img_id);	
//   @EnableScan 
// 	List<PhotoInfo> findAll();
// 	@EnableScan 
// 	void deleteAll();
// }