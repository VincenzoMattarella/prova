package com.auto.client;

import java.sql.Timestamp;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MyFactory extends AutoBeanFactory{
	AutoBean<Address> address();
	AutoBean<Person> person();
	AutoBean<Worker> worker();
	}
