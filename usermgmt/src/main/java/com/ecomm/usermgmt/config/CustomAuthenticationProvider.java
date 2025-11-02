package com.ecomm.usermgmt.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ecomm.usermgmt.domain.Users;
import com.ecomm.usermgmt.repo.UserRepo;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
    private UserRepo repo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernameRoleAuthenticationToken token = (UsernameRoleAuthenticationToken) authentication;

        String username = token.getName();
        String rawPassword = String.valueOf(token.getCredentials());
        String requestedRole = token.getRole();

        System.out.println(requestedRole);
        Optional<Users> rows = repo.findByUsernameAndRoleName(username, requestedRole);
        Users row = null;
        if(rows == null)
        	System.out.print("There is no user with this role in the DB");
        else {
        	row = rows.get();
        	System.out.print("User "+row.getUsername()+" with role "+row.getRole().getName()+" is found");
        }

        if (!rawPassword.equals(row.getPassword())) {
            throw new BadCredentialsException("Bad credentials, the password is incorrect");
        }

        // Grant only the requested role for this login session
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + requestedRole.toUpperCase()));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authClass) {
        return UsernameRoleAuthenticationToken.class.isAssignableFrom(authClass);
    }
}
