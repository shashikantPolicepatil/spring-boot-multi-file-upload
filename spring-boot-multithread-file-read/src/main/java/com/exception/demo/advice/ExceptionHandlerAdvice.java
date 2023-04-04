package com.exception.demo.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.exception.demo.dto.ResponseData;
import com.exception.demo.exception.FileWriteException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(FileWriteException.class)
	public ResponseEntity<ResponseData> handleFileWriteException(FileWriteException ex) {
		String message = "Could not upload the file: " + ex.getFileName() + "!";
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseData(HttpStatus.EXPECTATION_FAILED.toString(), message));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ResponseData> handleMaxSizeException(MaxUploadSizeExceededException exc) {
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
				.body(new ResponseData(HttpStatus.EXPECTATION_FAILED.toString(), "File too large!"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validationError(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	}

}
