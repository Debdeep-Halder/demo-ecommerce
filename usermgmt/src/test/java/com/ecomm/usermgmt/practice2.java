package com.ecomm.usermgmt;
import java.util.*;
class Employee{
	private int id;
	private String name;
	private double salary;
	
	Employee(int id, String name, double salary ){
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
		return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}

	public static void main(String[] args) {
		Employee emp1 = new Employee(1,"abc",44000);
		Employee emp2 = new Employee(2,"def",34500);
		Employee emp3 = new Employee(3,"ghi",58900);
		Employee emp4 = new Employee(4,"jkl",44000);
		
		List<Employee> list = new ArrayList<Employee>(Arrays.asList(emp1, emp2, emp3, emp4));
		list.forEach(emp -> System.out.println(emp.toString()));
		
		list.sort(Comparator.comparingDouble(Employee::getSalary)
				.thenComparing(Employee::getName, Comparator.naturalOrder()));
		System.out.println();
		list.forEach(emp -> System.out.println(emp.toString()));
	}
}
