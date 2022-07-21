<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html lang="en">

<head>
<meta charset="UTF-8" />
<title>Document</title>
<script src="/js/table.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">
		<div class="row" style="float: none; margin: 100 auto">
			<div>
				<br>
				<h2>전체 게시판</h2>
				<table id="mytable" class="table table-bordred table-striped mt-5">
					<thead>
						<th class="col-md-1 text-center">번호</th>
						<th class="col-md-5 text-center">제목</th>
						<th class="col-md-2 text-center">조회수</th>
						<th class="col-md-2 text-center">ID</th>
						<th class="col-md-2 text-center">작성일자</th>
					</thead>
					<c:forEach items="${board1}" varStatus="status" var="i">
						<tr>
							<td class="text-center">${status.count}</td>
							<td style="padding-left: 3rem;"><a
								href="/board/view/${i.getBoardNumber()}/${i.getAid()}"
								style="text-decoration: none; color: black;">
									${i.getTitle()}</a></td>
							<td class="text-center">${i.getViews()}</td>
							<td class="text-center">${i.getUserId()}</td>
							<td class="text-center">${i.getDate()}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>

				<div class="d-flex justify-content-between mb-3">
					<c:if test="${member1 != 'nonmember'}">
						<div style="display: inline-block">
							<a href="/board/writeFormPage" class="btn btn-dark"
								style="display: inline-block;">글쓰기</a>
						</div>
					</c:if>
					<c:if test="${member1 == 'nonmember'}">
						<div style="display: inline-block; visibility: hidden;">배치용</div>
					</c:if>
					<div style="display: inline-block">
						<nav aria-label="Page navigation example"
							style="margin-left: 10rem">
							<ul class="pagination" id="pageList123">

								<c:choose>
									<c:when test="${page.get(0) > 5 && search eq 'search' }">
										<li class="page-item" style="margin: 0; padding: 0;"><a
											class="page-link"
											href="/board/list/args/${page.get(0)-1}?title=${title}"
											style="color: black;">이전</a></li>
									</c:when>
									<c:when test="${page.get(0) > 5 && search != 'search' }">
										<li class="page-item" style="margin: 0; padding: 0;"><a
											class="page-link" href="/board/main/${page.get(0)-1}"
											style="color: black;">이전</a></li>
									</c:when>
								</c:choose>

								<c:forEach var="pg" varStatus="pages" items="${page}">
									<li class="page-item" style="margin: 0; padding: 0;"><c:choose>
											<c:when test="${search eq 'search'}">
												<c:if test="${selectpage == pg}">
													<a class="page-link bg-dark text-white"
														href="/board/list/args/${pg}?title=${title}"> ${pg} </a>
												</c:if>
													<c:if test="${selectpage != pg}">
													<a class="page-link bg-white text-dark"
														href="/board/list/args/${pg}?title=${title}"> ${pg} </a>
												</c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${selectpage == pg}">
													<a class="page-link bg-dark text-white"
														href="/board/main/${pg}"> ${pg} </a>
												</c:if>
												<c:if test="${selectpage != pg}">
													<a class="page-link bg-white text-dark"
														href="/board/main/${pg}"> ${pg} </a>
												</c:if>

											</c:otherwise>
										</c:choose></li>
								</c:forEach>

								<c:choose>
									<c:when test="${page.size() == 5 && search eq 'search' }">
										<li class="page-item" style="margin: 0; padding: 0"><a
											class="page-link"
											href="/board/main/${page.get(page.size()-1)+1}"
											style="color: black;">다음</a></li>
									</c:when>
									<c:when test="${page.size() == 5 && search != 'search'}">
										<li class="page-item" style="margin: 0; padding: 0"><a
											class="page-link"
											href="/board/main/${page.get(page.size()-1)+1}"
											style="color: black;">다음</a></li>
									</c:when>
								</c:choose>

							</ul>
						</nav>
					</div>
					<div style="display: inline-block">
						<!-- 검색 버튼 누를 때 -->
						<form action="/board/list/args/1" method="get">
							<div class="form-outline" style="display: inline-block">
								<input type="search" id="stitle" name="title"
									class="form-control" placeholder="제목만 검색됩니다."
									style="display: inline-block;" />
							</div>
							<div class="form-outline"
								style="display: inline-block; position: relative; bottom: 2px;">
								<button id="search-button" type="submit" class="btn btn-dark"
									style="display: inline-block;" onclick="searchTitle(event)">검색</button>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>
	</div>



</body>
</html>