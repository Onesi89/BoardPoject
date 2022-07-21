function loginfail() {
	document.getElementById("loginBtn1").click();
	let errorText = document.getElementById("errorText");
	errorText.style = "display:blcok";

	var myModalEl = document.getElementById('loginModal');

	myModalEl.addEventListener("hidden.bs.modal", function() {
		errorText.style = "display: none";
	}, false)
}

function signUp(str) {
	let str1 = str;
	let forms = document.querySelectorAll('#login>.needs-validation')
	let pw = document.getElementById("password2");
	let pw1 = document.getElementById("password3");


	
	if (str1 == "signUp") {
		forms = document.querySelectorAll('#signUp .needs-validation')
		pw = document.getElementById("password");
		pw1 = document.getElementById("password1");
	}

	// Loop over them and prevent submission
	Array.prototype.slice.call(forms)
		.forEach(function(form) {
			form.addEventListener('submit', function(event) {

				if (!form.checkValidity()) {
					event.preventDefault()
					event.stopPropagation()
				}

				/*console.log(pw1.value);*/

				/*pw1.value= pw.value;*/
				if (pw.value != null) {
					pw1.value = CryptoJS.SHA256(pw.value).toString(CryptoJS.enc.Base64);

					//회원 가입 후 뒤로가기 버튼 눌렀을 때 비밀번호 초기화
				}

				//document.getElementById("signForm").reset()
				/*console.log(pw1.value);*/

				form.classList.add('was-validated')

			}, false)
		})
}


//!--회원 가입시 로그인페이지로 돌아가는 화면
function historyBack() {
	window.history.back();
}


function deleteCookie(name) {

	//localhost 나중에 바꿔야할듯
	document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;domain=localhost;path=/;';
}

function reload() {
	location.reload();
}

function checkSize(input) {
	if (input.files && input.files[0].size > (5 * 1024 * 1024)) {
		alert("파일 사이즈가 5mb 를 넘습니다. 5mb 이하만 가능합니다.");
		input.value = null;
	}
}

function searchTitle(e) {
	let searchTitle = document.getElementById("stitle").value;
	console.log(searchTitle);
	if (searchTitle.length < 2 || searchTitle.length > 7) {
		alert("글자수는 2~6자 사이입니다.");
		e.preventDefault()
		e.stopPropagation()
	}
}

