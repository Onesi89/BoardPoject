<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>게시글 뷰</title>
</head>
<body>
	<article>
		<div class="container w-75 mt-5 mx-auto">
			<h2 style="display: inline-block;">${board1.getTitle()}</h2>
			<div class="mt-4" style="display: inline-block;float:right;">
				<span> 조회수 : ${board1.getViews()}</span> 
				<span>/ Date : ${board1.getDate()}</span>
			</div>
			<hr />
			<div class="card w-75 mx-auto">
				<div>
				<c:if test="${board1.getImg() != '/img/'}"> 
				<img src="${board1.getImg()}" style="width:40%" class="card-img-top" alt="이미지" />
				</c:if>
				</div>
				<div class="card-body">
					<p class="card-text">${board1.getContent()}</p>
				</div>
			</div>
			<hr />
			<div style="display: inline-block; float:left; margin: 0 auto;">
				<a href="javascript:history.back()" class="btn btn-dark">Back</a>
			</div>

			<c:if test="${member1 != 'nonmember' && member1.getUserId() eq board1.getUserId()}">
				<div style="display: inline-block; float:right;">
					<a class="btn btn-dark" href="/board/editFormPage/${board1.getAid()}">수정</a>
				</div>
				<div style="display: inline-block; float:right;margin-right: 20px;">
					<a class="btn btn-dark" href="/board/delete/${board1.getAid()}" onclick="if(confirm('정말 삭제 하시곘습니까?')==false)return false;">삭제</a>
				</div>
			</c:if>
		</div>

		<%-- 댓글 기능 추후 구현 --%>

	</article>
</body>
</html>