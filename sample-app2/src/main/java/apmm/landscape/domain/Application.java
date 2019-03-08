package apmm.landscape.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Biswanath Mukherjee
 */
@NodeEntity
public class Application {

	@Id
	@GeneratedValue
	private Long id;
	private String AppName;
	private String brand;

	public String getAppName() {
		return AppName;
	}


	public String getBrand() {
		return brand;
	}


//	@JsonIgnoreProperties("application")
	@Relationship(type = "DEPENDS_ON", direction = Relationship.OUTGOING)
	private List<AppDependency> dependencies;

	public Application() {
	}

	public Application(String appName, String brand) {
		this.AppName = appName;
		this.brand = brand;
	}

	public Long getId() {
		return id;
	}


	public List<AppDependency> getDependencies() {
		return dependencies;
	}

	public void addDependency(AppDependency dependency) {
		if (this.dependencies == null) {
			this.dependencies = new ArrayList<>();
		}
		this.dependencies.add(dependency);
	}
}