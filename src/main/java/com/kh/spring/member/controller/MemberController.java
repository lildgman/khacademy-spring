package com.kh.spring.member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.spring.member.model.vo.Member;
import com.kh.spring.member.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
//	private MemberService memberService = new MemberServiceImpl();
	
	//암호화에 필요한 객체
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	/*
	 * 기존 객체 생성 방식
	 * 객체 간 결합도가 높아진다(소스코드 수정이 일어날 경우 하나하나 전부 다 바꿔줘야한다)
	 * 서비스가 동시에 매우 많은 횟수 요청이 될 경우 그만큼 객체가 생성된다.
	 * 
	 * Spring의 DI(Dependency Injection)을 이용한 방식
	 * 객체를 생성해서 주입해준다.
	 * new라는 객체생성 키워드 없이 @Autowired라는 어노테이션 만을 사용해야한다.
	 */
	
	/*
	 * Spring에서 파라미터를 받는 방법
	 * 1. HttpServletRequest를 활용해서 전달받기(jsp/servlet 방식)
	 * 해당 메소드의 매개변수로 HttpServletRequest를 작성해두면
	 * 스프링컨테이너가 해당 메소드를 호출 시 자동으로 객체를 생성해서 매개변수로 주입해준다.
	 */
		
//	@RequestMapping("login.me")
//	public String loginMember(HttpServletRequest request) {
//		String userId = request.getParameter("userId");
//		String userPwd = request.getParameter("userPwd");
//		
//		System.out.println("userId = " + userId);
//		System.out.println("userPwd = " + userPwd);
//		
//		return "main";
//	}
	
	/*
	 * 2.@RequestParam 어노테이션을 이용하는 방법
	 * request.getParameter("키")로 벨류로 추출하는 역할을 대신해주는 어노테이션
	 * value 속성의 벨류로 jsp에서 작성했던 name 속성값을 담으면 알아서 매개변수로 받아올 수 있다.
	 * 만약, 넘어온 값이 비어있는 형태라면 defaultValue 속성으로 기본값을 지정할 수 있다.
	 * 
	 * @RequestParam은 생략이 가능하다.
	 * 
	 */
//	@RequestMapping("login.me")
//	//@RequestParam 생략 가능
//	public String loginMember(@RequestParam(value="userId", defaultValue="testId") String id, @RequestParam(value="userPwd") String pwd) {
//		
//		System.out.println("userId = " + id);
//		System.out.println("userPwd = " + pwd);
//		
//		return "main";
//		//WEB-INF/views/main.jsp
//	}
	/*
	 * 3. 커맨드 객체 방식
	 * 
	 * 해당 메소드 매개변수로 요청 시 전달값을 담고자하는 VO 클래스 타입을 세팅 후
	 * 요청시 전달값의 키값(jsp의 name 속성값)을 vo클래스에 담고자하는 필드명으로 작성하면 된다.
	 */
//	@RequestMapping("login.me")
//	//@RequestParam 생략 가능
//	public String loginMember(Member member) {
//		
//		System.out.println("userId = " + member.getUserId());
//		System.out.println("userPwd = " + member.getUserPwd());
//		
//		Member loginUser = memberService.loginMember(member);
//		
//		if(loginUser == null) {
//			System.out.println("로그인 실패");
//		} else {
//			System.out.println("로그인 성공");
//		}
//		
//		return "main";
//		//WEB-INF/views/main.jsp
//	}
	
	/*
	 * 요청 처리 후 응답 데이터를 담고 응답페이지로 포워딩 또는 url 재요청 처리하는 방법
	 * 
	 * 1. 스프링에서 제공하는 Model 객체를 이용하는 방법
	 * 포워딩할 응답뷰로 전달하고자하는 데이터를 맵 형식(Key, Value)으로 담을 수 있는 영역
	 * Model 객체는 requestScope 객체를 사용한다.
	 * request.setAttribute() => model.addAttribute();
	 * 
	 */
	
	@RequestMapping("login.me")
	//@RequestParam 생략 가능
	public String loginMember(Member member, Model model, String saveId ,HttpSession session, HttpServletResponse response) {	
		// 암호화 전
//		Member loginUser = memberService.loginMember(member);
//		
//		if(loginUser == null) { // 로그인 실패 => 에러문구를 requestScope에 담고 에러페이지로 forwording
//			model.addAttribute("errorMsg", "로그인 실패");
//			
//			// 에러 페이지 위치: /WEB-INF/views/common/errorPage.jsp
//			// servlet-context를 참고하자!
//			return "common/errorPage";
//			
//		} else { //로그인 성공 => sessionScope에 로그인 유저 담아서 메인으로 url 재요청
//			session.setAttribute("loginUser", loginUser);
//			
//			return "redirect:/";
//		}
		
		// 암호화 후
		// Member member의 id => 사용자가 입력한 아이디
		// member의 pwd => 사용자가 입력한 pwd(평문)
		
		Member loginUser = memberService.loginMember(member);
		
		// loginUser의 id => 아이디로 db에서 검색해온 id
		// loginUser Pwd => db에 기록된 암호화된 비밀번호
		
		// bcryptPasswordEncoder 객체의 matches() 이용
		// matchs(평문, 암호문)을 작성하면 내부적으로 복호화 작업 후 비교
		// 두 구문이 일치하면 true, 일치하지 않으면 false
		bcryptPasswordEncoder.matches(member.getUserPwd(), loginUser.getUserPwd());
		
		if(loginUser == null) { // 아이디가 없는 경우
			model.addAttribute("errorMsg","일치하는 아이디가 없습니다.");
			return "common/errorPage";
		} else if (!bcryptPasswordEncoder.matches(member.getUserPwd(), loginUser.getUserPwd())) {
			// 비밀번호가 다른 경우
			model.addAttribute("errorMsg","비밀번호가 일치하지 않습니다..");
			return "common/errorPage";
		} else {
			// 아이디, 비밀번호 일치 => 성공
			Cookie ck = new Cookie("saveId", loginUser.getUserId());
			if(saveId == null) {
				ck.setMaxAge(0);
			}
			
			response.addCookie(ck);
			
			session.setAttribute("loginUser", loginUser);
			return "redirect:/";
		}

	}
	
	//2. 스프링에서 제공하는 ModelAndView 객체 사용
	
