package com.example.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.AttachVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
import com.example.service.AttachService;

@Controller
@RequestMapping("/gallery/*")
public class GalleryController {

	private AttachService attachService;
	
	
	
	public GalleryController(AttachService attachService) {
		super();
		this.attachService = attachService;
	}



	@GetMapping("/list")
	public String list(Criteria cri, Model model) {
		
		cri.setAmount(12);
		
		// 전체 이미지 첨부파일 리스트
		List<AttachVO> attachList = attachService.getImgAttachesByCri(cri);
		
		// 이미지 첨부파일 개수
		int totalCount = attachService.getCountImgAttaches();
		
		
		PageDTO pageDTO = new PageDTO(cri, totalCount);
		
		System.out.println(pageDTO);
		
		model.addAttribute("attachList", attachList);
		model.addAttribute("pageMaker", pageDTO);
		
		return "gallery/galleryList";
	}

}
