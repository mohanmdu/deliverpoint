package com.delivery.product.security;

import java.util.Collection;

import com.delivery.product.mapper.UserVO;
import com.delivery.product.model.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomUserDetails extends UserEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(UserVO user) {
		this.username = user.getEmail();
		this.password = user.getPassword();
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

}
