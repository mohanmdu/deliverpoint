package com.delivery.product.util;

import com.delivery.product.mapper.ResponseVO;
import com.delivery.product.security.CustomUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AppUtil {

    public ResponseVO successResponse(Object object, String key, String message){
        ResponseVO responseVO = new ResponseVO();
        Map<String, Object> map = new HashMap<>();
        map.put(key, object);
        responseVO.setOutputData(map);
        responseVO.setSuccessMessage(message);
        responseVO.setAppStatus(true);
        return responseVO;
    }

    public ResponseVO failedResponse(String errorCode, String errorDetails){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setErrorCode(errorCode);
        responseVO.setErrorDetails(errorDetails);
        responseVO.setAppStatus(false);
        return responseVO;
    }

    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "System";
        if(authentication != null){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            currentPrincipalName = customUserDetails != null && !StringUtils.isBlank(customUserDetails.getUsername()) ? customUserDetails.getUsername() : "System";
        }
        return Optional.of(currentPrincipalName);
    }

    public double calculateDeliveryFees(double distanceInKm, double weightInKg, double cost){
        // Inputs
        double baseCharge = 50.0; // Base charge in currency units
        double ratePerKm = 10.0; // Rate per kilometer
        double ratePerKg = 15.0; // Rate per kilogram
        double ratePerCost = 20.0; // Rate per cubic meter

        // Calculate delivery charge
        return baseCharge
                + (distanceInKm * ratePerKm)
                + (weightInKg * ratePerKg)
                + (cost * ratePerCost);
    }

}
