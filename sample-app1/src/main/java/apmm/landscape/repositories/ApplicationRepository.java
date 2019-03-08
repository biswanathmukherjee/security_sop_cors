package apmm.landscape.repositories;

import apmm.landscape.domain.Application;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * @author Biswanath Mukherjee
 */
@RepositoryRestResource(collectionResourceRel = "applications", path = "applications")
public interface ApplicationRepository extends Neo4jRepository<Application, Long> {

	Application findByAppName(@Param("AppName") String appName);

	Collection<Application> findByAppNameLike(@Param("AppName") String appName);


    @Query("MATCH (startApp:Application)<-[R]-(endApp:Application) RETURN startApp,R,endApp LIMIT {limit}")
	Collection<Application> graph(@Param("limit") int limit);

    @Query("MATCH (startApp:Application)<-[R]-(endApp:Application{AppName:{0}}) RETURN startApp,R,endApp LIMIT {1}")
	Collection<Application> context(String appName, int limit);
}