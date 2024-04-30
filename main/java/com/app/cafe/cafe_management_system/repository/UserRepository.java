package com.app.cafe.cafe_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.cafe.cafe_management_system.model.User;
import com.app.cafe.cafe_management_system.wrapper.UserWrapper;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	//Implementation is done in pojo by namedquery
	User findByEmail(@Param("email") String email);
	List<UserWrapper> getAllUser();
}
