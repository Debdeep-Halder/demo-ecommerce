package com.ecomm.usermgmt;
import java.util.*;
import java.util.stream.Collectors;
class Person{
	private int age;
	private String fname;
	private String lname;
	
	Person(int age, String fname, String lname ){
		this.age = age;
		this.fname = fname;
		this.lname = lname;
	}
	public int getAge() {
		return age;
	}
	public String getFname() {
		return fname;
	}
	public String getLname() {
		return lname;
	}
	@Override
	public String toString() {
		return "Person [age=" + age + ", fname=" + fname + ", lname=" + lname + "]";
	}

	public static void main(String[] args) {
		Person person1 = new Person(15,"abc","mno");
		Person person2 = new Person(12,"def",null);
		Person person3 = new Person(30,"ghi","pqr");
		Person person4 = new Person(24,"jkl",null);
		
		List<Person> list = new ArrayList<Person>(Arrays.asList(person1, person2, person3, person4));
		list.forEach(person -> System.out.println(person.toString()));
		
		System.out.println();
		list.sort(Comparator.comparing(Person::getLname, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
				.thenComparing(Comparator.comparing(Person::getAge, Comparator.reverseOrder())));
		list.forEach(person -> System.out.println(person.toString()));
	}
}
