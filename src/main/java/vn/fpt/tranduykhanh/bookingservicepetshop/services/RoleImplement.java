package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Role;
import vn.fpt.tranduykhanh.bookingservicepetshop.repositories.RoleRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.RoleDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RoleImplement implements RoleInterface {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public ResponseEntity<ResponseObj> createRole(RoleDTO roleDTO) {
       try{
           if(roleRepository.findByRoleName(roleDTO.getRoleName()) != null){
               return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObj(HttpStatus.CONFLICT.toString(), "Role name has exist", roleDTO.getRoleName()));
           }
           Role role = new Role();
           role.setRoleName(roleDTO.getRoleName());
           role.setActive(true);
           role.setCreateAt(LocalDateTime.now());
           roleRepository.save(role);
           return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Create role successfully", role));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error creating role: " + e.getMessage(), null));
       }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllRole() {
        try{
            var role = roleRepository.findAll();
            if(role.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", role));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List role!!", role));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage().toString(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateRole(Long roleId, RoleDTO roleDTO) {
        try{
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
            role.setRoleName(roleDTO.getRoleName());
            role.setUpdateAt(LocalDateTime.now());
            roleRepository.save(role);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Update Successfully", role));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage().toString(), null));
        }
    }
}
