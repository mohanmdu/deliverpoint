package com.delivery.product.config;

import java.util.Optional;

import com.delivery.product.security.CustomUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "System";
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails userDetails){
            currentPrincipalName = userDetails.getUsername();
        } else if(principal instanceof String userString){
            currentPrincipalName = userString;
        }
        currentPrincipalName = currentPrincipalName.equalsIgnoreCase("anonymousUser") ? "System" : currentPrincipalName;
        return Optional.of(currentPrincipalName);
    }

}
