package apmm.landscape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * @author Biswanath Mukherjee
 */
@SpringBootApplication
@EnableNeo4jRepositories("apmm.landscape.repositories")
public class LandscapeUIApplication {

    public static void main(String[] args) {
        SpringApplication.run(LandscapeUIApplication.class, args);
    }
}