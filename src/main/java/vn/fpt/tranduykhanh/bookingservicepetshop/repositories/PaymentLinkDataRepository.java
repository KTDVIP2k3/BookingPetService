package vn.fpt.tranduykhanh.bookingservicepetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler;
import org.springframework.stereotype.Repository;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.PaymentLinkData;

@Repository
public interface PaymentLinkDataRepository extends JpaRepository<PaymentLinkData, Long> {
}
