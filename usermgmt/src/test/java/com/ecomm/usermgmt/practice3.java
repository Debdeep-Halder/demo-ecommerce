package com.ecomm.usermgmt;

import java.util.*;

public class practice3 implements Comparable{
	
	String name;
	int marks;
	
	practice3(String name, int marks){
		this.name = name;
		this.marks = marks;
	}
	public String getName() {
		return name;
	}
	public int getMarks() {
		return marks;
	}
	
	@Override
	public String toString() {
		return "practice3 [name=" + name + ", marks=" + marks + "]";
	}
	@Override
	public int compareTo(Object o) {
		practice3 obj = (practice3) o;
		if(marks > obj.getMarks())
			return 1;
		else if(marks < obj.getMarks())
			return -1;
		return 0;
	}
	
	public static void main(String[] args) {
		practice3 obj1 =  new practice3("abc",86);
		practice3 obj2 =  new practice3("bcd",72);
		practice3 obj3 =  new practice3("bbg",68);
		practice3 obj4 =  new practice3("aez",81);
		List<practice3> list = new ArrayList<practice3>(Arrays.asList(obj1, obj2,obj3, obj4));
		list.forEach(student -> System.out.println(student.toString()));
		
		Collections.sort(list);
		System.out.println();
		list.forEach(student -> System.out.println(student.toString()));
		
		list.sort(Comparator.comparing(practice3::getName, Comparator.naturalOrder()));
		System.out.println();
		list.forEach(student -> System.out.println(student.toString()));
	}
}
