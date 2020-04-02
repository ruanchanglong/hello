package com.chengxu.sun.home.dao;

import com.chengxu.sun.home.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

	void newUser(@Param("user")User user);
}
