package com.base.auth.constant;


import io.swagger.models.auth.In;
import java.util.Arrays;
import java.util.List;

public class UserBaseConstant {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";


    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_MANAGER = 2;
    public static final Integer USER_KIND_USER = 3;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer NATION_TYPE_PROVINCE = 0;
    public static final Integer NATION_TYPE_DISTRICT = 1;
    public static final Integer NATION_TYPE_COMMUNE = 2;

    public static final Integer MALE_GENDER = 0;
    public static final Integer FEMALE_GENDER = 1;
    public static final Integer OTHER_GENDER = 2;

    public static final Integer GROUP_KIND_ADMIN = 1;
    public static final Integer GROUP_KIND_MANAGER = 2;
    public static final Integer GROUP_KIND_USER=3;
    public static final Integer GROUP_KIND_CUSTOMER = 5;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final int MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;

    public static final Integer CATEGORY_KIND_NEWS = 1;

    public static final Integer ORDER_BOOKING = 1;
    public static final Integer ORDER_APPROVED = 2;
    public static final Integer ORDER_DELIVERY = 3;
    public static final Integer ORDER_DONE = 4;
    public static final Integer ORDER_CANCEL = 5;

    public static final Integer CUSTOMER_ADDRESS_HOME = 1;
    public static final Integer CUSTOMER_ADDRESS_OFFICE = 2;

    public static final List<Integer> validStates = Arrays.asList(
        ORDER_BOOKING,
        ORDER_APPROVED,
        ORDER_DELIVERY,
        ORDER_DONE,
        ORDER_CANCEL
    );

    private UserBaseConstant(){
        throw new IllegalStateException("Utility class");
    }
}
