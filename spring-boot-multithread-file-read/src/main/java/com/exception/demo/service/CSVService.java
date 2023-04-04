package com.exception.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exception.demo.entity.TutorialEntity;
import com.exception.demo.exception.FileWriteException;
import com.exception.demo.helper.CSVHelper;
import com.exception.demo.repository.TutorialRepo;

@Service
public class CSVService {

	@Autowired
	private TutorialRepo tutorialRepo;

	public CompletableFuture<Void> save(MultipartFile file) throws FileWriteException {
		System.out.println("Started thread: "+Thread.currentThread().getName());
		List<TutorialEntity> csvToTutorials = csvToTutorials(file);
		csvToTutorials = tutorialRepo.saveAll(csvToTutorials);
		return new CompletableFuture<Void>();
	}

	public List<TutorialEntity> getAll() {
		return tutorialRepo.findAll();
	}
	
	public List<TutorialEntity> csvToTutorials(MultipartFile file) {
		CSVParser csvParser=null;
		try  {

			BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
			csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			List<TutorialEntity> tutorials = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				TutorialEntity tutorial = new TutorialEntity(0,
						csvRecord.get("Title"), csvRecord.get("Description"),
						Boolean.parseBoolean(csvRecord.get("Published")));
				tutorials.add(tutorial);
			}
			return tutorials;
		} catch (Exception e) {
			throw new FileWriteException("fail to parse CSV file: " + e.getMessage(),file.getOriginalFilename(),Boolean.FALSE);
		} finally {
			try {
				csvParser.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
