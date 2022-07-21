<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="kr">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="/js/jssCollect.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">
		<div class="py-5 text-center">
			<c:if test="${write eq 'write'}">
			<h2>게시글 작성</h2>
			</c:if>
			
			<c:if test="${write eq 'edit'}">
			<h2>게시글 수정</h2>
			</c:if>
		</div>

		<div class="row align-items-center"
			style="width: 50%; margin: 0px auto" id="signUp">	
			<c:if test="${write eq 'write'}">
				<form class="row g-3" method="POST" action="/board/write" id=form-signin enctype="multipart/form-data">
			</c:if>
			
			<c:if test="${write eq 'edit'}">
				<form class="row g-3" method="POST" action="/board/edit" id=form-signin enctype="multipart/form-data">
			</c:if>
			
				<div>
					<label for="username" class="form-label">제목</label> 
					<input type="text" class="form-control" name="title" />
				</div>	
				
				<c:if test="${write eq 'edit'}">
					<div>
					<label for="username" class="form-label">글번호</label> 
					<input type="text" class="form-control" name="aid" value="${aid}" readonly/>
					</div>
				</c:if>
				
				
				<div>
					<span>카테고리</span> <select class="form-select" name="boardNumber">
						<option value="10">A게시판</option>
						<option value="20">B게시판</option>
						<option value="30">C게시판</option>
						<option value="40">드라마</option>
					</select>
				</div>
				
				<script type="text/javascript">
			</script>

				<div>
					<label for="username" class="form-label" >이미지파일</label>
					<input type="file" class="form-control" name="file" onchange="checkSize(this)"/>
				</div>

				<div>
					<label for="name" class="form-label">본문</label>
					<textarea style="overflow-y: auto" class="form-control" cols="40"
						rows="10" placeholder="글을 작성해주세요" name="content"></textarea>
				</div>


				<div id="div1" class="text-center mt-0">
					<hr class="mb-4">

					<button id=signUpbtn class="btn btn-primary btn-lg  bg-dark"
						type="submit">글쓰기</button>


					<button id=backBtn class="btn btn-primary btn-lg bg-dark"
						type="button" onclick="historyBack()">Back</button>
					<hr class="mb-4">

				</div>
				<footer th:replace="/fragments/semantic :: footer"></footer>
			</form>
		</div>
	</div>

	<script>
		if (self.name != 'reload') {
			self.name = 'reload';
			self.location.reload(true);
		} else
			self.name = 'notReload';
	</script>
</body>
</html>