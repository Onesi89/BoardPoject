<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="kr">
<head>
</head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<body>
	<div class="container">
		<div class="py-5 text-center">
			<h2>회원정보</h2>
		</div>
		<div class="row align-items-center" style="width:50%;margin:0px auto">
			<table class="table table-striped">
				<tr>
					<td>
					<div class="m-2">ID</div>
					</td>
					<td>
						<div class="m-2">${member2.getUserId()}</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="m-2">이름</div>
					</td>
					<td>
						<div class="m-2">${member2.getName()}</div>
					</td>
				</tr>
				<tr>
					<td><div class="m-2">이메일주소</div></td>
					<td>
						<div class="m-2">${member2.getEmail()}</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="m-2">가입일자</div>
					</td>
					<td>
						<div class="m-2">${member2.getDate()}</div>
					</td>
				</tr>
				<tr>
					<td><div class="m-2">작성한 글 수</div></td>
					<td>
						<div class="m-2">${member2.getTotalWriteNumber()}</div>
					</td>
				</tr>
				<tr>
					<td><div class="m-2">작성한 댓글 수</div></td>
					<td>
					<div class="m-2">${member2.getTotalCommentNumber()}</div>
						
					</td>
				</tr>
			</table>
			
			<div id="div1" class="text-center mt-3">
				<a href="/board/member/editMemberPage/${member2.getUserId()}">
				<button id=signUpbtn class="btn btn-primary btn-lg  bg-dark"
					type="button">정보 수정</button></a>
					
				<a href="/board/member/removal/${member2.getUserId()}" onclick="if(confirm('정말 삭제 하시곘습니까?')==false)return false;">
				<button id=backBtn class="btn btn-primary btn-lg bg-dark"
					type="button" >회원탈퇴</button></a>
			</div>
		</div>
	</div>
</body>

</html>