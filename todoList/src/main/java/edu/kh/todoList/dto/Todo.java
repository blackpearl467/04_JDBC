package edu.kh.todoList.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

	private int todoNo; //todo 번호
	private String title; //todo 제목
	private String detail; //todo 상세내용
	private boolean complete; //todo 완료여부
	private String regDate; //todo 등록일
}
