<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="renderer" content="webkit">
<meta http-equiv="expires" content="0" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<title>去支付</title>
<script type="text/javascript">
		var base = "${ctx}";
	</script>
</head>
<body onload="autosubmit()">
	<form action="http://maymonkey.com/pro_3/example/native.php"
		method="post" id="batchForm">
		<input type="hidden" name="total_fee" value="${map1.total_fee}"><br />
		<input type="hidden" name="out_trade_no" value="${map1.out_trade_no}"><br />
		<input type="hidden" name="body" value="${map1.body}"><br />
	</form>
	<script type="text/javascript">
		function autosubmit(){
			  document.getElementById("batchForm").submit();   
		}	
	</script>
</body>
</html>