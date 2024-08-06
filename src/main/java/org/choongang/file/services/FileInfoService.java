package org.choongang.file.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.file.constants.FileStatus;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.entities.QFileInfo;
import org.choongang.file.exceptions.FileNotFoundException;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService {

    private final FileInfoRepository infoRepository;
    private final FileProperties properties;
    private final HttpServletRequest request;

    /**
     * 파일 1개 조회
     *
     * @param seq : 파일 등록번호
     * @return
     */
    public FileInfo get(Long seq) {
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFoundException::new);

        /**
         * 2차 가공
         * 1. 파일을 접근할 수 있는 URL - 보여주기 위한 목적
         * 2. 파일을 접근할 수 있는 PATH - 파일 삭제, 다운로드 등등
         */

        addFileInfo(item); // 추가적인 데이터 넣어주기, db랑 상관없이 데이터 넣어주는거 @Trandient fileUrl, filePath // ex) 썸네일

        return item;
    }

    /**
     * 파일 목록 조회
     *
     * @param gid
     * @param location
     * @param status - ALL: 완료 + 미완료, DONE - 완료, UNDONE - 미완료
     * @return
     */
    public List<FileInfo> getList(String gid, String location, FileStatus status) {
        // gid : 하나의 ??? // location : 하나의 상품에 여러이미지, 리스트이미지, 상세이미지...(필수는 아님) // status : 게시글 올리고 이미지가 바로 보일려면 파일이 서버에 올라가 있어야 함 done, undone, all(작업완료 미완료 모두 보기)

        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(fileInfo.gid.eq(gid)); // eq : =

        if (StringUtils.hasText(location)) { // 이미지를 리스트 올 상세페이지 등등...
            andBuilder.and(fileInfo.location.eq(location));
        }

        if (status != FileStatus.ALL) {
            andBuilder.and(fileInfo.done.eq(status == FileStatus.DONE));
        }

        List<FileInfo> items = (List<FileInfo>) infoRepository.findAll(andBuilder, Sort.by(asc("createdAt")));

        // 2차 추가 데이터 처리
        items.forEach(this::addFileInfo);

        return items;
    }

    public List<FileInfo> getList(String gid, String location) {
        // gid를 가장 많이 사용
        // 조회 : done
        // 수정, 등록 : undone
        return getList(gid, location, FileStatus.DONE); // 가장 많이 조회하는게 done이니까 고정
    }

    public List<FileInfo> getList(String gid) {
        return getList(gid, null, FileStatus.DONE);
    }

    /**
     * 파일 정보 추가 처리
     * - fileUrl, filePath
     *
     * @param item
     */
    public void addFileInfo(FileInfo item) {
        String fileUrl = getFileUrl(item);
        String filePath = getFilePath(item);

        item.setFileUrl(fileUrl);
        item.setFilePath(filePath);
    }

    // 브라우저 접근 주소
    public String getFileUrl(FileInfo item) {
        return request.getContextPath() + properties.getUrl() + "/" + getFolder(item.getSeq()) + "/" + getFileName(item);
    }

    // 실제 서버업로드(서버쪽) 파일 경로
    public String getFilePath(FileInfo item) {
        return properties.getPath() + "/" + getFolder(item.getSeq()) + "/" + getFileName(item);
    }

    public String getFolder(long seq) {
        return String.valueOf(seq % 10L);
    }

    public String getFileName(FileInfo item) {
        String fileName = item.getSeq() + Objects.requireNonNullElse(item.getExtension(), "");
        return fileName;
    }
}