package vn.fpt.tranduykhanh.bookingservicepetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PetOptionalService;

@Repository
public interface PetOptionalServiceRepository extends JpaRepository<PetOptionalService, Long> {
}
