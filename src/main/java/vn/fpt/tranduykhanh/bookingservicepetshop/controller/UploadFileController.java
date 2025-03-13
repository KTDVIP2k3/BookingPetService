package vn.fpt.tranduykhanh.bookingservicepetshop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import vn.fpt.tranduykhanh.bookingservicepetshop.services.UploadImageFileService;

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
