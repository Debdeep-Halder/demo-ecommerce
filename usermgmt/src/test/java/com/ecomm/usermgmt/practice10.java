package com.ecomm.usermgmt;
import java.util.*;
import java.util.stream.Collectors;
class Employee4{
	private int id;
	private String name;
	private double salary;
	Employee4(int id, String name, double salary ){
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
		return "Employee4 [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}
	public static void main(String[] args) {
		Employee4 emp1 = new Employee4(1,"abc",44000);
		Employee4 emp2 = new Employee4(2,"def",34500);
		Employee4 emp3 = new Employee4(3,"ghi",58900);
		Employee4 emp4 = new Employee4(4,"jkl",44000);
		
		List<Employee4> list = new ArrayList<Employee4>(Arrays.asList(emp1, emp2, emp3, emp4));
		list.forEach(emp -> System.out.println(emp.toString()));
		System.out.println();
		
		list.sort(Comparator.comparingDouble(Employee4::getSalary).reversed());
		list.forEach(emp -> System.out.println(emp.toString()));
		System.out.println();
		
		list.stream()
		.limit(3)
		.collect(Collectors.toList()).forEach(emp -> System.out.println(emp.toString()));
	}
	
}
