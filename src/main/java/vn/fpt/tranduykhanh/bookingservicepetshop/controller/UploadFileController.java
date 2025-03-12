package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.UpLoadImageFileInterface;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.UploadImageFileService;

import java.io.IOException;

//@RequestMapping("/api/upload")
//@RestController
@RequiredArgsConstructor
@Tag(name = "Upload", description = "API để upload file")
public class UploadFileController {
    private final UploadImageFileService uploadImageFile;


//    @Operation(summary = "Upload một file ảnh")
//    @PostMapping(value = "/image", consumes = "multipart/form-data")
//    public String uploadImage(
//            @RequestParam("file") MultipartFile file) throws IOException {
//        return uploadImageFile.uploadImage(file);
//    }

//    @Operation(summary = "Update ảnh mới và xóa ảnh cũ")
//    @PostMapping(value = "/update-image", consumes = "multipart/form-data")
//    public ResponseEntity<?> updateImage(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("oldPublicId") String oldPublicId) {
//        try {
            // Kiểm tra nếu file null
//            if (file == null || file.isEmpty()) {
//                return ResponseEntity.badRequest().body("File không được để trống.");
//            }

            // Kiểm tra loại file (chỉ cho phép ảnh)
//            String contentType = file.getContentType();
//            if (contentType == null || !contentType.startsWith("image/")) {
//                return ResponseEntity.badRequest().body("Chỉ hỗ trợ upload file ảnh.");
//            }

            // Kiểm tra dung lượng file (giới hạn 5MB)
//            long maxSize = 5 * 1024 * 1024;
//            if (file.getSize() > maxSize) {
//                return ResponseEntity.badRequest().body("Dung lượng file tối đa là 5MB.");
//            }

//            String newImageUrl = uploadImageFile.updateImage(file, oldPublicId);
//            return ResponseEntity.ok(newImageUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Lỗi khi update ảnh: " + e.getMessage());
//        }
//    }
}
