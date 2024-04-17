package com.kh.spring.member.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {
	
	//로그인서비스
	Member loginMember(Member member);

	int idCheck(String checkId);
}
