package com.ecomm.usermgmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class practice8 {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>(Arrays.asList("abcd","def","abfgc","HJIwssf","ba","abcd"));
		list.forEach(str -> System.out.print(str+" "));
		
		System.out.println();
		//Map<String, Integer> map = new HashMap<>();
		//list.forEach(str -> map.put(str, str.length()));
		//for(Entry<String, Integer> str: map.entrySet())
		//	System.out.println(str.getKey() +" "+str.getValue());
		list.stream()
			.collect(Collectors.toMap(str -> str, str -> str.length(), (str1, str2) -> str1))
			.forEach((k,v) -> System.out.println(k+" "+v));
		
		System.out.println();
		list.stream()
			.collect(Collectors.groupingBy(String::valueOf))
			.entrySet().stream()
			.filter(entry -> entry.getValue().size() > 1)
			.forEach(entry -> System.out.println(entry.getKey()+" "+entry.getValue().size()));
	}
}
