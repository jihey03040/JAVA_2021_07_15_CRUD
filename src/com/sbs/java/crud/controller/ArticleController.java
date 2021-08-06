package com.sbs.java.crud.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sbs.java.crud.container.Container;
import com.sbs.java.crud.dto.Article;
import com.sbs.java.crud.dto.Member;
import com.sbs.java.crud.util.Util;

public class ArticleController extends Controller {

	private Scanner sc;	// App에서 입력받은 것을 저장할 변수
	private List<Article> articles;	// 게시물 리스트를 저장할 변수
	private String command;
	private String actionMethodName;	// 실행할 메소드를 구분할 명령어의 두번째 자리가 들어간 변수
	MemberController member;

	public ArticleController(Scanner sc,MemberController member) {	// 스캐너로 입력받은것을 가져온다.
		this.sc = sc;		
		// 가져온 스캐너는 따로 저장시킨다.
		articles = Container.articleDao.articles;	// 생성자를 실행하면 만들어진다. 
	}

	public void doAction(String command, String actionMethodName) {	// 커맨드와 실행할 메소드의 이름을 구별할 메개변수를 받아온다.
		this.command = command;						// 각각 만들어둔 변수에 저장한다.
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {		// 메소드를 구별할 두번째 명령어가 들어간 변수가 무엇이냐에 따라 실행하는게 달라진다.
		case "list":					// list가 들어있다면 showList의 메소드가 실행된다. 의 형식
			showList();
			break;
		case "detail":
			showDetail();
			break;
		case "write":
			doWrite();
			break;
		case "delete":
			doDelete();
			break;
		case "modify":
			doModify();
			break;
		default:				// 위에 써있는것이 모두 아니라면 정의되지 않은 명령어다.
			System.out.println("존재하지 않는 명령어입니다.");
			break;
		}
	}
	
	
	private void doWrite() {	// 게시물 작성 메소드
		
		int	id = Container.articleDao.getNewId();
		// get은 (index)번째(사이즈-1 즉, 마지막으로 생성된 value) 위치에 value(게시물)를 얻어온다. 그러니까 마지막 생성된 게시물의 id +1를 doWrite의 id에 넣는것
		String regDate = Util.getNowDateStr();	// 작성 날자와 시간을 가져와서 regDate에 넣는다.
		System.out.printf("제목 : ");
		String title = sc.nextLine();			// 각각 작성한 제목과 내용을 title과 body에 넣는다.
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate, loginedMember.id, title, body, 0);	// 작성이 끝났으면 article을 새로 만들어 각각 변수에 저장한 것들을 넣어준다.
		Container.articleDao.add(article);	// articles에 article를 맨 뒤에 추가한다.

