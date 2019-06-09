<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Home</title>
	 <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	<script>
	function searchCompletion(){
		var jsonStr = makeJsonString();
        $.ajax({
            url: '/completion/search',
            type: 'POST',
            // contentType: 'application/json; charset=UTF-8',
            // contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            crossDomain: true,
            dataType: 'json',
            data: "schWord="+$('#schWord').val(),
            success: function(response) {
                console.log(response);
                var obj = JSON.stringify(response);
                var objJson = JSON.parse(obj);
                var list = objJson.rtnList;
                for(var i=0; i<list.length; i++){
                	console.log(list[i]);
                }
                
            },
            error: function(status){
            	console.log(status);
            }
        });
	}
	
	
	function makeJsonString(){
		 var docJson = new Object();
         var query = new Object();
         var bool = new Object();
         var should = new Array();
         var shouldPrefix = new Object();
         var shouldTerm1 = new Object();
         var shouldTerm2 = new Object();
         var shouldTerm3 = new Object();
         var prefix = new Object();
         
         
         var term = new Object();
         term.titleNgram = "노래";
         shouldTerm1.term = term;
		 term = new Object();
		 term.titleNgramEdge = "노래";
		 shouldTerm2.term = term;
		 term = new Object();
		 term.titleNgramEdgeBack = "노래";
		 shouldTerm3.term = term;
		 prefix.title = "노래";
		 shouldPrefix.prefix = prefix;
         
         should.push(shouldPrefix);
         should.push(shouldTerm1);
         should.push(shouldTerm2);
         should.push(shouldTerm3);
         
         bool.should = should;
         bool.minimum_should_match = 1;
         query.bool = bool;
		 
         docJson.query = query;
         
         var jsonInfo = JSON.stringify(docJson);
         console.log(jsonInfo); //브라우저 f12개발자 모드에서 confole로 확인 가능
         return jsonInfo;
	}
	</script>
</head>
<body>
<h1>
	Music title Completion
</h1>

<P>  Music Title Search Completion</P>

<input id="schWord" type="text" onkeyup="searchCompletion()"/>

</body>
</html>
