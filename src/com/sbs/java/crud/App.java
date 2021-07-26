package com.sbs.java.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sbs.java.controller.ArticleController;
import com.sbs.java.controller.MemberController;
import com.sbs.java.crud.dto.Article;
import com.sbs.java.crud.dto.Member;
import com.sbs.java.crud.util.Util;

public class App {

	private List<Article> articles;
	private List<Member> members;

	public App() {
		articles = new ArrayList<>();
		members = new ArrayList<>();
	}

	public void start() {
		System.out.println("== 프로그램 시작 ==");

		makeTestData();

		Scanner sc = new Scanner(System.in);

		MemberController memberconteroller = new MemberController(sc, members);

		while (true) {
			System.out.printf("명령어) ");
			String command = sc.nextLine();
			command = command.trim();
			
			ArticleController articlecontroller = new ArticleController(sc, articles,command);

			if (command.length() == 0) {
				continue;
			}

			if (command.equals("system exit")) {
				break;
			}
			if (command.equals("member join")) {
				memberconteroller.doJoin();

			} else if (command.equals("article write")) {
				articlecontroller.doWrite();

			} else if (command.startsWith("article list")) {
				articlecontroller.doList();

			} else if (command.startsWith("article detail ")) {
				articlecontroller.doDetail();

			} else if (command.startsWith("article modify ")) {
				articlecontroller.doModify();

			} else if (command.startsWith("article delete ")) {
				articlecontroller.doDelete();

			} else {
				System.out.printf("%s(은)는 존재하지 않는 명령어 입니다.\n", command);
			}
		}

		sc.close();

		System.out.println("== 프로그램 끝 ==");
	}

	private void makeTestData() {
		System.out.println("테스트를 위한 데이터를 생성합니다.");

		articles.add(new Article(1, Util.getNowDateStr(), "제목1", "내용1", 11));
		articles.add(new Article(2, Util.getNowDateStr(), "제목2", "내용2", 22));
		articles.add(new Article(3, Util.getNowDateStr(), "제목3", "내용3", 33));

	}

}