package com.auto.client;

import java.util.List;

public interface Worker extends Person{
	String getFreeday();
	void setFreeday(String day);
	
	List<Address> getAltAddress();
	void setAltAddress(List<Address> l);
}
