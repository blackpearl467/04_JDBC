package edu.kh.todoList.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static edu.kh.todoList.common.JDBCTemplate.*;
import edu.kh.todoList.dto.Todo;

public class TodoListDAOImpl implements TodoListDAO{

	//JDBC 객체 참조변수 + Properties 참조 변수 선언
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Properties prop;
	
	public TodoListDAOImpl() {
		//TodoListDAOImpl 객체가 생성될때
		//sql.xml 파일의 내용을 읽어와
		//Properties prop 객체에 K:V 세팅
		try {
			String filePath = TodoListDAOImpl.class.
								getResource("/xml/sql.xml").getPath();
			
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(filePath)); //읽어들이겠다
			
			
			
			
			
		} catch (Exception e) {
			System.out.println("sql.xml 로드 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<Todo> todoListFullView(Connection conn) throws Exception{
		
		//결과 저장용 변수 선언
		List<Todo> todoList = new ArrayList<Todo>();
		
		try {
			String sql = prop.getProperty("todoListFullView");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				//Builder 패턴: 특정값으로 초기화된 객체를 쉽게 만드는 방법
				//->Lombok에서 제공하는 @Builder 어노테이션을 DTO에 작성해두면
				//	사용가능한 패턴
				
				boolean complete = rs.getInt("TODO_COMPLETE") == 1;
				
				Todo todo = Todo.builder()
							.todoNo(rs.getInt("TODO_NO"))
							.title(rs.getString("TODO_TITLE"))
							.complete(complete)
							.regDate(rs.getString("REG_DATE"))
							.build();
				todoList.add(todo);
				
			}
			
		} finally {
			close(rs);
			close(stmt);
			
		}
		
		return todoList;
		
	}
	
	
	
	@Override
	public int getCompleteCount(Connection conn) throws Exception{
		
		int completeCount = 0;
		
		try {
			String sql = prop.getProperty("getCompleteCount");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				completeCount = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(stmt);
			
		}
		
		return completeCount;
		
	}

	@Override
	public int todoAdd(Connection conn, String title, String detail) throws Exception{

		int result = 0;
		
		try {
			String sql=prop.getProperty("todoAdd");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setString(2, detail);
			
			result = pstmt.executeUpdate();
					
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	
	
	
	
	
}
