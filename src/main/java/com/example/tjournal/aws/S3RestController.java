package com.example.tjournal.aws;

import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.inif.IResponseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/aws")
public class S3RestController implements IResponseController {
    @Autowired
    private S3ImageService s3ImageService;

    @PostMapping("/s3/upload")
    public ResponseEntity<?> s3Upload(@RequestPart(value = "image", required = false) MultipartFile image){
        String profileImage = s3ImageService.upload(image);
        return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "성공", profileImage);
    }
}