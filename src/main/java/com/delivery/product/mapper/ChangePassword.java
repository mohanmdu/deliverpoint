package com.delivery.product.mapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChangePassword implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private long userId;
    private String userName;
    private String newPassword;
    private String oldPassword;
}
