package com.ecomm.usermgmt;
import java .util.*;
public class practice5 {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>(Arrays.asList("abc","def","AbC","HJI","baf"));
		list.forEach(str -> System.out.print(str+" "));
		
		Collections.sort(list);
		System.out.println();
		list.forEach(str -> System.out.print(str+" "));
		
		Collections.sort(list, Comparator.reverseOrder());
		System.out.println();
		list.forEach(str -> System.out.print(str+" "));
	}
}
