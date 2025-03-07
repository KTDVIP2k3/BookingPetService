package vn.fpt.tranduykhanh.bookingservicepetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Role;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.RoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleEnum roleEnum);
}
