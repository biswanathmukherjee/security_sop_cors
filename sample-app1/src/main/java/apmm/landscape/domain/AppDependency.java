package apmm.landscape.domain;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Biswanath Mukherjee
 */
@RelationshipEntity(type = "DEPENDS_ON")
public class AppDependency {

    @Id
    @GeneratedValue
	private Long id;
	private List<String> dependencies = new ArrayList<>();

	private String dependency;

	@StartNode
	private Application startApp;

	@EndNode
	private Application endApp;

	public AppDependency() {
	}

	public AppDependency(Application startApp, Application endApp) {
		this.startApp = startApp;
		this.endApp = endApp;
	}

	public Long getId() {
	    return id;
	}

	public List<String> getDependencies() {
	    return dependencies;
	}

	public Application getStartApp() {
	    return startApp;
	}

	public Application getEndApp() {
	    return endApp;
	}

    public void addDependency(String dependency) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<>();
        }
        this.dependencies.add(dependency);
    }

	public String getDependency() {
		return dependency;
	}
}