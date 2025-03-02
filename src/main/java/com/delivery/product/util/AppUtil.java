package com.delivery.product.util;

import com.delivery.product.mapper.ResponseVO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

}
