package com.ecomm.usermgmt.service;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
    private static final Duration EXPIRY = Duration.ofMinutes(10);
	
	private String key(String username, String role) {
        return "active:" + username + ":" + role.toLowerCase();
    }
	
	private boolean isRedisUp() {
		boolean status =false;
		if(redisTemplate.getConnectionFactory().getConnection().ping().toString().equalsIgnoreCase("pong"))
			return true;
		return status;
	}
	
	public void setKey(String username, String role) {
		
		String key = key(username,role);
		if(isRedisUp()) {
			//redisTemplate.opsForHash().put(key, "role", role);
			redisTemplate.opsForHash().put(key, "lastseen", String.valueOf(System.currentTimeMillis()));
			
			redisTemplate.expire(key, EXPIRY);
		}
	}
	
	public void logout(String username, String role) {
		if(isRedisUp())
			redisTemplate.delete(key(username,role));
	}
	
	public long totalActive() {
		Long total = 0l;
		if(isRedisUp())
			total = (long) redisTemplate.keys("active:*:*").size();
		else
			System.out.print("The Redis Server is not running!");
		return total;
    }
	
	public long totalActiveByRole(String role) {
		Long totalByRole = 0l;
		if(isRedisUp()) {
			totalByRole = (long) redisTemplate.keys("active:*:" + role.toLowerCase()).size();
			//totalByRole =  keys.stream().filter(k -> redisTemplate.opsForHash().get(k, "role").toString().equalsIgnoreCase(role)).count();
		}
		else
			System.out.print("The Redis Server is not running!");
		return totalByRole;
	}
	
//	public String getKey(String username) {
//		String str= "";
//		if(isRedisUp())
//			for(Entry<Object, Object> entry : redisTemplate.opsForHash().entries(key(username)).entrySet())
//				str+= "Key: "+entry.getKey()+" Value: "+entry.getValue();
//		else
//			System.out.print("The Redis Server is not running!");
//		return str;
//	}
}
