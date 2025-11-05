package com.ecomm.usermgmt;
import java.util.*;
public class practice4 {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(3,6,4,7,5,1,9,2));
		list.forEach(num -> System.out.print(num+" "));
		
		//int max = list.stream().max(Integer::compare).orElse(0);
		//list.remove((Integer) max);
		//max = list.stream().max(Comparator.comparingInt(Integer::intValue)).orElse(0);
		
		int max = list.stream()
				.filter(num -> num < list.stream().max(Integer::compareTo).orElse(0))
				.max(Integer::compareTo)
				.orElse(0);
		System.out.print("\n"+max);
	}
}
