<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">


<!-- 부트스트랩 css -->
<link rel="canonical"
	href="https://getbootstrap.com/docs/5.1/examples/sign-in/">
	
<!-- 부트스트랩 cdn -->	
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">
	
<!--  폰트 어썸 v4.7 -->
<link rel="stylesheet"  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" />

<!-- 프로젝트 css  -->
<link rel="stylesheet" href="/css/infocss.css">
<link rel="stylesheet" href="/css/home.css">


<!-- js 시작 -->
<!-- 프로젝트 js -->
<script src="/js/jssCollect.js" type="text/javascript"></script>

<!-- 부트스트랩 js  -->	

<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
	crossorigin="anonymous"></script>
	



<!--  crpyo cdn -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>


<title>게시판</title>
</head>

<!-- body 시작 로그인 실패 정보가 있으면 body onload 이벤트 등록 -->
<c:choose>
	<c:when test="${loginInfo eq 'fail'}">
		<body onload="loginfail()">
	</c:when>
	<c:when test="${loginInfo != 'fail'}">
		<body>
	</c:when>
</c:choose>

<!-- header 시작  -->
<header>

<!-- 네비바 모달 트리거 Button trigger modal -->
	<nav class="navbar navbar-expand-md navbar-dark bg-dark">
		<div class="container-fluid">
		<a class="navbar-brand mb-2 mb-lg-0" href="/board/main/1" id="titleName">
		<i class="fa fa-github-alt"></i> 게시판 </a>

		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarTogglerDemo02"
			aria-controls="navbarTogglerDemo02" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse justify-content-end"
			id="navbarTogglerDemo02">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item"><a class="nav-link" href="#">A 게시판</a></li>
				<li class="nav-item"><a class="nav-link" href="#">B 게시판</a></li>
				<li class="nav-item"><a class="nav-link" href="#">C 게시판</a></li>
				<li class="nav-item"><a class="nav-link" href="/board/list/40/1">드라마</a></li>
			</ul>
			<div class="d-flex flex-row-reverse bd-highlight">
				
				<%--리스트 생성 로그인 / 로그아웃 구별 --%>
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<c:choose>
						<c:when test= "${member1 == null ||  member1 eq 'nonmember'}">
							<li class="nav-item"><a id="loginBtn1" class="nav-link" data-bs-toggle="modal" data-bs-target="#loginModal" href="#">로그인</a></li>
							
							<li class="nav-item"><a class="nav-link" href="/board/member/signUp">회원가입</a></li>
						</c:when>
						
						<c:when test = "${member1 != null}">
						       <li class="nav-item dropdown">
						          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
						           ${member1.getUserId()}
						          </a>
						          <ul class="dropdown-menu mb-0 mt-0" aria-labelledby="navbarDropdown">			      
						            <li class="mb-0 mt-0"><a class="dropdown-item" href="/board/member/info/${member1.getUserId()}">마이페이지</a></li>		
						          </ul>
						        </li>
							<li class="nav-item"><a class="nav-link" onclick="deleteCookie('mySessionId')" href="/board/member/logout/${member1.getUserId()}" >로그아웃</a></li>
						</c:when>
					</c:choose>
				</ul>
			</div>
		</div>
		
	<!-- Modal 시작-->
	<div class="modal fade" id="loginModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<div class="form-title text-center mt-3">
						<h4>Login</h4>
					</div>
					<div class="d-flex flex-column text-center" id="login">
						<form class="needs-validation" novalidate
							action="/board/member/login" method="POST" id="form" autocomplete="off">
							<div class="col-md-12">
								<input name="userId" type="text" class="form-control mb-2"
									id="userId2" placeholder="ID" pattern="[a-zA-Z0-9]{6,20}">
							</div>
							<div class="col-md-12 mb-0 mt-0">
								<input type="hidden" class="form-control" id="password3"
									 name="password" required>
								<div class="invalid-feedback"></div>
							</div>
							<div class="col-md-12">
								<input type="password" class="form-control mb-2" id="password2"
								 placeholder="password" autocomplete="off">
							</div>
							<button id="btn" type="submit"
								class="btn btn-block btn-round bg-dark text-white"
								onclick="signUp('aa')">Login</button>

							<div class="alert alert-danger col-md-12 mb-0 mt-2"
								style="display: none;" id="errorText">아이디와 비밀번호를 확인해주세요.</div>
						</form>
					</div>
				</div>
				<div class="modal-footer d-flex justify-content-center">
					<div class="signup-section">
						아직 회원이 아니세요?&nbsp; <a class="text-dark"
							href="/board/member/signUp"> 회원가입</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</nav>


<!-- header 끝 -->
</header>

<%-- 세션 시작 include 활용하여 다른 페이지를 뿌려지게 할 예정--%>
<section>
	<%-- 회원 가입일 때 --%>
	<c:if test="${newMember == 'newmember'}">
		<script>
		alert("회원 가입을 축하합니다.");
		</script>
	</c:if>
	
	<%-- ID 중복 일 때 --%>
	<c:if test="${IDerror1 == 'IDerror2'}">
		<div class="container">
			<div class="alert alert-danger mt-3" role="alert">ID가 중복되었습니다.
				잠시 후에 이전 페이저로 돌아갑니다.</div>
		</div>
		<script type="text/javascript">
			setTimeout('history.back(-1)', 2000);
		</script>
	</c:if>

	<%-- 회원가입 페이지 --%>
	<c:if test="${selectVeiwJsp eq 'signUp'}">
		<jsp:include page="signUp.jsp" />
		
	</c:if>
	
	<%-- 마이페이지 회원 페이지 --%>
	<c:if test="${selectVeiwJsp eq 'memberPage'}">
		<jsp:include page="memberPage.jsp" />
	</c:if>
	
	<%-- 마이페이지 회원 수정수정  --%>
	<c:if test="${selectVeiwJsp eq 'editMember'}">
		<jsp:include page="editMemberInfo.jsp" />
	</c:if>
	
	<%--메인 리스트페이지 --%>
	<c:if test="${board eq 'main'}">
		<jsp:include page="board.jsp"/>
	</c:if>
	
	<%--메인 리스트페이지 --%>
	<c:if test="${board eq 'active'}">
		<jsp:include page="boardVIew.jsp"/>
	</c:if>
	
	<%--메인 리스트페이지 --%>
	<c:if test="${board eq '40'}">
		<jsp:include page="drama.jsp"/>
	</c:if>
	
	<%-- 글쓰기 및 수정페이지 --%>
	<c:if test="${write eq 'write' || write eq 'edit'}">
		<jsp:include page="writeForm.jsp"/>
	</c:if>

</section>



<footer style="position : absolute bottom : 0;">

</footer>
</body>


</html>
