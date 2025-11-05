package com.ecomm.usermgmt;
import java.util.*;
import java.util.stream.Collectors;
class Practice1{
    public static void main(String[] args){
        List<Integer> list = new ArrayList<>(Arrays.asList(1,4,2,6,8,7,3,1));
        list.forEach(i -> System.out.print(i+" "));
        list = list.stream()
        .filter(num -> num > 3)
        .map(num -> num * 2)
        .sorted().collect(Collectors.toList());
        System.out.println();
        list.forEach(i -> System.out.print(i+" "));
    }
}