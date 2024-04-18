package com.kh.spring.member.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {
	
	//로그인서비스
	Member loginMember(Member member);

	//id check를 위한 서비스
	int idCheck(String checkId);

	
	// 회원가입
	int insertMember(Member member);
	
	// 회원수정
	int updateMember(Member member);
	
	// 회원탈퇴
	int deleteMember(String userId);

}
