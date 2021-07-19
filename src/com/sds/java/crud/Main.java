package com.sds.java.crud;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		System.out.println("=========프로그램 시작=========");

		Scanner sc = new Scanner(System.in);

		int lastArticleId = 0;

		ArrayList<Article> articles = new ArrayList<Article>();
		SimpleDateFormat adf = new SimpleDateFormat("yyyy/MM/dd  HH:mm");

		while (true) {
			System.out.print("\n명령어) ");
			String command = sc.nextLine().trim();

			command = command.trim();

			if (command.length() == 0) {
				continue;
			}
			if (command.equals("system exit")) {
				break;
			}
			if (command.equals("article write")) {
				int id = lastArticleId + 1;
				lastArticleId = id;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				Date arDate = new Date();

				Article article = new Article(id, title, body, arDate);
				articles.add(article);

				System.out.println("\n" + id + "번 글이 생성 되었습니다.");
			} else if (command.equals("article list")) {

				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다.");
					continue;
				}
				System.out.println("번호 | 제목");
				for (int i = 0; i < articles.size(); i++) {
					Article article = articles.get(i);

					System.out.printf("%2d  |   %s\n", article.id, article.title);
				}
			} else if (command.startsWith("article detail ")) {
				String[] commandBits = command.split(" ");

				int id = Integer.parseInt(commandBits[2]);

				boolean foundArticle = false;

				for (int i = 0; i < articles.size(); i++) {
					Article article = articles.get(i);

					if (article.id == id) {
						foundArticle = true;
						break;
					}
				}

				if (foundArticle == false) {
					System.out.println(id + "번 게시물은 존재하지 않습니다.");
					continue;
				} else {
					System.out.printf("번호 : %d\n제목 : %s\n내용 : %s\n게시한 날짜 : %s", id, articles.get(id - 1).title,
							articles.get(id - 1).body, adf.format(articles.get(id - 1).articleDate));
					continue;
				}
			} else {
				System.out.printf("%s는(은) 존재하지 않는 명령어 입니다.\n", command);
			}
		}
		sc.close();
		System.out.println("=========프로그램 끝=========");
		 
	}
}

class Article {
	int id;
	String title;
	String body;
	Date articleDate;

	public Article(int id, String title, String body, Date arDate) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.articleDate = arDate;
	}
}