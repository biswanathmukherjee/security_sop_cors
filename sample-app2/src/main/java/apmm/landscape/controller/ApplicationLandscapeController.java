package apmm.landscape.controller;

import apmm.landscape.services.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Biswanath Mukherjee
 */
@RestController
@RequestMapping("/")
public class ApplicationLandscapeController {



	@CrossOrigin(origins = "http://localhost:8081")
	@GetMapping("/weather")
	public String weather(@RequestParam(required=false, defaultValue="Kolkata") String name) {
		System.out.println("==== Weather API Invokes ====");
		return "<html>\n" +
				"  <head>\n" +
				"    <meta charset=\"utf-8\" />\n" +
				"\n" +
				"    <meta http-equiv=\"Access-Control-Allow-Origin\" content=\"*\">\n" +
				"    <title>Weather Report: High: 25C, Low: 15C, Cloudy</title>\n" +
				"    <header name = \"Access-Control-Allow-Origin\" value = \"*\" />\n" +
				"  </head>\n" +
				"\n" +
				"  <body>\n" +
				"  </body>\n" +
				"</html>\n";
	}


    @GetMapping("/accountInfo")
    public String accountInfo(@RequestParam(required=false, defaultValue="Kolkata") String name) {
        System.out.println("==== Account Info API Invoked ====");
        return "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "\n" +
                "    <meta http-equiv=\"Access-Control-Allow-Origin\" content=\"*\">\n" +
                "    <title>Account Info</title>\n" +
                "    <header name = \"Access-Control-Allow-Origin\" value = \"*\" />\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "  </body>\n" +
                "</html>\n";
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/contactUs")
    public String contactUs(@RequestParam(required=false, defaultValue="Kolkata") String name) {
        System.out.println("==== Contact Us Invokes ====");
        return "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "\n" +
                "    <meta http-equiv=\"Access-Control-Allow-Origin\" content=\"*\">\n" +
                "    <title>Contact Us @ +44 1234 87960</title>\n" +
                "    <header name = \"Access-Control-Allow-Origin\" value = \"*\" />\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "  </body>\n" +
                "</html>\n";
    }

}
