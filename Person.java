package Coursework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Person {
	private int userId;
	private String username;
	private String surname;
	private int houseNumber;
	private String postcode;
	private String city;
	private String roletype;
	
	//Defining the structure of a person object
	public Person(int userId, String username, String surname, int houseNumber, String postcode, String city, String roletype) {
		this.userId = userId;
		this.username = username;
		this.surname = surname;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.roletype = roletype; 
	}
	
	public int retId() {
		return this.userId;
	}
	public String retType() {
		return this.roletype;
	}
	public String retCode() {
		return this.postcode;
	}
}