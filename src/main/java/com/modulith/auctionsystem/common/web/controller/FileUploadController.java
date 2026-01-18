package com.modulith.auctionsystem.common.web.controller;

import com.modulith.auctionsystem.common.domain.FileUploadService;
import com.modulith.auctionsystem.common.web.response.GenericApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Upload", description = "APIs for uploading files to Cloudinary")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FileUploadController {

    FileUploadService fileUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file", description = "Uploads a file to Cloudinary and returns the URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file", content = @Content),
            @ApiResponse(responseCode = "500", description = "Upload failed", content = @Content)
    })
    public ResponseEntity<GenericApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileUploadService.uploadFile(file);
            return ResponseEntity.ok(GenericApiResponse.success("File uploaded successfully", url));
        } catch (Exception e) {
            log.error("File upload failed", e);
            return ResponseEntity.internalServerError()
                    .body(GenericApiResponse.error("File upload failed: " + e.getMessage(), 500));
        }
    }
}