		System.out.printf("%d번 글이 생성되었습니다.\n", id);
	}

	private void showList() {	// 작성된 게시물을 보여주는 메소드		// article list길이만큼 잘라준 다음 앞의 공백을 제거한 문자를 서치키워드에 넣는다.
		String searchKeyword = command.substring("article list".length()).trim();	
		// substring()은 변수에 들어있는 문자열을 ()안의 숫자의 순서부터 잘라주라는 명령어.	// trim()은 문자열의 양 옆에있는 공백을 제거해준다.
		List<Article> forListArticles = articles;	// forList에 articles를 넣는다

		if (searchKeyword.length() > 0) {	// 서치키워드가 있다면	
			
			forListArticles = new ArrayList<>();	// 검색한 단어가 포함된 게시글만 넣은 list를 생성해 forList에 넣어준다.
																		// contains() 문자열에 특정문자를 포함하고 있는지 확인하는 함수. 대소문자를 구분한다.
			for (Article article : articles) {	// 향상된 for문 (int-타입 i-변수명 : list-배열명)
				if (article.title.contains(searchKeyword)) {	// 반복중 title이 서치키워드와 같으면 forList의 맨 뒤에 article을 추가한다.
					forListArticles.add(article);
				}
			}

			if (forListArticles.size() == 0) {	// 검색한 단어와 같은 title이 없다면 forList에 들어가지 못했을 테니 size는 0
				System.out.println("검색결과가 존재하지 않습니다.");
				return;
			}
		}

		if (articles.size() == 0) {	//게시글이 없을 경우
			System.out.println("게시글이 없습니다.");
			return;
		}

		System.out.print("번호 |      작성자 | 조회 | 제목\n");	

		for (int i = forListArticles.size() - 1; i >= 0; i--) {	// (index는 0부터)size -1 = 마지막의 게시글	// 역순으로 불러온다.
			Article article = forListArticles.get(i);
			
			String writerName = null;
			
			List<Member> members = Container.memberDao.members;
			
			for(Member member : members) {
				if(article.memberId == member.id) {
					writerName = member.name;
					break;
				}
			}

			System.out.printf("%4d|%10s|%5d|%4s\n", article.id, writerName, article.hit, article.title);
		}
	}

	private void showDetail() {
		
		int id = articleNumber();
		if(id == 0) return;

		Article foundArticle = getArticleById(id);	// id를 넣고 리턴받은 값을 found에 넣어준다.

		if (foundArticle == null) {		// found의 값이 null이라면 같은 id가 없는것.
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
					// 리턴되지 않고 내려온 found는 articles의 index번째 위치에 value를 얻어왔다.
		foundArticle.increaseHit();		// hit를 실행시킨다(조회수를 +1해준다.)

		System.out.printf("번호 : %d\n", foundArticle.id);		// 얻어온 value를 출력
		System.out.printf("날짜 : %s\n", foundArticle.regDate);
		System.out.printf("제목 : %s\n", foundArticle.title);
		System.out.printf("내용 : %s\n", foundArticle.body);
		System.out.printf("조회수 : %d\n", foundArticle.hit);		// hit는 바로 위에서 한번 실행이 되었기 때문에 +1이 되었다.
	}

	private void doModify() {
		
		int id = articleNumber();
		if(id == 0) return;

		Article foundArticle = getArticleById(id);	// id가 같다면 i값을 같지 않다면 null값을 리턴한 ById의  값을 found에 넣어준다.
		
		
		if (foundArticle == null) {		// 찾는 게시물이 없을 경우
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("게시글은 작성자만 수정할 수 있습니다.");
			return;
		}

		System.out.printf("제목 : ");	// 찾는 게시물이 존재할 경우 제목과 내용을 수정
		String title = sc.nextLine();
		System.out.printf("\n내용 : ");
		String body = sc.nextLine();

		foundArticle.title = title;
		foundArticle.body = body;

		System.out.printf("%d번글이 수정되었습니다.\n", id);
	}

	private void doDelete() {
		
		int id = articleNumber();
		if(id == 0) return;

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		if (foundArticle.memberId != loginedMember.id) {
			System.out.println("권한이 없습니다.");
			return;
		}
		
		articles.remove(foundArticle);	// found의 값이 -1가 아니라면(id가 일치한다면) articles의 found값의 index에 있는 article을 지운다.
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);

	}
	private int getArticleIndexbyId(int id) {
		int i = 0;

		for (Article article : articles) {

			if (article.id == id) {	// 반복중 article id가 불러온 id와 같을때 까지 i는 증가하다가 같다면 리턴시킨다.
				return i;
			}
			i++;	
		}

		return -1;	// article id가 id와 같지 않다면 -1를 리턴시킨다.
	}

	private Article getArticleById(int id) {

		int index = getArticleIndexbyId(id);	// 매개변수로 불러온 id가 IndexbyId함수로 다시 들어갔다가 i 또는 -1를 리턴. index에 넣어준다.

		if (index != -1) {						// index가 -1가 아니라면(article에 id와 맞는 id가 있다면) articles의 index번째 위치에 value를 얻어온다
			return articles.get(index);
		}

		return null;		// IndexbyId가 -1를 리턴했다면 article에 일치하는것이 없다는것 null를 리턴한다.
	}
	
	public int articleNumber() {
		
		String[] commandBits = command.split(" ");	// 커맨드에 있는 문자열을 공백을 기준으로 잘라서 commandBits에 집어넣는다.
		
		if(commandBits.length != 3) {
			System.out.println("존재하지 않는 명령어입니다.");
			return 0;
		}
		int id = Integer.parseInt(commandBits[2]);	// String타입의 commandBits에 있는 숫자를 int타입으로 변환해 id에 넣어준다.
		
		return id;
	}
	
	public void makeTestData() {	// 테스트를 하기 위한 데이터.
		System.out.println("테스트를 위한 데이터를 생성합니다.");
		
		Container.articleDao.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(),1, "제목1", "내용1", 11));
		Container.articleDao.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(),2, "제목2", "내용2", 22));
		Container.articleDao.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(),3, "제목3", "내용3", 33));
		
	}
}
