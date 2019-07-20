<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>Korean Dirctionary Search by Elasticsearch</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<!-- <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script> -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
	
 var getWords = function(){
		var searchWord = document.getElementsByClassName("searchWord")[0].value;
		
		$.ajax({
			url:"/completion/kdy/searchWord",
			data: { word: searchWord},
			method: "POST",
			dataType: "json"
		})
		.done(function(json){
			console.log(json);
			printWord(json);
		})
	}
 
 function printWord(json){
	 console.log("printWord",json)
 	 var list = document.getElementById('word');
	
	 console.log('remove Elemenets',list.childElementCount);
 	 var listCount = list.childElementCount;
	 for(var i=0;i<listCount;i++){
		 list.removeChild(list.firstElementChild);
		 console.log("remove Eleemnt",list.firstElementChild);
	 }
 
	 
	 
	 json.data.forEach( function(result){
		 console.log("count");
		 var resultWord = document.createElement('ul');
		 resultWord.innerHTML = "<li>"+result.voca+" : "+ result.isNative +" : "+ result.meaning+":"+result.partOfSpeech+"</li>"
		 document.getElementById('word').appendChild(resultWord);	 
	 })
 }

</script>

</head>
<body>
	<h1>Korean Dictionary AutoCompletion KDY</h1>	
	<div>
		<label> SearchWord</label>
		<input class="searchWord" type="text" placeholder="input Word"/>
		<Button onclick = "getWords();">Search</Button>
	</div>
	<div id='word'>
	</div>

</body>
</html>
