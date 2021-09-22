package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "<html><body><h1>Hello</h1><button>Click Me</button</body><!html>";
	}

	@GetMapping("/a")
	public String pagea() {
		return "A: Greetings from Spring Boot!";
	}
	@GetMapping("/b")
	public String pageb() {
		return "B: Greetings from Spring Boot!";
	}

	@PostMapping("/postbody")
	public String postBody(@RequestBody String fullName) {
		System.out.println("Posting");
		return "Hello " + fullName+"\r\n";
	}

}
