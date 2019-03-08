package apmm.landscape.services;

import apmm.landscape.domain.Application;
import apmm.landscape.repositories.ApplicationRepository;
import apmm.landscape.domain.AppDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }


    private Map<String, Object> toD3FormatV2(Collection<Application> applications) {
        List<Application> appList = new ArrayList(applications);
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();

        Iterator<Application> result = appList.iterator();
        while (result.hasNext()) {
            Application application = result.next();
            printNework(application);
            int influence = 10+2*(application.getDependencies()!=null?application.getDependencies().size():0);
            int zone = application.getDependencies()!=null?application.getDependencies().size():0;
            Map<String, Object> appNode = map("appName", application.getAppName(), "label", "Application", "brand",application.getBrand(),"influence",influence,"zone", zone) ;
            nodes.add(appNode);


        }
        Iterator<Application> result2 = appList.iterator();
        while (result2.hasNext()) {
            Application application = result2.next();

            if(application.getDependencies() !=null) {
                for (AppDependency dependency : application.getDependencies()) {
                    int source = appList.indexOf(application);
                    int target = appList.indexOf(dependency.getEndApp());
                    log.info("Source = " + source + " ------ target = " + target + " Dependency = " + dependency.getDependency());
                    rels.add(map("source", source, "target", target, "dependency", dependency.getDependency()));

                }
            }

        }

        return map("nodes", nodes, "links", rels);
    }

    private void printNework(Application application) {
        log.info("============================================");
        log.info("App Name = " + application.getAppName() + " App Id = " + application.getId());
        if(application.getDependencies() !=null) {
            for (AppDependency dependency : application.getDependencies()) {
                log.info(dependency.getStartApp().getAppName() + " --------- Id: " + dependency.getId() + ", Dependency: " + dependency.getDependency() +" ---------> " + dependency.getEndApp().getAppName());
            }
        }
        else{
            log.info("This application has no dependency");
        }
        log.info("============================================");

    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        result.put(key4, value4);
        result.put(key5, value5);

        return result;
    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        return result;
    }

    @Transactional(readOnly = true)
    public Application findByAppName(String appName) {
        Application result = applicationRepository.findByAppName(appName);
        return result;
    }

    @Transactional(readOnly = true)
    public Collection<Application> findByAppNameLike(String appName) {
        log.info("------------- appName = " + appName);
        Collection<Application> result = applicationRepository.findByAppNameLike(appName);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> graph(int limit) {
        Collection<Application> result = applicationRepository.graph(limit);
        return toD3FormatV2(result);
    }

    @Transactional(readOnly = true)
    public Map<String,Object> context(String appName, int limit) {
        Collection<Application> result = applicationRepository.context(appName, limit);
        return toD3FormatV2(result);
    }
}
