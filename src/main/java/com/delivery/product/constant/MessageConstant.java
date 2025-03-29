package com.delivery.product.constant;

public class MessageConstant {
    private MessageConstant(){

    }

    public static final String USER_CREATED_MSG="User Created Successfully";
    public static final String LOGIN_SUCCESS="User login Successfully";
    public static final String USER_UPDATED_MSG="User Updated Successfully";
    public static final String USER_DELETED_MSG="User Deleted Successfully";
    public static final String USER_GET_ALL_MSG="Get All User Successfully";
    public static final String USER_GET_BY_ID_MSG="Get User By Id Successfully";
    public static final String USER_DATA_NO_FOUND_MSG="User name not found %s in data base";
    public static final String USER_ALREADY_EXISTS_MSG="User mobile already exists %s in data base";
    public static final String LOGIN_FAILED="User not found in data base";
    public static final String PASSWORD_NOT_MATCH="Password doesn't match";
    public static final String INPUT_ERROR="Please validate input for %s";
    public static final String NO_DATA_FOUND="No Data Found";

    public static final String USER_ALREADY_EXISTS_MSG_1="User email or mobile already exists %s in data base";


    public static final String ORDER_GET_ALL_MSG="Get All Order Successfully";
    public static final String ORDER_GET_BY_ID_MSG="Get Order By Id Successfully";
    public static final String ORDER_CREATED_MSG="Order Created Successfully";
    public static final String ORDER_DATA_NO_FOUND_MSG="Order not found %s in data base";
    public static final String ORDER_UPDATED_MSG="Order Updated Successfully";
    public static final String ORDER_DATA_NO_FOUND_DELIVERY_MSG="Delivery User Data Not Found in System";
    public static final String ORDER_DATA_USER_TYPE_DELIVERY_MSG="You don't have access to delivery the order";
    public static final String CANCEL_ORDER_MSG="You don't have access to cancel the order";
    public static final String ORDER_NOT_CANCEL_MSG="Order can't be cancelled";
    public static final String CANCEL_ORDER_COMMENTS_MSG="Cancel Order Comments Should Not Be Empty";
}
