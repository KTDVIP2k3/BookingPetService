package vn.fpt.tranduykhanh.bookingservicepetshop.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;

@Component
public class AuthenUtil {

    public User getCurrentUSer(){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User)
            return (User) user;
        else
            return null;
    }
}
