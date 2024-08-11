package com.ezp.sac.model;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class User {
	public String username; //user
	public String name;
	public String password;
	
	public Long transaction_id; //payment
	public String type;
	public Double amount;
	public LocalDateTime date;
	public String status; //transaction id, type, amount, date, status //incomplete
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(Long transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User(String username, String name, String password, Long transaction_id, String type, Double amount,
			LocalDateTime date, String status) {
		super();
		this.username = username;
		this.name = name;
		this.password = password;
		this.transaction_id = transaction_id;
		this.type = type;
		this.amount = amount;
		this.date = date;
		this.status = status;
	}
	
	

	public User() {}
	
	
	
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return "User [username=" + username + ", name=" + name + ", password=" + password + ", transaction_id="
				+ transaction_id + ", type=" + type + ", amount=" + amount + ", date=" + date.format(formatter) + ", status=" + status
				+ "]";
	}
	

	
	
	
}