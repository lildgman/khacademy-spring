<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세보기</title>
<style>
	table * {margin:5px;}
	table {width:100%;}
</style> 
</head>
<body>
	<jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>게시글 상세보기</h2>
            <br>

            <a class="btn btn-secondary" style="float:right;" href="list.bo">목록으로</a>
            <br><br>

            <table id="contentArea" algin="center" class="table">
                <tr>
                    <th width="100">제목</th>
                    <td colspan="3">${board.boardTitle }</td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td>${board.boardWriter }</td>
                    <th>작성일</th>
                    <td>${board.createDate }</td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td colspan="3">
						<c:choose>
							<c:when test="${not empty board.originName }">
								<!-- case1 -->
                        		<a href="${board.changeName }" download="${board.originName }">${board.originName }</a>
							</c:when>
				
                        	<c:otherwise>
                        		<!-- case2 -->
								첨부파일이 없습니다.
                        	</c:otherwise>
							
						
						</c:choose>
							
		
                    </td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="4"><p style="height:150px;">${board.boardContent }</p></td>
                </tr>
            </table>
            <br>

   			
   			<!-- 수정하기, 삭제하기 버튼은 이 글이 본인이 작성한 글일 경우에만 보여져야 함 -->
            <div align="center">
            	<c:if test="${loginUser.userId eq board.boardWriter }">
                	<a class="btn btn-primary" onclick="postFormSubmit('edit')">수정하기</a>
                	<a class="btn btn-danger" onclick="postFormSubmit('delete')">삭제하기</a>
                </c:if>
            </div>
            <br><br>

            <form action="" method="POST" id="postForm">
                <input type="hidden" name="bno" value="${board.boardNo}">
            </form>

            <script>
                function postFormSubmit(type) {
                    const formEl = document.querySelector('#postForm');
                    switch(type) {
                        case 'edit':{
                            formEl.action = "updateForm.bo";
                            // formEl.setAttribute("action", "updateForm.bo");
                        } break;

                        case 'delete' :{
                            formEl.action = "delete.bo";
                            // formEl.setAttribute("action", "delete.bo");
                        } break;
                    }
                    $(formEl).submit();
                }

            </script>
           
            
             <form action="" method="post" id="postForm">
           		<input type="hidden" name=bno value="7">
           		<input type="hidden" name="filePath" value="이미지.jpg">
             </form>
            
          
            <table id="replyArea" class="table" align="center">
                <thead>
                	<c:choose>
                		<c:when test="${empty loginUser }">
		                	<tr>
		                        <th colspan="2">
		                            <textarea class="form-control" readonly cols="55" rows="2" style="resize:none; width:100%;">로그인 후 이용 가능합니다.</textarea>
		                        </th>
		                        <th style="vertical-align:middle"><button class="btn btn-secondary disabled">등록하기</button></th>
		                    </tr>
	                	</c:when>	
	                    
	                    <c:otherwise>
		                     <tr> 
		                        <th colspan="2">
		                            <textarea class="form-control" id="content" cols="55" rows="2" style="resize:none; width:100%;"></textarea>
		                        </th>
		                        <th style="vertical-align:middle"><button class="btn btn-secondary" onclick="addReply();">등록하기</button></th>
		                    </tr>
	                    </c:otherwise>
  	
                	</c:choose>
         
                    <tr>
                        <td colspan="3">댓글(<span id="rcount">0</span>)</td>
                    </tr>
                    

                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
        <br><br>
        <script>
            $(function() {
                getReplyList({bno: ${board.boardNo}}, function(res) {
                    const replyBody = document.querySelector("#replyArea tbody");
                    const list = [];
                    for(let r of res) {
                        list.push({
                            tdData1:r.replyWriter,
                            tdData2:r.replyContent,
                            tdData3:r.createDate,
                            rowEvent: function() {
                                console.log(r);
                            }
                        })
                    }

                    drawTableList(list, replyBody);
                })
            })

            function getReplyList(data, callback) {
                $.ajax({
                    url:'rlist.bo',
                    data : data,
                    success: function(res) {
                        callback(res);
                    },

                    error: function(item) {
                        console.log(item);
                    }
                })
            }

            function drawTableList(itemList, parent) {
                for(let item of itemList) {
                    const replyRow = document.createElement('tr');
                    replyRow.innerHTML = `
                        <th>`+item.tdData1+`</th>
                        <td>`+item.tdData2+`</td>
                        <td>`+item.tdData3+`</td>`;
                    parent.append(replyRow);

                    replyRow.onclick=function() {
                        console.log(reply);
                    }
                }
            }

        </script>

    </div>
    
    <jsp:include page="../common/footer.jsp" />
</body>
</html>