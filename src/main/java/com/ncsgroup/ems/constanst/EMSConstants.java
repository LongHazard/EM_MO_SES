package com.ncsgroup.ems.constanst;


import java.time.Instant;

public class EMSConstants {

  public static class CommonConstants {
    public static final String SUCCESS = "com.ncsgroup.ems.constanst.CommonConstants.SUCCESS";
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String LANGUAGE = "Accept-Language";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String SYSTEM = "SYSTEM";
    public static final Integer EXIST = 1;
    public static final Integer ZERO = 0;
    public static final Long ANONYMOUS = 0L;
    public static final Long TIME_NOW = Instant.now().toEpochMilli();

  }

  public static final class ImageType {
    public static final String PERSON = "PERSON";
    public static final String VEHICLE = "VEHICLE";
    public static final String VEHICLE_CARD_FRONT = "VEHICLE_CARD_FRONT";
    public static final String VEHICLE_CARD_BACK = "VEHICLE_CARD_BACK";
    public static final String PERSON_CARD_FRONT = "PERSON_CARD_FRONT";
    public static final String PERSON_CARD_BACK = "PERSON_CARD_BACK";
  }

  public static final class GroupType {
    public static final String PERSON = "PERSON";
    public static final String VEHICLE = "VEHICLE";
  }

  public static final class AddressType {
    public static final String PLACE_OF_ORIGIN_TYPE = "origin";
    public static final String PERMANENT_RESIDENT_TYPE = "resident";
    public static final String VEHICLE_REGISTRATION_PLACE = "registration_place";
  }

  public static class StatusException {
    public static final Integer NOT_FOUND = 404;
    public static final Integer CONFLICT = 409;
    public static final Integer BAD_REQUEST = 400;
  }

  public static class MessageCode {
    public static final String CREATE_VEHICLE_BRAND_SUCCESS = "com.ncsgroup.ems.controller.VehicleBrandController.create";
    public static final String CREATE_COLOR_SUCCESS = "com.ncsgroup.ems.controller.ColorController.create";
    public static final String CREATE_PERSON_SUCCESS = "com.ncsgroup.ems.controller.PersonController.create";
    public static final String CREATE_CARD_TYPE_SUCCESS = "com.ncsgroup.ems.controller.CardTypeController.create";
    public static final String DELETE_CARD_TYPE_SUCCESS = "com.ncsgroup.ems.controller.CardTypeController.delete";
    public static final String UPDATE_CARD_TYPE_SUCCESS = "com.ncsgroup.ems.controller.CardTypeController.update";
    public static final String DELETE_COLOR_SUCCESS = "com.ncsgroup.ems.controller.ColorController.delete";
    public static final String CREATE_PERSON_CARD_SUCCESS = "com.ncsgroup.ems.controller.PersonCardController.create";
    public static final String UPDATE_PERSON_CARD_SUCCESS = "com.ncsgroup.ems.controller.PersonCardController.update";
    public static final String DELETE_VEHICLE_TYPE_SUCCESS = "com.ncsgroup.ems.controller.VehicleTypeController.delete";
    public static final String CREATE_VEHICLE_TYPE_SUCCESS = "com.ncsgroup.ems.controller.VehicleTypeController.create";
    public static final String CREATE_VEHICLE_SUCCESS = "com.ncsgroup.ems.controller.VehicleController.create";
    public static final String CREATE_VEHICLE_CARD_SUCCESS = "com.ncsgroup.ems.controller.VehicleCardController.create";
    public static final String UPDATE_VEHICLE_CARD_SUCCESS = "com.ncsgroup.ems.controller.VehicleCardController.update";
    public static final String DELETE_VEHICLE_SUCCESS = "com.ncsgroup.ems.controller.VehicleController.delete";
    public static final String CREATE_GROUP_SUCCESS = "com.ncsgroup.ems.controller.GroupController.create";
    public static final String UPDATE_GROUP_SUCCESS = "com.ncsgroup.ems.controller.GroupController.update";

  }

  public static class MessageValidation {
    public static final String INVALID_EMAIL = "Invalid Email";
    public static final String INVALID_PHONE_NUMBER = "Invalid Phone Number";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
  }

  public static class MessageException {

  }

  public static class AuthConstant {
    public static String TYPE_TOKEN = "Bear ";
    public static String AUTHORIZATION = "Authorization";

  }
}
