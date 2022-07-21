function pageList1(event,m) {
	console.log(m.innerHTML);
	console.log(event.target.innerHTML);
	
	if(event.target.value == m.value){
		m.style.color = "white";
		m.style.backgroundColor = "black";
	}else{
		m.style.color="black";
		m.style.backgroundColor = "white";
	}



}


function gogo(){
	
	let list = document.querySelectorAll("#pageList123 a");
	
	list.addEventListener("click",pageList(event,a),false);
		
};
