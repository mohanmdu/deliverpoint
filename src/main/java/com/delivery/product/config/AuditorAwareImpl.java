package com.delivery.product.config;

import java.util.Optional;

import com.delivery.product.security.CustomUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "System";
        if(authentication != null){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            currentPrincipalName = customUserDetails != null && !StringUtils.isBlank(customUserDetails.getUsername()) ? customUserDetails.getUsername() : "System";
        }
        return Optional.of(currentPrincipalName);
    }

}
