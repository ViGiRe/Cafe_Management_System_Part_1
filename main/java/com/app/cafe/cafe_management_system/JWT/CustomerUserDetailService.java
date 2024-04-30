package com.app.cafe.cafe_management_system.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.cafe.cafe_management_system.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUserDetailService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	private com.app.cafe.cafe_management_system.model.User userDetails;
	Logger log = LoggerFactory.getLogger(CustomerUserDetailService.class);
	

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Inside loadUserByUsername", username);

		userDetails = userRepository.findByEmail(username);
		userRepository.save(userDetails);
		if(!Objects.isNull(userDetails)) {
			return new User(userDetails.getEmail(),userDetails.getPassword(),new ArrayList<>());
			
		 }
		throw new UsernameNotFoundException("User Not Found");
	}



	public com.app.cafe.cafe_management_system.model.User getUserDetails() {
//     password is secured this way		
		//com.app.cafe.cafe_management_system.model.User user = userDetails;
		userDetails.setPassword("$2a$12$XsgUg/Lj.p9dLxG02c9EvOMvNm/wwndEl3LVRjKMOtB9X6gSZyv3e");
	

		return userDetails;
	
	}

}
