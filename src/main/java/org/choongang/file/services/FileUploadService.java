package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class) // 밸루 + 플레이스홀더 방식 보다는 직접 범주화해서 사용하기 // 파일경로
public class FileUploadService {

    private final FileInfoRepository fileInfoRepository;
    private final FileInfoService fileInfoService;
    private final FileProperties properties;

    public List<FileInfo> upload(MultipartFile[] files, String gid, String location) {
        /**
         * 1. 파일 정보 저장
         * 2. 파일을 서버로 이동
         * 3. 이미지이면 썸네일 생성
         * 4. 업로드한 파일목록 반환
         */

        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString(); // 없어도 db에 저장되는 값이 있어야 해서 이거 씀

        List<FileInfo> uploadedFiles = new ArrayList<>(); // 업로드 성공한 파일 정보 리스트 담아줄거임 // TDD : 목멀티파트파일 활용하기

        // 1. 파일 정보 저장
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename(); // 업로드한 파일의 실제 이름
            String contentType = file.getContentType(); // 파일 형식 -> 이거가지고 썸네일 만드는데 활용
            String extension = fileName.substring(fileName.lastIndexOf(".")); // ex) image.png -> 끝에서 잘라와서 파일 확장자 가져오기

            FileInfo fileInfo = FileInfo.builder()
                    .gid(gid)
                    .location(location)
                    .fileName(fileName)
                    .extension(extension) // 나중에 파일경로 찾을 때 필요
                    .contentType(contentType)
                    .build();

            // 파일 정보 db에 저장
            fileInfoRepository.saveAndFlush(fileInfo); // 서버가 너무 부담스러워 해서 웹서버와 파일저장공간을 분리하는 경우가 많음


            // 2. 파일을 서버로 이동 // 파일 업로드 시 파일을 10개의 폴더로 나누어 분산 저장하기 위한 로직
            long seq = fileInfo.getSeq(); // seq : 서버에 업로드 될 파일 이름
            String uploadDir = properties.getPath() + "/" + (seq % 10L); // 업로드 경로 // seq 값을 10으로 나눈 나머지를 추가하여 서브 디렉토리를 만듬, 이는 파일을 10개의 폴더로 나누어 분산 저장하기 위함
            File dir = new File(uploadDir); // 위에서 설정한 uploadDir 경로를 사용하여 File 객체를 생성합니다. 이 객체는 업로드될 파일이 저장될 디렉토리를 나타냅니다. 처음에는 해당 폴더가 없을 수 있으므로, 이후의 코드에서 폴더를 생성
            if (!dir.exists() || !dir.isDirectory()) { //이 줄은 해당 경로에 디렉토리가 존재하지 않거나 디렉토리가 아닌 경우를 확인
                dir.mkdir(); // makedir : 디렉토리가 존재하지 않으면 mkdir() 메서드를 사용하여 새로운 디렉토리를 생성합니다. 이를 통해 파일을 분산 저장
            }

            String uploadPath = uploadDir + "/" + seq + extension; // 업로드될 파일의 전체 경로를 설정
            try {
                file.transferTo(new File(uploadPath)); // 파일을 uploadPath 경로로 이동 // 임시폴더나 렘에 있는 파일을 실제 파일경로에 옮기기
                uploadedFiles.add(fileInfo); // 업로드가 성공하면 uploadedFiles 리스트에 fileInfo를 추가 //업로드 성공 파일 정보
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 이동 실패 시 정보 삭제
                fileInfoRepository.delete(fileInfo); // 파일 이동 실패 시 fileInfo를 삭제
                fileInfoRepository.flush();
            }
        }

        uploadedFiles.forEach(fileInfoService::addFileInfo);

        return uploadedFiles; // 업로드된 파일 정보 리스트를 반환
    }
}
