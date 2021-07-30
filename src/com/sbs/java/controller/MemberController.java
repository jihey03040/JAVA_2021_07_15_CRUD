package com.sbs.java.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sbs.java.crud.dto.Member;
import com.sbs.java.crud.util.Util;

public class MemberController extends Controller {

	private Scanner sc;
	private List<Member> members;
	private String command;
	private String actionMethodName;
	private Member loginedMember;

	public MemberController(Scanner sc) {
		this.sc = sc;

		members = new ArrayList<Member>();
	}

	public void doAction(String command, String actionMethodName) {	// 명령어와 실행할 메소드의 이름을 구별할 메개변수
		this.command = command;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {	// MethodName에 들어있는것에 따라 실행할 메소드가 정해진다.
		case "join":		// join이 들어있다면 doJoin 메소드가 실행된다.
			doJoin();		// doJoin은 login id와 pw를 생성하는 메소드
			break;
		case "login":	
			doLogin();
			break;
		case "logout":
			doLogout();
			break;
		default:		// 위의 것들과 일치하지 않는다면 정해진 명령어가 아니므로.
			System.out.println("존재하지 않는 명령어입니다.");
			break;
		}
	}

	
	private void doLogout() {
		
		if(loginedMember == null) {
			System.out.println("로그인이 되어있지 않습니다.");
		} else if (loginedMember != null) {
			loginedMember = null;
			System.out.println("로그아웃 되었습니다.");
		}
		
		
	}

	public void doLogin() {

		if(loginedMember != null) {
			System.out.println("이미 로그인 되어있습니다.");
			return;
		}

		System.out.println("로그인 아이디 : ");
		String loginId = sc.nextLine();
		System.out.println("로그인 비밀번호 : ");
		String loginPw = sc.nextLine();

		Member member = getMemberByLoginId(loginId);	// loginId를 매개변수로 주고 얻어온 value를 member에 넣어준다.

		if (member == null) {		// member의 값이 null이라면(loginId가 존재하지 않는다면)
			System.out.println("해당 회원은 존재하지 않습니다.");
			return;
		}		
		if (member.loginPw.equals(loginPw) == false) {	// member에 있는 loginPw와 입력받은 loginPw가 일치하지 않는다면
			System.out.println("비밀번호를 확인해주세요");
			return;
		}

		loginedMember = member;	// member를 logined에 넣어준다.
		
			
		System.out.printf("로그인 성공!, %s님 환영합니다.\n", loginedMember.name);

		// 입력받은 아이디에 해당하는 회원이 존재하는지 체크
	}

	private Member getMemberByLoginId(String loginId) {	// 매개로 받은 loginId를 IndexBy의 매개변수로 넣어준다.
		int index = getMemberIndexByLoginId(loginId);	// loginId를 매개변수로 받고 나온 리턴값을 index에 넣어준다.

		if (index == -1) {	// index가 -1이라면 (loginId가 일치하지 않는다면) null을 리턴

			return null;
		} 
		return members.get(index);	// index가 -1가 아니라면(loginId가 일치하는 것이 있다면) members에 index번째의 value를 얻어온다.
	}

	private int getMemberIndexByLoginId(String loginId) {	// 매개변수 loginId를 가져온다
		int i = 0;		// i는 0으로 초기화
		for (Member member : members) {		// i를 증가시키며 member를 돌리는 중 loginId가 매개로 받은 loginId와 같을 경우 i를 리턴
			if (member.loginId.equals(loginId)) {
				return i;
			}
			i++;
		}

		return -1;		// member에 loginId가 매개로 받은 loginId와 일치하지 않을경우 -1 리턴
	}

	private boolean isJoinableLoginId(String loginId) {	// 받은 매개변수를 다시 IndexBy 함수의 매개변수로 넣어준다
		int index = getMemberIndexByLoginId(loginId);	// IndexBy에서 리턴한 값을 index에 넣어준다.

		if (index == -1) {		// index가 -1일 경우(members에 같은 id가 없을 경우) true를 리턴
			return true;
		}

		return false;	// index가 -1이 아닐 경우(members에 일치하는 id가 있을 경우) false를 리턴
	}

	private void doJoin() {	// login id와 pw를 생성하는 메소드

		int id = members.size() + 1;	// id는 members size(0부터 시작)의 +1
		String regDate = Util.getNowDateStr();	// 만든 날자

		String loginId = null;	// loginId 초기화

		while (true) {
			System.out.printf("로그인 아이디 : ");
			loginId = sc.nextLine();	

			if (isJoinableLoginId(loginId) == false) {	// 받은 매개변수로 리턴한 값이 false인 경우(members에 일치하는id가 있을 경우)
				System.out.printf("%s는(은) 이미 사용중인 아이디 입니다.\n", loginId);
				continue;
			}

			break;
		}

		String loginPw = null;			// loginPw를 초기화
		String loginPwConfirm = null;	// 초기화

		while (true) {

			System.out.printf("로그인 비밀번호 : ");
			loginPw = sc.nextLine();

			System.out.printf("로그인 비밀번호 확인: ");
			loginPwConfirm = sc.nextLine();

			if (loginPw.equals(loginPwConfirm) == false) {		// 입력한 loginPw가 재확인 하는 Pw와 같지 않을경우 다시입력.
				System.out.println("비밀번호를 다시 입력해주세요.");
				continue;
			}
			break;		// 입력한 loginPw가 재확인 하는 Pw와 같을 경우. 넘어간다
		}
		
		System.out.printf("이름 : ");
		String name = sc.nextLine();

		Member member = new Member(id, regDate, loginId, loginPw, name);	// 입력받은 변수들을 member에 넣는다.
		members.add(member);	// members에 member을 맨 뒤에 추가한다.

		System.out.printf("%d번 회원이 생성되었습니다.\n", id);

	}
	public void testLogin() {
		members.add(new Member(1,Util.getNowDateStr(), "1111", "1111", "멤버1"));
		members.add(new Member(2,Util.getNowDateStr(), "2222", "2222", "멤버2"));
		members.add(new Member(3,Util.getNowDateStr(), "3333", "3333", "멤버3"));
	}
}
