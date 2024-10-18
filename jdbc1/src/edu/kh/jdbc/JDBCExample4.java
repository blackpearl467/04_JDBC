package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {
	
	public static void main(String[] args) {
		
		//부서명을 입력받아
		//해당 부서에 근무하는 사원의
		//사번, 이름, 부서명, 직급명을
		//직급코드 오름차순으로 조회
		
		//부서명 입력 : 총무부
		//200 / 선동일 / 총무부 / 대표
		//202 / 노옹철 / 총무부 / 부사장
		//201 / 송종기 / 총무부 / 부사장
		
		//hint : sql 에서 문자열은 양쪽에 ''(홑따옴표) 필요
		// ex) 총무부 입력 => '총무부'
		
		// 1. JDBC 객체 참조 변수 선언
				Connection conn = null;
				Statement stmt = null; 
				ResultSet rs = null;	// -> sql(select문으로 수행)
				
				try {
					//2.DriverManger 객체를 이용해서 Connection 객체 생성하기
					Class.forName("oracle.jdbc.driver.OracleDriver");
					
					String url = "jdbc:oracle:thin:@localhost:1521:XE";
					
					String userName = "kh_kbg"; // 사용자 계정명
					String password = "kh1234"; // 계정 비밀번호
					
					conn = DriverManager.getConnection(url, //conn이라는곳에 참조변수에 담아주기(연결)
							userName,
							password); //커넥션을 만들기 위해서는 db정보가 필요			
					
		//3.SQL 작성
		Scanner sc = new Scanner(System.in);
		System.out.print("부서명 입력 : ");
		String input = sc.nextLine();
		
		String sql = """ 
				SELECT
					EMP_ID, 
					EMP_NAME, 
					NVL(DEPT_TITLE, '없음') DEPT_TITLE, 
					JOB_NAME
				FROM EMPLOYEE
				JOIN JOB USING (JOB_CODE)
				LEFT JOIN DEPARTMENT ON (DEPT_CODE=DEPT_ID)
				WHERE DEPT_TITLE = '""" + input + "' ORDER BY JOB_CODE";
		
		//4.Statement 객체 생성
		stmt = conn.createStatement(); //stmt가 버스가 됨 sql이 승객
				
		//5. SQL 수행 후 결과 반환 받기
		rs = stmt.executeQuery(sql); //버스종류가 바뀜 sql(승객) 실어보내기 //반환값 rs에 대입
		
		boolean flag = true;
		//조회 결과 있다면 false, 없으면 true
		
		//6.1행씩 접근해서 컬럼 값 얻어오기
		
			while(rs.next()) {
				flag = false;
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n",
									empId, empName, deptTitle, jobName );
			}
		
			if(flag) { //flag == true == while문이 수행된 적이 없음
				System.out.println("일치하는 부서가 없습니다!");
			}
		
		} catch (Exception e) {
					e.printStackTrace();
					
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close(); //역순으로 닫아주기
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	
}