package com.ecomm.usermgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class practice7 {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>(Arrays.asList("abc","def","abc","HJI","baf"));
		list.forEach(str -> System.out.print(str+" "));
		
		List<String> list1 = list.stream().filter(str -> Collections.frequency(list, str) > 1).distinct().collect(Collectors.toList());
		System.out.println();
		list1.forEach(str -> System.out.print(str+" "));
		
		List<String> list2 = list.stream().filter(str -> Collections.frequency(list, str) == 1).distinct().collect(Collectors.toList());
		System.out.println();
		list2.forEach(str -> System.out.print(str+" "));
	}
}
