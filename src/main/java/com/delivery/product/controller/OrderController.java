package com.delivery.product.controller;

import com.delivery.product.constant.AppConstant;
import com.delivery.product.constant.MessageConstant;
import com.delivery.product.mapper.OrderVO;
import com.delivery.product.mapper.ResponseVO;
import com.delivery.product.services.IOrderService;
import com.delivery.product.util.AppUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Delivery App Order Service", description = "Order Service API")
@RestController
@RequestMapping("/dlr/order")
public class OrderController {

    private final IOrderService orderService;
    private final AppUtil appUtil;

    @Autowired
    public OrderController(IOrderService orderService, AppUtil appUtil) {
        this.orderService = orderService;
        this.appUtil = appUtil;
    }

    @Operation(summary = "Get All Order Service", description = "Find All Order Data", tags = {"Delivery Get All Order"})
    @GetMapping(value = "/get-all-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getAll(){
        return new ResponseEntity<>(appUtil.successResponse(orderService.findAllOrder(), AppConstant.ORDER_RESPONSE_VO, MessageConstant.ORDER_GET_ALL_MSG), HttpStatus.OK);
    }

    @Operation(summary = "Get Order By Id Service", description = "Find Order By Id Data", tags = {"Delivery Get Order By Id"})
    @GetMapping(value = "/get-by-order-id/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getByOrderId(@PathVariable Long orderId){
        Optional<OrderVO> orderVO = orderService.findByOrderId(orderId);
        return orderVO.map(vo -> new ResponseEntity<>(appUtil.successResponse(vo, AppConstant.ORDER_RESPONSE_VO, MessageConstant.ORDER_GET_BY_ID_MSG), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(appUtil.failedResponse(MessageConstant.NO_DATA_FOUND, String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Save Order Service", description = "Save Order Data", tags = {"Delivery Save Order"})
    @PostMapping(value = "/save-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> saveOrder(@RequestBody OrderVO orderVO){
        String error = orderService.validateOrderDetails(orderVO);
        if(!StringUtils.isBlank(error)){
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, error)), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(appUtil.successResponse(orderService.saveOrder(orderVO), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_CREATED_MSG), HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Update Order Service", description = "Update Order Data", tags = {"Delivery Update Order"})
    @PutMapping(value = "/update-order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> updateOrder(@PathVariable Long orderId, @RequestBody OrderVO orderVO){
        Optional<OrderVO> orderVODB = orderService.findByOrderId(orderId);
        if(orderVODB.isPresent()){
            String error = orderService.validateOrderDetails(orderVO);
            if(!StringUtils.isBlank(error)){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, error)), HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>(appUtil.successResponse(orderService.updateOrder(orderVO), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_UPDATED_MSG), HttpStatus.NO_CONTENT);
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST);
        }
    }
}
