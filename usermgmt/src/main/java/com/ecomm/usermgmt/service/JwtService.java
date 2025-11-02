package com.ecomm.usermgmt.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.ArrayList;
import java.util.Base64;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private String secretKey = "ecommerce-project";
	
	public JwtService() throws NoSuchAlgorithmException {
	    KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
	    SecretKey sk = keyGen.generateKey();
	    secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
	    System.out.print(secretKey);
	}
	
	public String generateToken(String username, Set<String> roles) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", roles);
		
		return Jwts.builder()
				.claims().add(claims)
				.subject(username)
				.issuer("ecomm-user-mgmt-service")
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
				.and()
				.signWith(getKey()).compact();
		
	}
	
	private SecretKey getKey() {
		byte[] KeyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(KeyBytes);
		
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
        return Jwts
        		.parser()
        		.verifyWith(getKey())
        		.build()
        		.parseSignedClaims(token)
        		.getPayload(); 
    }
	
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public String extractUserRole(String token) {
		ArrayList list = extractClaim(token, claims -> claims.get("role", ArrayList.class));
		System.out.println("Total size of the array" + list.size());
		return list.get(0).toString().substring(5).toLowerCase();
	}

	public boolean jwtValidateToken(String token) {
		Claims claims = Jwts.parser()
	            .verifyWith(getKey())
	            .requireIssuer("ecomm-user-mgmt-service")
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();

	    Date exp = claims.getExpiration();
	    return exp != null && exp.after(new Date());
	}

}
