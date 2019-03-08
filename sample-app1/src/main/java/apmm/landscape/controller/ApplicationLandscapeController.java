package apmm.landscape.controller;

import apmm.landscape.services.ApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Biswanath Mukherjee
 */
@RestController
@RequestMapping("/")
public class ApplicationLandscapeController {

	private final ApplicationService applicationService;

	public ApplicationLandscapeController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

    @GetMapping("/appgraph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return applicationService.graph(limit == null ? 10000 : limit);
	}

	@GetMapping("/context")
	public Map<String, Object> context(@RequestParam(value = "appName",required = true) String appName, @RequestParam(value = "limit",required = false) Integer limit) {
		return applicationService.context(appName, limit == null ? 1000 : limit);
	}
}
