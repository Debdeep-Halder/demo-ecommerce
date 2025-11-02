package com.ecomm.usermgmt.custom_metrics;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActiveUsers {
	private final RedisTemplate<String, String> redis = new RedisTemplate<String, String>();
	
}
