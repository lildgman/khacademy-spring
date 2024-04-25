<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body onload="init()">
	<jsp:include page="common/header.jsp" />
	<div class="content">
		<br><br>

		<div class="innerOuter">
			<h4>게시글 TOP 5</h4>
			<br>
			<table id="boardList" class="table table-hover" align="center">
				<thead>
					<tr>
						<th>글번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>첨부파일</th>
					</tr>
				</thead>
				<tbody>
					<!-- 현재 조회수가 가장 높은 상위 5개의 게시글을 조회해서 그리기(ajax)-->
					<!-- <tr>
						<th>글번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>첨부파일</th>
					</tr> -->
				</tbody>
			</table>
		</div>
		<script>
			function init() {
				// 서버로부터 조회수가 높은 게시글 5개 조회 => ajax통신
				// tbody 요소에 추가
				getTopBoardList(function(list) {
					drawTopBoardListBody(list);
				})
			}

			function drawTopBoardListBody(list) {
				const tbody = document.querySelector('#boardList > tbody');
				$(tbody).empty();
				for(let board of list) {
					const tr = document.createElement("tr");
					tr.innerHTML = "<td>" + board.boardNo + "</td>"
									+ "<td>" + board.boardTitle + "</td>"
									+ "<td>" + board.boardWriter + "</td>"
									+ "<td>" + board.count + "</td>"
									+ "<td>" + board.createDate + "</td>"
									+ "<td>" + (board.originName != null ? "★" : "" )+ "</td>";
					tr.onclick = function() {
						location.href = "detail.bo?bno="+board.boardNo;
					}
					tbody.appendChild(tr);
				}
			}

			// 서버로부터 조회수가 높은 게시글 5개 가져오는 함수
			function getTopBoardList(callback) {
				$.ajax({
					url: "topList.bo",
					success: callback,
					error : function() {
						console.log("게시물 조회 실패")
					}
				})
			}

		</script>

	</div>
	<jsp:include page="common/footer.jsp" />
</body>
</html>