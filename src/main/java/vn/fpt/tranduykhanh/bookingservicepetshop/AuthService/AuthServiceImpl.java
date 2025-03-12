package vn.fpt.tranduykhanh.bookingservicepetshop.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.User;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthServiceInterface{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userRepository.findByUserName(username) == null){

        }
        User user = userRepository.findByUserName(username);
        return user;
    }
}
