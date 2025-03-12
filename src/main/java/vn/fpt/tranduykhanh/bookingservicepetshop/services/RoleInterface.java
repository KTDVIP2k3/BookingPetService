package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.http.ResponseEntity;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Role;
import vn.fpt.tranduykhanh.bookingservicepetshop.request.RoleDTO;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

public interface RoleInterface {
    ResponseEntity<ResponseObj> createRole (RoleDTO roleDTO);

    ResponseEntity<ResponseObj> getAllRole();

    ResponseEntity<ResponseObj> updateRole(Long roleId, RoleDTO roleDTO);
}
