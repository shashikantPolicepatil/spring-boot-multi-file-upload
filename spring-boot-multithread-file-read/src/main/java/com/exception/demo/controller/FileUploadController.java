package com.exception.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exception.demo.dto.ResponseData;
import com.exception.demo.entity.TutorialEntity;
import com.exception.demo.exception.FileWriteException;
import com.exception.demo.helper.CSVHelper;
import com.exception.demo.multithread.MultiThreadFileService;
import com.exception.demo.service.CSVService;

@RestController
@RequestMapping("/api/csv")
public class FileUploadController {

	@Autowired
	private CSVService fileService;
	
	@Autowired
	private MultiThreadFileService threadFileService;
	
	@Autowired
	private Executor executor;

	@PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseData> uploadFile(@RequestParam("file") MultipartFile file) throws FileWriteException {
		String message = "";
		if (CSVHelper.hasCSVFormat(file)) {
			fileService.save(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseData("200", message));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData("400", "Please upload a csv file!"));
	}

	@GetMapping("/tutorials")
	public ResponseEntity<List<TutorialEntity>> getAllTutorials() {
		try {
			List<TutorialEntity> tutorials = fileService.getAll();

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/upload/multiple",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<ResponseData>> uploadFiles(@RequestParam("file") MultipartFile[] files) throws FileWriteException {
		ArrayList<ResponseData> responseDataList = new ArrayList<>();
		Stream.of(files).forEach(file->{
			if (CSVHelper.hasCSVFormat(file)) {
				threadFileService.setFile(file);
				executor.execute(threadFileService);
			}
		});
		return ResponseEntity.status(HttpStatus.OK).body(responseDataList);
	}

}
