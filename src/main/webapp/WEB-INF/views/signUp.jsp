<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
<!DOCTYPE html>
<html lang="kr">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<div class="container">
		<div class="py-5 text-center">
			<svg xmlns="http://www.w3.org/2000/svg" width="72" height="72"
				fill="currentColor" class="bi bi-gift mx-auto mb-4"
				 viewBox="0 0 16 16">
                <path
					d="M3 2.5a2.5 2.5 0 0 1 5 0 2.5 2.5 0 0 1 5 0v.006c0 .07 0 .27-.038.494H15a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1v7.5a1.5 1.5 0 0 1-1.5 1.5h-11A1.5 1.5 0 0 1 1 14.5V7a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h2.038A2.968 2.968 0 0 1 3 2.506V2.5zm1.068.5H7v-.5a1.5 1.5 0 1 0-3 0c0 .085.002.274.045.43a.522.522 0 0 0 .023.07zM9 3h2.932a.56.56 0 0 0 .023-.07c.043-.156.045-.345.045-.43a1.5 1.5 0 0 0-3 0V3zM1 4v2h6V4H1zm8 0v2h6V4H9zm5 3H9v8h4.5a.5.5 0 0 0 .5-.5V7zm-7 8V7H2v7.5a.5.5 0 0 0 .5.5H7z" />
              </svg>
			<h2>회원가입</h2>
			<p class="lead">회원가입하세요. 가입하면 게시판을 열람할 수 있습니다.</p>
		</div>

		<div class="col-md-12 order-md-1" style="width:50%;margin:0px auto" id="signUp">
			<h4 class="mb-3">회원정보</h4>
			<form class="row g-3 needs-validation" novalidate method="POST"  action="/board/member/addMember" id=form-signin autocomplete="off">
				<div class="mb-3">
					<label for="username" class="form-label"></label> 
					<input autocomplete="false"
						type="text" class="form-control" id="username" name="userId"
						placeholder="ID" required pattern="[a-z][a-z0-9]{5,12}">
					<div class="invalid-feedback">ID가 잘못되었습니다.</div>
				</div>
				
				<div class="mb-0 mt-0">
					<input type="hidden" class="form-control" id="password1" name="password" required autocomplete="off">
					<div class="invalid-feedback"></div>
				</div>
				
				<div class="mb-3">
					<label for="password" class="form-label"></label> 
					<input
						autocomplete="new-password" required
						type="password" class="form-control" id="password"
						placeholder="PW"
						pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$">
					<div class="invalid-feedback">비밀번호가 잘못되었습니다.</div>
				</div>

				<div class="mb-3">
					<label for="name" class="form-label"></label> <input type="text" name="name"
						placeholder="이름" class="form-control" id="name" required>
					<div class="invalid-feedback">이름이 잘못되었습니다.</div>
				</div>
				<div class="mb-3">
					<label for="email" class="form-label"></label> <input type="email" name="email"
						class="form-control" id="email" required placeholder="email"
						pattern="^[a-zA-Z0-9\+\-\.]+@[a-zA-Z0-9]+\.[a-zA-Z0-9\-\.]+$">
					<div class="invalid-feedback">이메일이 잘못되었습니다.</div>
				</div>
			
				<div id="div1" class="text-center mt-0">
					<hr class="mb-4">
					<button id=signUpbtn class="btn btn-primary btn-lg  bg-dark mb-2"
						type="submit"  onclick="signUp('signUp')">가입하기</button>
					<button id=backBtn class="btn btn-primary btn-lg bg-dark mb-2"
						type="button" onclick="historyBack()">돌아가기</button>
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
    }
    else self.name = 'notReload'; 
	</script>
</body>
</html>