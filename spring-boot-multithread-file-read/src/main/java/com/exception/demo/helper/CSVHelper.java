package com.exception.demo.helper;

import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {

	public static String TYPE = "text/csv";
	static String[] HEADERs = {  "Title", "Description", "Published" };

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	

}
