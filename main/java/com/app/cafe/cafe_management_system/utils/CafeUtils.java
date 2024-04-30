package com.app.cafe.cafe_management_system.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//util methos---generic 
public class CafeUtils {
	
	
	public CafeUtils() {
		
	}

	public static ResponseEntity<String> getResponseEntity(String responseMessage,HttpStatus httpStatus){
		return new ResponseEntity<String>(responseMessage, httpStatus); 
		
	}
}
