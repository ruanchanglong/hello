package com.chengxu.sun.home.bean;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName User
 * @Description TODO
 * @Author EDZ
 * @Date 2020/4/113:02
 * @Version 1.0
 **/
@Data
public class User {

	private String id;
	private String name;
	private String phone;
	private String address;
	private String age;
	private int gender;
	private String email;
	@DateTimeFormat
	private Date createTime;
	@DateTimeFormat
	private Date updateTime;

}
