package com.exception.demo.multithread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exception.demo.entity.TutorialEntity;
import com.exception.demo.exception.FileWriteException;
import com.exception.demo.repository.TutorialRepo;

@Service
public class MultiThreadFileService implements Runnable {

	@Autowired
	private TutorialRepo tutorialRepo;

	private MultipartFile file;

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public void run() {
		System.out.println("Started thread: " + Thread.currentThread().getName());
		List<TutorialEntity> csvToTutorials = csvToTutorials(file);
		csvToTutorials = tutorialRepo.saveAll(csvToTutorials);
	}

	public List<TutorialEntity> csvToTutorials(MultipartFile file) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			List<TutorialEntity> tutorials = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				TutorialEntity tutorial = new TutorialEntity(0, csvRecord.get("Title"), csvRecord.get("Description"),
						Boolean.parseBoolean(csvRecord.get("Published")));
				tutorials.add(tutorial);
			}
			return tutorials;
		} catch (Exception e) {
			throw new FileWriteException("fail to parse CSV file: " + e.getMessage(), file.getOriginalFilename(),
					Boolean.FALSE);
		}
	}

}