//	@RequestMapping("login.me")
//	//@RequestParam 생략 가능
//	public ModelAndView loginMember(Member member, ModelAndView mv, HttpSession session) {
//
//		Member loginUser = memberService.loginMember(member);
//		
//		if(loginUser == null) { // 로그인 실패 => 에러문구를 requestScope에 담고 에러페이지로 forwording
//
//			mv.addObject("errorMsg", "로그인 실패");
//			// 에러 페이지 위치: /WEB-INF/views/common/errorPage.jsp
//			// servlet-context를 참고하자!
//			mv.setViewName("common/errorPage");
//			
//		} else { //로그인 성공 => sessionScope에 로그인 유저 담아서 메인으로 url 재요청
//			session.setAttribute("loginUser", loginUser);
//			
//			mv.setViewName("redirect:/");
//		}
//		
//		return mv;
//	}
	
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		// 로그아웃 -> session에서 loginUser 삭제 or 만료
		
		session.removeAttribute("loginUser");
//		session.invalidate(); // 만료
		return "redirect:/";
	}
	
	@RequestMapping("enrollForm.me")
	public String enrollForm() {
		return "member/memberEnrollForm";
	}
	
	// idCheck ajax 요청을 받아줄 controller
	/*
	 * ajax 요청에 대한 응답을 위한 controller에는 @ResponseBody 어노테이션을 작성해줘야 한다.
	 * 기본적인 세팅이 jsp 응답으로 되어있기 때문에 @ResponseBody를 작성해주면
	 * 반환값을 http 응답 객체에 직접 작성하겠다 의미를 가지고 있음
	 */
	
	@ResponseBody // 리턴을 응답바디에 담아주겠다
	@RequestMapping("idCheck.me")
	public String idCheck(String checkId) {
		int result = memberService.idCheck(checkId);
		
		if(result > 0) { // 이미 존재한다면
			return "NNNNN";
		} else { // 존재하지 않으면
			return "NNNNY";
		}
	}
	
	@RequestMapping("insert.me")
	public String insertMember(Member member, HttpSession session, Model model) {
		/*
		 * 1. 한글깨짐문제 발생 => web.xml에 스프링에서 제공하는 인코딩 필터 등록
		 * 2. 나이를 입력하지 않을 경우 int 자료형에 빈문자열을 대입해야하는 경우가 발생한다.
		 * => 400 에러 발생 Member의 age필드 자료형을 String으로 변경해주면 된다.
		 * 3. 비밀번호가 사용자 입력이 그대로 전달이 된다
		 * Bcrypt 방식을 이용해서 암호화를 한 후 저장 하자
		 * => 스프링 시큐리티에서 제공하는 모듈을 이용하자.
		 * => <pom.xml에 라이브러리 추가> 
		 */
		
		//암호화 작업
		String encPwd = bcryptPasswordEncoder.encode(member.getUserPwd());
		
		member.setUserPwd(encPwd);
		

		int result = memberService.insertMember(member);
		
		if(result > 0) {
			session.setAttribute("alertMsg", "성공적으로 회원가입이 완료되었습니다.");
			return "redirect:/";
		} else {
			model.addAttribute("errorMsg","회원가입 실패");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("myPage.me")
	public String myPage() {
		return "member/myPage";	
	}
	
	@RequestMapping("update.me")
	public String updateMember(Member member, HttpSession session, Model model) {
		
		int result = memberService.updateMember(member);
		
		if(result > 0) { 
			session.setAttribute("loginUser", memberService.loginMember(member));
			session.setAttribute("alertMsg", "회원정보 수정 성공");
			return "redirect:/myPage.me";
		} else {
			model.addAttribute("errorMsg", "회원정보 수정 실패");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("delete.me")
	public String deleteMember(Member member, HttpSession session) {
		
		// 1. 암호화된 비밀번호를 가져오자
		String encPwd =((Member)session.getAttribute("loginUser")).getUserPwd();
		
		// 2. 비밀번호가 일치? 불일치?
		if(bcryptPasswordEncoder.matches(member.getUserPwd(), encPwd)) {
			int result = memberService.deleteMember(member.getUserId());
			
			if(result > 0) {
				// 3. 일치하면 탈퇴처리 => session에서 제거 -> main페이지로 ㄱㄱ
				session.removeAttribute("loginUser");
				session.setAttribute("alertMsg","회원탈퇴가 성공적으로 이루어졌습니다..");
				return "redirect:/";
				
			} else {
				session.setAttribute("alertMsg","탈퇴처리를 실패하였습니다.");
				return "redirect:/myPage.me";
			}
					
		} else {
			// 4. 불일치 시 => alertMsg: 비밀번호 다시 입력 -> myPage
			session.setAttribute("alertMsg","비밀번호를 다시 입력해주세요");
			return "redirect:/myPage.me";
		}
			
	}
}
