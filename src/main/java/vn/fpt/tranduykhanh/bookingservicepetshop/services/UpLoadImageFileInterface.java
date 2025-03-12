package vn.fpt.tranduykhanh.bookingservicepetshop.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpLoadImageFileInterface {
    String uploadImage(MultipartFile file) throws IOException;
}
