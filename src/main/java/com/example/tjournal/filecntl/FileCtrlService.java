package com.example.tjournal.filecntl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileCtrlService {
    @Value("${spring.servlet.multipart.location}")
    private String uploadDir; // @Value 설정된 경로 값이 자동으로 주입됨  yml 파일에 설정된 이미지 저장 경로

    private static final String UPDIR = "C:\\Temp\\upload\\board\\";

    public String getFullPath(String filename) {
        return UPDIR  + filename;
    }


    private void checkDirectory(String directory) throws IOException {
        Path path = Paths.get(directory);
        if ( !Files.exists(path) ) {
            Files.createDirectories(path);
        }
    }

    public Boolean saveFile(MultipartFile file, String tbl, String destFileName) throws IOException {
        if ( tbl == null || tbl.isEmpty() || file == null ) {
            return false;
        }
        this.checkDirectory(uploadDir + "/" + tbl);
        Files.copy( file.getInputStream(), Path.of(uploadDir + "/" + tbl + "/" + destFileName) );
        return true;
    }

    public byte[] downloadFile(String tbl, String uniqName) {
        byte[] bytes = null;
        try {
            this.checkDirectory(uploadDir + "/" + tbl);
            Path path = Paths.get(uploadDir + "/" + tbl + "/" + uniqName);
            bytes = Files.readAllBytes(path);
        } catch (IOException ex) {
            log.error(ex.toString());
        }
        return bytes;
    }

    public byte[] downloadFile(String tbl, String uniqName, String fileType) {
        return downloadFile(tbl, uniqName + fileType);
    }

    public Boolean deleteFile(String tbl, String uniqName, String fileType) {
        try {
            this.checkDirectory(uploadDir + "/" + tbl);
            Path path = Paths.get(uploadDir + "/" + tbl + "/" + uniqName + fileType);
            Files.delete(path);
        } catch (IOException ex) {
            log.error(ex.toString());
            return false;
        }
        return true;
    }
}
