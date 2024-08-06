package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.constants.FileStatus;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.repositories.FileInfoRepository;
import org.choongang.global.exceptions.UnAuthorizedException;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileDeleteService {

    // 본인이 올린파일만 삭제해야함
    // 관리자는 모든 파일 삭제 가능
    // 비회원도 게시글을 작성함 -> 비회왼이 게시글을 수정하거나 삭제할 수 있는 권한이 있내 없냐
    // 비밀번호가 기타 인증할 수 있는 수단이 있음
    // 그걸로 지금 인증을 받은 비회원이 게시글을 수정하고 삭제할 수 있는 궈노한이 있음
    //  gid가지고 해당하는 그룸작업을 찾아보고 수정가능한 세션? 인증받은 형태이면 삭제가능하게 할거임 -> 나중에 할거임
    // 지금은 회원과 관리자만 하고 비회원은 나중에 기능 구현할거임
    // 삭제한 게시글이 필요할 때가 있음 -> 자바스크립트로 동적으로 제거함
    // 삭제한 파일정보도 반환값으로 내보낼 생각임

    private final FileInfoService infoService;
    private final FileInfoRepository infoRepository;
    private final MemberUtil memberUtil;

    // FileInfo : 삭제한 파일정보도 반환값으로 내보낼 생각임

    // ✨낱개 삭제
    public FileInfo delete(Long seq) {
        FileInfo data = infoService.get(seq); // 파일 조회하기
        String filePath = data.getFilePath();
        String createdBy = data.getCreatedBy(); // 파일을 업로드한 회원 이메일

        Member member = memberUtil.getMember(); // 로그인한 회원정보 가져오기

        // 관리자가 아닐때라면 + 회원이 올린파일이 있다면 + 현재 로그인 상태라면 + 해당회원이 올린파일이 맞다면
        if (!memberUtil.isAdmin() && StringUtils.hasText(createdBy) && memberUtil.isLogin() && !member.getEmail().equals(createdBy)) {
            throw new UnAuthorizedException();
        }

        // 파일 삭제
        File file = new File(filePath);
        if (file.exists()) { // 파일이 있을 때 파일 삭제 // 파일이 없는데 삭제하는 경우 예외가 발생함
            file.delete();
        }
        // 파일 정보 삭제
        infoRepository.delete(data);
        infoRepository.flush();

        return data; // 삭제한 파일정보 반환값으로 내보냄
    }

    // ✨전체 삭제
    public List<FileInfo> delete(String gid, String location, FileStatus status) {
       List<FileInfo> items = infoService.getList(gid, location, status); // 파일 조회
        items.forEach(i -> delete(i.getSeq()));

        return items;
    }

    // ✨전체 삭제(status : 완료든 미완료든 전체 삭제해야 함)
    public List<FileInfo> delete(String gid, String location) {
        return delete(gid, location, FileStatus.ALL);
    }

    // ✨전체 삭제(gid만을 가지고 파일 전체 삭제)
    public List<FileInfo> delete(String gid) {
        return delete(gid, null);
    }
}
