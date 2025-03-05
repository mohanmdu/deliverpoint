package com.delivery.product.controller;

import com.delivery.product.constant.AppConstant;
import com.delivery.product.constant.MessageConstant;
import com.delivery.product.enumeration.OrderStatus;
import com.delivery.product.enumeration.UserType;
import com.delivery.product.mapper.OrderVO;
import com.delivery.product.mapper.ResponseVO;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.services.IOrderService;
import com.delivery.product.services.IUserService;
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
    private final IUserService userService;

    @Autowired
    public OrderController(IOrderService orderService, AppUtil appUtil, IUserService userService) {
        this.orderService = orderService;
        this.appUtil = appUtil;
        this.userService = userService;
    }

    @Operation(summary = "Get All Order Service", description = "Find All Order Data", tags = {"Delivery Get All Order"})
    @GetMapping(value = "/get-all-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getAll(){
        return new ResponseEntity<>(appUtil.successResponse(orderService.findAllOrder(), AppConstant.ORDER_RESPONSE_VO, MessageConstant.ORDER_GET_ALL_MSG), HttpStatus.OK);
    }

    @Operation(summary = "Get All Active Order Service", description = "Find All Active Order Data", tags = {"Delivery Get All Active Order"})
    @GetMapping(value = "/get-all-active-order/{userName}/{orderStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getAllActiveOrder(@PathVariable String userName, @PathVariable OrderStatus orderStatus){
        return new ResponseEntity<>(appUtil.successResponse(orderService.findAllActiveOrder(userName, orderStatus), AppConstant.ORDER_RESPONSE_VO, MessageConstant.ORDER_GET_ALL_MSG), HttpStatus.OK);
    }

    @Operation(summary = "Get All Respective User Order Address Service", description = "Find All Respective User Order Address Service", tags = {"Get All Respective User Order Address Service"})
    @GetMapping(value = "/get-all-active-order/{userName}/{userType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getAllAddressByUser(@PathVariable String userName, @PathVariable UserType userType){
        return new ResponseEntity<>(appUtil.successResponse(orderService.getAllAddressByUser(userName, userType), AppConstant.ORDER_RESPONSE_VO, MessageConstant.ORDER_GET_ALL_MSG), HttpStatus.OK);
    }

    @Operation(summary = "Get Order By Id Service", description = "Find Order By Id Data", tags = {"Delivery Get Order By Id"})
    @GetMapping(value = "/get-by-order-id/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getByOrderId(@PathVariable Long orderId){
        Optional<OrderVO> orderVO = orderService.findByOrderId(orderId);
        return orderVO.map(vo -> new ResponseEntity<>(appUtil.successResponse(vo, AppConstant.ORDER_RESPONSE_VO, MessageConstant.ORDER_GET_BY_ID_MSG), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(appUtil.failedResponse(MessageConstant.NO_DATA_FOUND, String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Save Order Service", description = "Save Order Data", tags = {"Save Order Data"})
    @PostMapping(value = "/save-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> saveOrder(@RequestBody OrderVO orderVO){
        String error = orderService.validateOrderDetails(orderVO);
        if(!StringUtils.isBlank(error)){
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, error)), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(appUtil.successResponse(orderService.saveOrder(orderVO), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_CREATED_MSG), HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Book Delivery Person Order Service", description = "Book Delivery Person Order Service", tags = {"Book Delivery Person Order Service"})
    @PostMapping(value = "/booking-order-by-delivery/{orderId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> bookOrderByDelivery(@PathVariable Long orderId, @PathVariable Long userId){
        Optional<OrderVO> orderVODB = orderService.findByOrderId(orderId);
        if(orderVODB.isPresent()){
            Optional<UserVO> userVO = userService.findByUserId(userId);
            if(userVO.isEmpty()){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
            }else{
                if(userVO.get().getUserType().name().equalsIgnoreCase(UserType.DELIVERY.name())){
                    return new ResponseEntity<>(appUtil.successResponse(orderService.bookOrderByDelivery(orderId, userId), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_UPDATED_MSG), HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_USER_TYPE_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
                }
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Start Delivery Person Order Service", description = "Start Delivery Person Order Service", tags = {"Start Delivery Person Order Service"})
    @PostMapping(value = "/start-order-by-delivery/{orderId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> startOrderByDelivery(@PathVariable Long orderId, @PathVariable Long userId){
        Optional<OrderVO> orderVODB = orderService.findByOrderId(orderId);
        if(orderVODB.isPresent()){
            Optional<UserVO> userVO = userService.findByUserId(userId);
            if(userVO.isEmpty()){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
            }else{
                if(userVO.get().getUserType().name().equalsIgnoreCase(UserType.DELIVERY.name())){
                    return new ResponseEntity<>(appUtil.successResponse(orderService.startOrderByDelivery(orderId, userId), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_UPDATED_MSG), HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_USER_TYPE_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
                }
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Completed Delivery Person Order Service", description = "Completed Delivery Person Order Service", tags = {"Completed Delivery Person Order Service"})
    @PostMapping(value = "/complete-order-by-delivery/{orderId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> completedOrderByDelivery(@PathVariable Long orderId, @PathVariable Long userId){
        Optional<OrderVO> orderVODB = orderService.findByOrderId(orderId);
        if(orderVODB.isPresent()){
            Optional<UserVO> userVO = userService.findByUserId(userId);
            if(userVO.isEmpty()){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
            }else{
                if(userVO.get().getUserType().name().equalsIgnoreCase(UserType.DELIVERY.name())){
                    return new ResponseEntity<>(appUtil.successResponse(orderService.completedOrderByDelivery(orderId, userId), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_UPDATED_MSG), HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_USER_TYPE_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
                }
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Cancel Order Service", description = "Cancel Order Service", tags = {"Cancel Order Service"})
    @PostMapping(value = "/cancel-order-by-delivery", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> cancelOrderByDelivery(@RequestParam Long orderId,
                                                               @RequestParam Long userId,
                                                               @RequestParam String comments){
        Optional<OrderVO> orderVODB = orderService.findByOrderId(orderId);
        if(orderVODB.isPresent()){
            Optional<UserVO> userVO = userService.findByUserId(userId);
            if(StringUtils.isBlank(comments)){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.CANCEL_ORDER_COMMENTS_MSG, userId)), HttpStatus.BAD_REQUEST);
            }
            if(userVO.isEmpty()){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_DELIVERY_MSG, userId)), HttpStatus.BAD_REQUEST);
            }
            if(orderVODB.get().getOrderStatus().name().equalsIgnoreCase(OrderStatus.DELIVERED.name())
                    || orderVODB.get().getOrderStatus().name().equalsIgnoreCase(OrderStatus.IN_TRANSIT.name())
                     ||   orderVODB.get().getOrderStatus().name().equalsIgnoreCase(OrderStatus.SHIPPED.name())){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_NOT_CANCEL_MSG, userId)), HttpStatus.BAD_REQUEST);
            }
            if(userVO.get().getUserType().name().equalsIgnoreCase(UserType.CUSTOMER.name()) || userVO.get().getUserType().name().equalsIgnoreCase(UserType.BUSINESS.name())){
                return new ResponseEntity<>(appUtil.successResponse(orderService.cancelOrderByDelivery(orderId, userId, comments), AppConstant.ORDER_RESPONSE_VO,MessageConstant.ORDER_UPDATED_MSG), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.CANCEL_ORDER_MSG, userId)), HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.ORDER_DATA_NO_FOUND_MSG, orderId)), HttpStatus.BAD_REQUEST);
        }
    }

}
