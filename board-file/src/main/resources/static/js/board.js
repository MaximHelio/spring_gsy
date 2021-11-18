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
	
	// 파일 삭제 버튼 클릭 이벤트
	$(".btnFileDelete").on("click", function() {
		let fileNo = $(this).attr("data");
		
		if (confirm("정말로 삭제하시겠습니까?"))
			deleteFile(fileNo);
	});
});

function deleteFile(fileNo) {
	
	$.ajax({
		url: 	'/file',
		type:	'delete',
		data:	{
					'fileNo' : fileNo
				},
		dataType: 'text',
		success: function(data) {
			// 서버로부터 정상적인 응답이 왔을 때 실행
			console.log(data)
			location.reload();
		},
		error: function(request, status, error) {
			alert("파일 삭제시, 에러가 발생하였습니다");
		}
	});
	
}