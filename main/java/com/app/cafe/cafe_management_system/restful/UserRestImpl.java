package com.app.cafe.cafe_management_system.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.app.cafe.cafe_management_system.Iservice.UserService;
import com.app.cafe.cafe_management_system.constents.CafeConstants;
import com.app.cafe.cafe_management_system.rest.UserRest;
import com.app.cafe.cafe_management_system.utils.CafeUtils;
import com.app.cafe.cafe_management_system.wrapper.UserWrapper;

@RestController
public class UserRestImpl implements UserRest{

	@Autowired
	UserService userService;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			return userService.signup(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
	try {
		return userService.login(requestMap);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return CafeUtils.getResponseEntity(CafeConstants.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			userService.getAllUser();
		} catch (Exception e) {
		e.printStackTrace(); 
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
