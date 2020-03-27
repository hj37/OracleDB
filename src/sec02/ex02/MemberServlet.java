package sec02.ex02;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sec02.ex02.MemberVO;

//memberForm.html파일에서 입력한 회원정보는? 톰켓이 새로 생성한 HttpServletRequest객체 메모리에 저장후 
//doPost()메소드의 매개변수로 전달 받는다.
//먼저 HttpServletRequest객체 내부에 저장되어 있는 command값을 먼저 받아와 값이 addMember이면 
//회원가입 요청임을 알 수 있다.
@WebServlet("/member3")
public class MemberServlet extends HttpServlet {

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* 1. 요청한 값(DB에 insert할? 입력한 새로운 회원정보)얻기 */

		// 요청값 한글처리
		request.setCharacterEncoding("UTF-8");

		// DB에 입력한 회원정보를 INSERT하기 위해 DAO객체 생성
		MemberDAO dao = new MemberDAO();

		// 1.클라이언트가 서블릿으로 어떤 요청을 했는지 판단 값을 얻기 (삭제 요청일시 delMember)
		String command = request.getParameter("command");

		// 회원가입창에서 전송된 command값이 addMember이면 (회원등록 요청이면)
		if (command != null && command.equals("addMember")) {
			// 회원가입창에서 입력한 회원정보들을 request객체에서 꺼내오기
			String _id = request.getParameter("id"); // 입력한 아이디 얻기
			String _pwd = request.getParameter("pwd"); // 입력한 비밀번호 얻기
			String _name = request.getParameter("name"); // 입력한 이름 얻기
			String _email = request.getParameter("email"); // 입력한 이메일 얻기

			// 입력한 회원정보를 DB에 insert하기 전.. MemberVO객체의 각 변수에 저장
			MemberVO vo = new MemberVO();
			// setter메소드들을 호출해서 MemberVO객체의 각 변수에 입력한 값들을 저장
			vo.setId(_id);
			vo.setPwd(_pwd);
			vo.setName(_name);
			vo.setEmail(_email);

			// 회원가입 양식에서 입력한 회원정보를 MemberVO객체에 저장한 후 ~
			// MemberVO객체 자체를? MemberDAO의 메소드호출시 매개변수로 전달하여 DB작업함(INSERT작업함)
			dao.addMember(vo);
		} // if문
		
		//command값이 delMember인경우(삭제 요청일 경우) 삭제한 회원 ID를 request객체서 꺼내와서 
		//DAO객체의 delMember() 메소드 호출시 매개변수로 전달하여 DB삭제 작업함.
		
		else if(command != null && command.equals("delMember"))
		{
			//삭제할 회원 ID를 request영역에서 꺼내오기 
			String id = request.getParameter("id");
			
			dao.delMember(id);//DAO의 delMember()메소드 호출시 삭제할 회원 ID를 전달하여 DB작업함 
		}
		// 위의 회원 가입 후 가입한 회원정보들을 모두 조회하여 클라이언트의 웹브라우저로 출력하자.
		// 서블릿은 회원정보 조회 요청을 받아 조회한 회원정보를 클라이언트의 웹브라우저로 응답해야한다.
		// 1.응답할 데이터의 MIME-TYPE설정
		response.setContentType("text/html;charset=utf-8");
		// 2.응답데이터를 웹브라우저로 출력할 PrintWriter 객체 얻기
		PrintWriter out = response.getWriter();

		dao.listMembers();

		// 4.MemberDAO의 listMembers()메소드를 호출하여 검색한 모든 회원정보를 각각 MemberVO객체에 저장하여
		// 각각의 MemberVO객체들을 최종적으로 ArrayList라는 가변길이 배열에 추가후 ....
		// DB에서 검색한 모든 회원정보인? ArrayList를 리턴(반환)받는다.
		ArrayList list = dao.listMembers();

		// 5.클라이언트의 웹브라우저로 DB로부터 조회한 회원정보를 출력
		out.print("<html>");
		out.print("<body>");
		out.print("<table border=1>");
		out.print("<tr align='center' bgcolor ='lightgreen'>");
		out.print("<td>아이디</td><td>비밀번호</td><td>이름</td><td>이메일</td><td>가입일</td><td>삭제</td>");
		out.print("</tr>");

		for (int i = 0; i < list.size(); i++) {
			// 조회한 회원정보는 ArrayList라는 가변길이 배열공간에 저장되어 있으므로
			// ArrayLsit가변길이 배열에 저장된 검색한 회원정보(MemberVO객체)를 하나씩 얻는다.

			/*
			 * //업캐스팅 : 부모클래스 타입의 참조변수에 자식객체를 저장하는 것! //업캐스팅 Object obj = list.get(i);
			 * //다운캐스팅 자식클래스타입의 참조변수로 형변환(캐스팅) 해주어야함. MemberVO memberVO = (MemberVO)obj;
			 * MemberVO memberVO = (MemberVO)list.get(i); //get(i) Object타입이 무조건 반환이 됨
			 * 다형성(다양한 형태의 성질을 지닐 수 있는 것) 메소드 오버로딩 : 하나의 같은 이름으로 매개변수 개수 또는 타입을 달리해서 메소드를
			 * 여러개 만들어 놓는것 메소드 오버라이딩 : 메소드 선언부는 같게 하되 구현부만 재구현해서 여러개의 메소드를 만들 수 있다. 업캐스팅 :
			 * 하나의 부모클래스타입의 참조변수에 여러 가지 객체를 저장할 수 있다.
			 * 
			 */

			MemberVO memberVO = (MemberVO) list.get(i);
			// getter메소드를 이용하여 MemberVO의 각 변수에 저장된 회원 정보를 얻는다.
			String id = memberVO.getId();
			String pwd = memberVO.getPwd();
			String name = memberVO.getName();
			String email = memberVO.getEmail();
			Date joinDate = memberVO.getJoindate();

			out.print("<tr>");
			out.print("<td>" + id + "</td><td>" + pwd + "</td><td>" + name + "</td>" + "<td>" + email + "</td><td>"
					+ joinDate + "</td>" + "<td><a href='/pro08/member3?command=delMember&id=" + id + "'>삭제</a></td>");
			out.print("</tr>");
		}

		out.print("</table>");
		out.print("</body>");
		out.print("</html>");

		out.print("<a href='/pro08/memberForm.html'>새회원등록하기</a>");

	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request,response);
	}
}
