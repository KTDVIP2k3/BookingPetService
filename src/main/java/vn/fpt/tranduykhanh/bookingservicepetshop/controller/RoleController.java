package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.RoleDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.RoleImplement;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    RoleImplement roleImplement;

    @GetMapping("getAllRole")
    public ResponseEntity<ResponseObj> getAllRole(){
        return  roleImplement.getAllRole();
    }

    @PostMapping("createRole")
    public ResponseEntity<ResponseObj> createRole (@RequestBody RoleDTO roleDTO){
        return roleImplement.createRole(roleDTO);
    }

    @PutMapping("updateRole/{roleId}")
    public ResponseEntity<ResponseObj> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO){
        if (roleDTO.getRoleName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "roleName không được để trống");
        }

        return roleImplement.updateRole(roleId, roleDTO);
    }
}
