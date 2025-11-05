package com.ecomm.usermgmt;
import java.util.*;
import java.util.stream.Collectors;
public class practice12 {
	
	public static boolean isPrime(int num) {
		if (num < 2)
			return false;
		for (int i = 2; i <= Math.sqrt(num); i++)
			if(num % i == 0)
				return false;
		return true;
	}
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 1;i <= 10; i++)
			list.add(i);
		list.forEach(i -> System.out.print(i+" "));
		
		
		System.out.println();
		list.stream()
		.filter(i -> isPrime(i))
		.forEach(i -> System.out.print(i+" "));
		
		System.out.println();
		list.stream()
		.collect(Collectors.partitioningBy(practice12::isPrime))
		.entrySet().stream()
		.forEach(entry -> System.out.println(
				(entry.getKey().equals(true)?"Prime":"Composite")
				+ ": "+entry.getValue().toString()));
	}

}
