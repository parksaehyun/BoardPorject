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

    // 파일업로드는  post형태로만 넘어옴
    @PostMapping("/upload")
    public ResponseEntity<JSONData> upload(@RequestPart("file") MultipartFile[] files, @RequestParam(name = "gid", required = false) String gid, @RequestParam(name = "location", required = false) String location) {
        // MultipartFile[] files : 넘어온 파일데이터가 담기는 곳, 파일은 여러개니까 배열로
        // @RequestPart("form의 name값") : 어떤 필드(form의 name값)에서 파일이 넘어오는지 알려주어야 함 = 라이크 @RequestParam("naem값")
        // required = false : gid, location이 없는 경우 null값을 넣어주는 역할

        List<FileInfo> items = uploadService.upload(files, gid, location);
        HttpStatus status = HttpStatus.CREATED;
        JSONData data = new JSONData(items);
        data.setStatus(status);

        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
}
