package com.app.cafe.cafe_management_system.Iservice;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.app.cafe.cafe_management_system.model.User;
import com.app.cafe.cafe_management_system.wrapper.UserWrapper;

public interface UserService {

	ResponseEntity<String> signup(Map<String, String> requestMap);

	ResponseEntity<String> login(Map<String, String> requestMap);

	ResponseEntity<List<UserWrapper>> getAllUser();

}
