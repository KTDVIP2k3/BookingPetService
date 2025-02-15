package vn.fpt.tranduykhanh.bookingservicepetshop.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


public class ServiceDTO {
    private String serviceName;

    private String serviceDescription;

    private double servicePrice;

    private MultipartFile imageService;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public MultipartFile getImageService() {
        return imageService;
    }

    public void setImageService(MultipartFile imageService) {
        this.imageService = imageService;
    }
}
