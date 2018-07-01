package com.auto.client;

import java.sql.Timestamp;

public interface Person {

	  Address getAddress();
	  String getName();
	  void setName(String name);
	  
	  void setAddress(Address a);
	  
	  Timestamp getTimestamp();
	  void setTimestamp(Timestamp t);
	  int getAnni();
	  void setAnni(int i);
	  
}
