package com.thuy.shopeeproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.thuy.shopeeproject.exceptions.CustomErrorException;

@Component
public class AppUtils {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY", Locale.forLanguageTag("vi"));

    public ResponseEntity<?> mapErrorToResponse(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    public Date parseStringToDate(String dateInString) {

        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        Date date;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Please enter date correct to format dd/MM/YYYY");
        }
        return date;
    }
}
