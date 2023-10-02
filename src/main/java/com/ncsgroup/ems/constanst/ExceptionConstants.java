package com.ncsgroup.ems.constanst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstants {

    public static final Integer NOT_FOUND = 404;
    public static final Integer CONFLICT = 409;
    public static final Integer BAD_REQUEST = 400;

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String LANGUAGE = "Accept-Language";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String VI_LANGUAGE = "vi";
    public static class ExceptionMessage{
        public static final String LICENSE_PLATE_NOT_EMPTY = "Lisence plate can not be empty";
    }
}
