package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.services.FileUploadService;
import org.choongang.global.exceptions.RestExceptionProcessor;
import org.choongang.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController // rest 형태로 처리할거임, 주로 자바스크립트로 처리할 것 같아서
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements RestExceptionProcessor {
    // RestExceptionProcessor : json형태로 에러 응답?

    private final FileUploadService uploadService;

    @PostMapping("/upload") // @RequestPart : 어떤 필드(form의 name값)에서 넘어오는지 알려주기 = 라이크 @RequestParam
    public ResponseEntity<JSONData> upload(@RequestPart("file") MultipartFile[] files, @RequestParam(name = "gid", required = false) String gid, @RequestParam(name = "location", required = false) String location) {


        List<FileInfo> items = uploadService.upload(files, gid, location);
        HttpStatus status = HttpStatus.CREATED;
        JSONData data = new JSONData(items);
        data.setStatus(status);

        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
}
