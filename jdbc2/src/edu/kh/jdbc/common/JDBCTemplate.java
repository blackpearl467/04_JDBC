package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/* Template : 양식, 틀, 주형
 * -> "미리 만들어뒀다" 의미
 * 
 * JDBCTemplete :
 * JDBC 관련 작업을 위한 코드를
 * 미리 작성해서 제공하는 클래스
 * 
 * 
 * -Connection 생성
 * -AutoCommit false
 * -commit / rollback
 * -각종 close()
 * 
 * 
 * ****** 중요 ******
 * 어디서든 JDBCTemplete클래스를
 * 객체로 만들지 않고도 메서드를 사용할 수 있도록 하기위해
 * 모든 메서드를 public static으로 선언
 * 
 * 
 * 
 * */

public class JDBCTemplate {
	
	//필드
	private static Connection conn = null;
	//->static 메서드에서 사용가능한 필드로 static 필드 선언
	
	//메서드
	/** 호출 시 Connection 객체를 생성해서 반환하는 메서드 + AutoCommit 끄기
	 *  @return conn
	 */
	public static Connection getConnection() {

		try {
			// 이전에 참조하던 Connection 객체가 존재하고
			// 아직 close 된 상태가 아니라면
			// 새로 만들지 않고 기존 Connection 반환
			if(conn != null && !conn.isClosed()) {//앞에 ! not연산자
				return conn;
			}
			
			/* driver.xml 파일 내용 읽어오기 
			 * 
			 * 이유 1: 보안상의 이유 (Github에 DB연결 정보 등 올리면 해킹하라는 뜻..)
			 * 		--> .gitignore 파일에 driver.xml 작성하여 git이 관리 x
			 * 
			 * 이유 2: 혹시라도 DB연결정보가 변경될 경우
			 * 			Java코드가 아닌
			 * 			읽어오는 파일의 내용을 수정하면 되기 때문에
			 * 			Java 코드 수정 X -> 추가 재컴파일 필요 X
			 * 
			 * */
			
			// 1. Properties 객체 생성
			//-Map 의 자식 클래스
			//-K, V 가 모두 String 타입
			//-xml 파일 입출력을 쉽게 할 수 있는 메서드 제공
			//Properties.storeToXml()-> xml 파일 만들기
			//Properties.loadFromXML()-> xml 파일 읽어오기
			
			Properties prop = new Properties();
			
			//2. Properties 메서드를 이용해서
			//	 driver.xml 파일 내용을 읽어오기
			
			String filePath = "driver.xml";
			//프로젝트 폴더 바로 아래 driver.xml 파일 경로
			
			prop.loadFromXML(new FileInputStream(filePath));
			
			//prop에 저장된 값 (driver.xml 에서 읽어온 값)을 이용해서
			//Connection 객체 생성
			
			//prop.getProperty("key") :key가 일치하는 value를 반환
			Class.forName(prop.getProperty("driver"));
			//Class.forName("oracle.jdbc.diriver.OracleDriver")
			
			String url = prop.getProperty("url");
			//jdbc:oracle:thin:@localhost:1521:XE
			
			String userName = prop.getProperty("userName");
			//kh_kbg
			
			String password = prop.getProperty("password");
			//kh1234
		
			conn = DriverManager.getConnection(url, userName, password);
			
			//만들어진 Connection에서 AutoCommit 끄기
			conn.setAutoCommit(false);

			
		} catch (Exception e) {
			System.out.println("커넥션 생성 중 예외 발생..");
			e.printStackTrace();
		}
		return conn;
	}
	
	
	//-----------------------------
	
	/** 전달받은 커넥션에서 수행한 SQL을 COMMIT 하는 메서드
	 * @param conn
	 */
	public static void commit(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/** 전달받은 커넥션에서 수행한 SQL을 ROLLBACK 하는 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	//-----------------------------
	
	/** 전달받은 커넥션을 close(자원반환)하는 메서드
	 * @param conn
	 */
	public static void close(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/** 전달받은 Statement + PreparedStatement 둘 다 close()
	 * + 다형성 업캐스팅 적용
	 * -> PreparedStatement는 Statement 자식
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/** 전달받은 ResultSet을 close()하는 메서드
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		
		try {
			if(rs != null && !rs.isClosed()) rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
