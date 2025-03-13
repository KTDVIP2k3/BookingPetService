package vn.fpt.tranduykhanh.bookingservicepetshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fpt.tranduykhanh.bookingservicepetshop.model.Payment;
import vn.fpt.tranduykhanh.bookingservicepetshop.Enum.PaymentMethodEnum;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPaymentMethodName(PaymentMethodEnum paymentMethodName);
}
