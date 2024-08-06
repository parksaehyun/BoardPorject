package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.services.FileDeleteService;
import org.choongang.file.services.FileDownloadService;
import org.choongang.file.services.FileInfoService;
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
    private final FileInfoService infoService;
    private final FileDownloadService downloadService;
    private final FileDeleteService deleteService;

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

    @GetMapping("/download/{seq}") // 반환값 필요 없음 출력은 ... 못들음
    public void download(@PathVariable("seq") long seq) {
        downloadService.download(seq);
    }

    @DeleteMapping("/delete/{seq}")
    public JSONData delete(@PathVariable("seq") long seq) {
        FileInfo data = deleteService.delete(seq);

        return new JSONData(data); // 제이슨형태로 출력
    }

    // gid는 필수라서 = 패스배리어블에 넣음 -> gid만 있으면 전체삭제?
    // location은 필수아님, 있으면 쓰고 없으면 안쓸거임 = 리퀘스트파람에 넣음
    // 좀더 응용하면 패스배리어블만으로도 가능하다고 그때는 required = false 로 해주라고...
    @DeleteMapping("/deletes/{gid}")
    public JSONData deletes(@PathVariable("gid") String gid, @RequestParam (name="location", required = false) String location) {
        List<FileInfo> items = deleteService.delete(gid, location);

        return new JSONData(items);
    }

    // 파일 등록번호를 가지고 개별 조회
    @GetMapping("/info/{seq}")
    public JSONData info(@PathVariable("seq") Long seq) {
        FileInfo data = infoService.get(seq);

        return new JSONData(data);
    }

    // 파일 목록 조회
    @GetMapping("/list/{gid}")
    public JSONData list(@PathVariable("gid") String gid, @RequestParam (name="location", required = false) String location) {
        // gid는 필수라서 = 패스배리어블에 넣음
        // location은 필수아님, 있으면 쓰고 없으면 안쓸거임 = 리퀘스트파람에 넣음 = 쿼리스트링 형태
        List<FileInfo> items = infoService.getList(gid, location);

        return new JSONData(items);
    }
}
