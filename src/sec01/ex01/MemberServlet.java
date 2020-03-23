package sec01.ex01;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//서블릿 
//웹브라우저 주소창에......
//http://localhost:8080/pro08/member로 모든 회원정보를 조회해줘 ~라고 요청이 들어오면...
@WebServlet("/member")
public class MemberServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		 //서블릿은 회원정보 조회 요청을 받아 조회한 회원정보를 클라이언트의 웹브라우저로 응답해야한다.
		//1.응답할 데이터의 MIME-TYPE설정 
		response.setContentType("text/html;charset=utf-8");
		//2.응답데이터를 웹브라우저로 출력할 PrintWriter 객체 얻기 
		PrintWriter out =  response.getWriter();
		
		//3.DB작업명령 (t_member테이블에 접근하여 모든 회원정보 조회 명령) 
		MemberDAO dao = new MemberDAO();//DB작업할 객체 생성
		
		//4.MemberDAO의 listMembers()메소드를 호출하여 검색한 모든 회원정보를 각각 MemberVO객체에 저장하여
		//각각의 MemberVO객체들을 최종적으로 ArrayList라는 가변길이 배열에 추가후 ....
		// DB에서 검색한 모든 회원정보인? ArrayList를 리턴(반환)받는다.
		ArrayList list = dao.listMembers();
		
		//5.클라이언트의 웹브라우저로 DB로부터 조회한 회원정보를 출력 
		out.print("<html>");
		out.print("<body>");
		out.print("<table border=1>");
		out.print("<tr align='center' bgcolor ='lightgreen'>");
		out.print("<td>아이디</td><td>비밀번호</td><td>이름</td><td>이메일</td><td>가입일</td>");
		out.print("</tr>");
		
		for(int i=0; i <list.size(); i++) {
			//조회한 회원정보는 ArrayList라는 가변길이 배열공간에 저장되어 있으므로
			//ArrayLsit가변길이 배열에 저장된 검색한 회원정보(MemberVO객체)를 하나씩 얻는다.
			MemberVO memberVO = (MemberVO)list.get(i);
			//getter메소드를 이용하여 MemberVO의 각 변수에 저장된 회원 정보를 얻는다.
			String id = memberVO.getId();
			String pwd = memberVO.getPwd();
			String name = memberVO.getName();
			String email = memberVO.getEmail();
			Date joinDate = memberVO.getJoindate();
			
			out.print("<tr>");
			out.print("<td>"+ id+ "</td><td>" +pwd +"</td><td>"+name+"</td>" + 
			"<td>" + email + "</td><td>" + joinDate + "</td>");
			out.print("</tr>");
		}
		
		out.print("</table>");
		out.print("</body>");
		out.print("</html>");


	}
	
}
