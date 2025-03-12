package vn.fpt.tranduykhanh.bookingservicepetshop.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findByPetName(String petName);

    boolean existsByPetName(String petName);
}
