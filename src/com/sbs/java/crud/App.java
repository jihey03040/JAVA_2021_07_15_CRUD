package com.sbs.java.crud;

import java.util.Scanner;

import com.sbs.java.controller.ArticleController;
import com.sbs.java.controller.Controller;
import com.sbs.java.controller.MemberController;

public class App {

	public void start() {
		System.out.println("== 프로그램 시작 ==");


		Scanner sc = new Scanner(System.in);
		
		MemberController memberconteroller = new MemberController(sc);	// 멤버를 관리하는 컨트롤러
		ArticleController articlecontroller = new ArticleController(sc);	// 게시물을 관리하는 컨트롤러

		articlecontroller.makeTestData();	// 테스트용 게시물 데이터
		memberconteroller.testLogin();
	
		while (true) {
			System.out.printf("명령어) ");
			String command = sc.nextLine();	// 명령어 입력
			
			command = command.trim();	
			
			if (command.length() == 0) {	// 명령어를 입력하지 않았다면 다시 실행
				continue;
			}

			if (command.equals("system exit")) {	// 시스템 종료	
				break;
			}
			
			String[] commandBits = command.split(" ");	// 명령어를 공백을 기준으로 잘라서 commandBits 배열에 넣는다 ex) article detail
			
			if(commandBits.length == 1) {	// 명령어를 재대로 입력하지 않은경우(띄어쓰기를 안하고 쓴 경우)
				System.out.println("존재하지 않는 명령어입니다.");
				continue;
			}
			
			String cotrollerName = commandBits[0];		// 잘라서 넣어진 명령어의 첫 단어는 일을 할 컨트롤러의 구분  ex) article
			String actionMethodName = commandBits[1];	// 두번째 단어는 컨트롤러 안의 종류(메소드)의 구분 ex) detail
		
			Controller controller = null;	// 컨트롤러는 초기화를 시켜준다.
		
			if(cotrollerName.equals("article")) {	// 입력된 명령어의 첫 단어가 제대로 입력되었다면 컨트롤러로 넘긴다.
				controller = articlecontroller;
			}else if(cotrollerName.equals("member")) {
				controller = memberconteroller;
			} else {
				System.out.println("존재하지 않는 명령어 입니다.");	// 그게 아니라면 다시 입력하라고 보낸다.
				continue;
			}
			controller.doAction(command, actionMethodName);	// 컨트롤러는 추상클래스의 메소드를 가져온다 반드시 만들어야할것을 강제한다.
		}													// 컨트롤러를 상속받는 모든 하위 컨트롤러는 doAction 메소드를 오버라이딩 해야한다.
															// 명령어와 컨트롤러에 들어가서 사용될 메소드의 이름을 가져간다
		sc.close();

		System.out.println("== 프로그램 끝 ==");
	}

	

}