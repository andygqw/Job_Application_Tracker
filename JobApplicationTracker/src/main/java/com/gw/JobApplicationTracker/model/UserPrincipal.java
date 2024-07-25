package com.gw.JobApplicationTracker.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(UserPrincipal.class);

    private int id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public int getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserPrincipal create(UserDetails user, int id) {
        logger.warn("In UserPrinciple: " + user.getAuthorities().toArray().toString());
        return new UserPrincipal(
            id,
            user.getUsername(),
            user.getPassword(),
            user.getAuthorities()
        );
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}