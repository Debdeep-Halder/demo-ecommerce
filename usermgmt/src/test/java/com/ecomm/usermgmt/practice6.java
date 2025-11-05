package com.ecomm.usermgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Employee2{
	private int id;
	private String name;
	private String department;
	
	Employee2(int id, String name, String department ){
		this.id = id;
		this.name = name;
		this.department = department;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDepartment() {
		return department;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", salary=" + department + "]";
	}

	public static void main(String[] args) {
		Employee2 emp1 = new Employee2(1,"abc","Accounts");
		Employee2 emp2 = new Employee2(2,"def","Tech");
		Employee2 emp3 = new Employee2(3,"ghi","Accounts");
		Employee2 emp4 = new Employee2(4,"jkl","Tech");
		Employee2 emp5 = new Employee2(5,"mno","HR");
		
		List<Employee2> list = new ArrayList<Employee2>(Arrays.asList(emp1, emp2, emp3, emp4, emp5));
		list.forEach(emp -> System.out.println(emp.toString()));
		
		list.stream().collect(Collectors.groupingBy(Employee2::getDepartment))
		.forEach((key,value) -> System.out.println(key+" "+value.size()));
		
	}
}
