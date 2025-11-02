package com.ecomm.usermgmt.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecomm.usermgmt.service.RedisService;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class MetricsConfig {
	
	@Bean
	CommandLineRunner registerMeter(MeterRegistry meterRegistry, RedisService redisService) {
		
		return args -> {
		      Gauge.builder("app_active_user_total", redisService, RedisService :: totalActive)
		      .description("Total active users").register(meterRegistry);
		      
		      Gauge.builder("app_active_admin_total", redisService, admin -> admin.totalActiveByRole("admin"))
		      .description("Total active admins").register(meterRegistry);
		      
		      Gauge.builder("app_active_buyer_total", redisService, admin -> admin.totalActiveByRole("buyer"))
		      .description("Total active buyers").register(meterRegistry);
		      
		      Gauge.builder("app_active_seller_total", redisService, admin -> admin.totalActiveByRole("seller"))
		      .description("Total active sellers").register(meterRegistry);
		};
	}
	
}
