package com.sbs.java.crud.dto;

public class Article extends Dto {
	public String title;
	public String body;
	public int hit;
	public int memberId; // 작성자의 멤버 id

	public Article(int id, String regDate, int memberId, String title, String body) {
		this(id, regDate, memberId, title, body, 0);
	}

	public Article(int id, String regDate, int imemberId, String title, String body, int hit) {
		this.id = id;
		this.regDate = regDate;
		this.memberId = imemberId;
		this.title = title;
		this.body = body;
		this.hit = hit;
	}

	public void increaseHit() {
		hit++;
	}
}