package org.choongang.file.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.exceptions.FileNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response; // 응답헤더를 통해 파일을 다운받음

    // 파일을 불러오고 응답헤더를 통해 파일을 다운받음
    public void download(Long seq) {
        FileInfo data = infoService.get(seq); // 파일 조회

        String filePath = data.getFilePath(); // 2차가공한 데이터 가져오기
        String fileName = new String(data.getFileName().getBytes(), StandardCharsets.ISO_8859_1); // 파일이름 가져오기, 한글깨짐 방지하기 위해 2byte로 변환

        File file = new File(filePath); // 파일이 없으면 예외 던지기
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        String contentType = data.getContentType();
        contentType = StringUtils.hasText(contentType) ? contentType : "application/octet-stream"; // 콘텐트 타입이 없는 경우 고정해줄 값

        // 파일 가져오기
        try (FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis)) {

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName); // 응답헤더에 파일 넣어주기
            response.setContentType(contentType);
            response.setIntHeader("Expires", 0); // 만료시간 x
            response.setHeader("Cache-Control", "must-revalidate"); // 캐시 비우기
            response.setContentLengthLong(file.length()); // 현재 파일 용랑

            OutputStream out = response.getOutputStream();
            out.write(bis.readAllBytes());
            // 응답헤더에 정의하고 // 바디에서 출력

            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
