package aloha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
	//home
	@GetMapping( {"/", ""} )
	public String home(Model model) {
//		 모델에 속성 추가
		model.addAttribute("welcome", "안녕하세요");
		return "index";
	}
}
