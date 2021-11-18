/**
 * 
 */
 

 $(function() {
	
	var form = $("#board");
	var boardNo = $("#boardNo").val();
	
	var form = $("#board");
	// 등록 버튼 클릭 이벤트
	$("#btnInsert").on("click", () => {
		form.submit();
	});
	
	// 수정 화면 이동 클릭 이벤트
	$("#btnMoveUpdate").on("click", () => {
		location.href ="/board/update?boardNo=" +boardNo;
	});
	
	// 수정 버튼 클릭 이벤트
	$("#btnUpdate").on("click", () => {
		form.attr("action", "/board/update")
		form.submit();
	});
	
	// 삭제 버튼 클릭 이벤트
	$("#btnDelete").on("click", () => {
		form.attr("action", "/board/delete");
		form.submit();
	});
	
	// 목록 버튼 클릭 이벤트
	$("#btnList").on("click", () => {
		location.href ="/board/list";
	});
});