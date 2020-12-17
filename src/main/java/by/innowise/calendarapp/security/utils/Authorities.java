package by.innowise.calendarapp.security.utils;

import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;


public class Authorities {


    public final static GrantedAuthority AUTHORITY = new GrantedAuthority() {
        @Override
        public String getAuthority() {
            return "ROLE_ADMIN";
        }
    };
}
