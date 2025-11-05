package com.ecomm.usermgmt;
import java.util.*;
class Employee3{
	private int id;
	private String name;
	private double salary;
	Employee3(int id, String name, double salary ){
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public double getSalary() {
		return salary;
	}
	@Override
	public String toString() {
		return "Employee3 [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}
	public static void main(String[] args) {
		Employee3 emp1 = new Employee3(1,"abc",44000);
		Employee3 emp2 = new Employee3(2,"def",34500);
		Employee3 emp3 = new Employee3(3,"ghi",58900);
		Employee3 emp4 = new Employee3(4,"jkl",44000);
		
		List<Employee3> list = new ArrayList<Employee3>(Arrays.asList(emp1, emp2, emp3, emp4));
		list.forEach(emp -> System.out.println(emp.toString()));
		System.out.println();
		System.out.println(list.stream().max(Comparator.comparingDouble(Employee3::getSalary)).orElse(null).toString());
		
	}
	
}
