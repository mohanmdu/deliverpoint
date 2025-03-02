package com.delivery.product.mapper;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseVO implements Serializable {

   private static final long serialVersionUID = 1234567L;

   private Map<String, Object> outputData;
   private boolean appStatus;
   private String successMessage;
   private String errorCode;
   private String errorDetails;
   private String errorMessage;

}
