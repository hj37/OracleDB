package sec02.ex01;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//데이터베이스 연결 및 데이터베이스 작업(insert, select,delete,update 등등) 
public class MemberDAO {

	
	private Connection con;

	
	//Statement인터페이스를 구현한 자식객체를 저장하는 대신에 
	//PreparedStatement인터페이스를 구현한 자식객체를 저장할 용도의 변수 선언 
	private PreparedStatement pstmt;
	
	//커넥션풀 역할을 하는 DataSource객체를 저장할 변수 선언 
	private DataSource dataFactory;
	
	public MemberDAO() {//생성자~~~~
		// TODO Auto-generated constructor stub
		try {
			//톰캣이 실행되면 context.xml파일의 <Resource/>태그의 설정을 읽어와서 
			//톰캣 내부 메모리에 프로젝트별 정보를? Context객체를 생성하여 저장해둔다.
			//이때  InitialContext객체가 하는 역할을 톰캣 실행시 context.xml에 의해서 생성된 
			//context객체들이 접근을 하는 역할을 함.
			Context ctx= new InitialContext();
			//JNDI방법으로 접근하기 위해 기본경로(java:/comp/env)를 지정합니다.
			//lookup("java:/comp/env")는  그 중 환경설정에 관련된 Context객체에 접근하기 위한 기본 주소입니다.
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			
			//그런 후 다시 톰캣 context.xml에 설정한 <Resource name="jdbc/oracle"..../>태그의
			//name속성값 "jdbc/oracle"이라는 JNDI이름을 이용해 톰캣이 미리 연결한 DataSource객체(커넥션을 역할을 하는 객체) 
			//를 받아옵니다.
			 dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
			
		}catch(Exception exception) {
			System.out.println("커넥션풀(DataSource)은 얻기 실패함!" + exception);
		}
	}//DAO생성자 끝 

	//listMembers메소드는 어떤 클래스 내부에서 호출하는 메소드인가? MemberServlet 
	
	// DB의 모든 회원정보 검색(조회) 담담하는 메소드 만들기 list.Member
	public ArrayList listMembers() {
		
		ArrayList list = new ArrayList();
	try {
		
		//커넥션풀(DataSource)공간에서 DB와 미리 연결은 맺은 Connection객체를 빌려옴 
		//DB연결작업 
		con = dataFactory.getConnection();
		
		//5.Query작성하기 
		String query ="select * from t_member";
		
		//4. Connection객체의 PreparedStatement()메소드 호출시 ...SQL문을 전달해 
		//미리 로딩한 OraclePreparedStatementWrapper실행객체 얻기 
		pstmt = con.prepareStatement(query);
		
		//6.Query를 DB의 테이블에 전송하여 실행하기
		//->Statement객체의 executeQuery메소드 호출시...select구문을 전달하면...
		//검색한 회원정보를 테이블형식으로
		//ResultSet인터페이스의 자식객체인? OracleResultSetImpl 임시 메모리 공간에 저장하여 반환받는다.
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {	//검색할 줄이 존재할때까지 반복 
			//7.select문일 경우 검색할 결과값 데이터들이 ResultSet일시 메모리 저장소에 저장되어 있기때문에 
			//rs.next()메소드를 호출하여 그 다음 줄의 검색한 한 줄의 레코드를 가리키게 해야함.
			//현재 가리키고 있는 한 줄의 정보를 얻어올때...ResultSet객체의 get으로 시작하는 메소드 활용함.
			String id = rs.getString("id");
			String pwd = rs.getString("pwd");
			String name = rs.getString("name");
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joindate");
			
			//위 변수에 저장된 각 컬럼값을 다시 MemberVo객체를 생성하여
			//각 변수에 저장 
			MemberVO vo = new MemberVO();
			//setter메소드 호출~! 
			vo.setId(id);
			vo.setPwd(pwd);
			vo.setName(name);
			vo.setEmail(email);
			vo.setJoindate(joinDate);
			
			//MemberVO객체 자체를? ArrayList가변길이 배열에 추가해서 저장
			list.add(vo);
		}//while반복문 끝
		
		//자원해제~ 
		con.close(); //Connection객체를 DataSource커넥션풀로 반납! 
		pstmt.close();
		rs.close();
		
		}catch(Exception e) {
									// 오류 메세지 출력 
			System.out.println("listMembers메소드 내부에서 오류: " + e);
		}
		return list; //DB로부터 검색한 레코드의 갯수만큼 MemberVO객체들이 저장되어 있는 ArrayList배열공간을 반환함.
	}//listMembers()메소드 닫는 기호 

}//MemberDAO 클래스 닫는 기호 
