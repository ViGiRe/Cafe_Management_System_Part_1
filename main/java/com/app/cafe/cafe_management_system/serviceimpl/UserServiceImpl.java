package com.app.cafe.cafe_management_system.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.cafe.cafe_management_system.Iservice.UserService;
import com.app.cafe.cafe_management_system.JWT.CustomerUserDetailService;
import com.app.cafe.cafe_management_system.JWT.JwtUtil;
import com.app.cafe.cafe_management_system.JWT.jwtFilter;
import com.app.cafe.cafe_management_system.constents.CafeConstants;
import com.app.cafe.cafe_management_system.model.User;
import com.app.cafe.cafe_management_system.repository.UserRepository;
import com.app.cafe.cafe_management_system.utils.CafeUtils;
import com.app.cafe.cafe_management_system.wrapper.UserWrapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUserDetailService customerUserDetailService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	jwtFilter jwtFilter;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public ResponseEntity<String> signup(Map<String, String> requestMap) {
		
		log.info("Inside SignUp", requestMap);

		try {
			if(validateSignUpMap(requestMap)) {
				//email id exist in db or not
				User user = userRepository.findByEmail(requestMap.get("email"));
				if(Objects.isNull(user)) {
					
					 //register the user
					//here we cannot directly pass requestMap
					userRepository.save(getUserFromMap(requestMap));
					return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
				}
				else {
					return CafeUtils.getResponseEntity("Email Already Exist", HttpStatus.BAD_REQUEST);
				}
				
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.Invalid_Data, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.Something_went_wrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private boolean validateSignUpMap(Map<String, String> requestMap) {
	if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") 
			&& requestMap.containsKey("password")) {
		return true;
		
	}	
		return false;	
	}
	
	private User getUserFromMap(Map<String,String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
		
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap){
	    log.info("Inside Login");
	    try {
	    	
	    	System.out.println(requestMap.get("email"));
	    	System.out.println(requestMap.get("password"));
    		
	        Authentication auth = authenticationManager.authenticate(
	        		
	                new UsernamePasswordAuthenticationToken(requestMap.get("email"), bCryptPasswordEncoder.encode(requestMap.get("password"))));
	        if (auth.isAuthenticated()) {
	            if (customerUserDetailService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
	                String token = jwtUtil.generateToken(
	                        customerUserDetailService.getUserDetails().getEmail(),
	                        customerUserDetailService.getUserDetails().getRole());
	                return new ResponseEntity<String>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<String>("{\"message\":\"wait for admin approval\"}", HttpStatus.BAD_REQUEST);
	            }
	        }
	    } catch (Exception e) {
	        log.error("Authentication Error", e);
	    }
	    return new ResponseEntity<String>("{\"message\":\"Bad Credentials\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if(jwtFilter.isAdmin()) {
				
			}
			else {
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
