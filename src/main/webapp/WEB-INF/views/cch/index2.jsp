<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Korean Dictionary Search By Ealsticsearch</title>
<!-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"> -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/jui-ui.classic.css">
<script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jui-core.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jui-ui.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>


<script type="text/javascript">
	var rtnArray;
	// add Div btn event
/* 	jui.ready([ "ui.accordion" ], function(accordion) {
		accordion_1 = accordion("#accordion_1", {
			event : {
				open : function(index, e) {
					$(this.root).find("i").attr("class", "icon-arrow1");
					$(e.target).find("i").attr("class", "icon-arrow3");
				}
			},
			index : 0,
			autoFold : true
		});
	}); */
	// add autocomplete to Input Box
/* 	$(function() {
		$("#schWord").autocomplete({
			source : function(request, response) {
				$.ajax({
					url : '/completion/autoComplete',
					type : 'POST',
					dataType : 'json',
					data : "schWord=" + $('#schWord').val(),
					success : function(data) {
						response($.map(data, function(item) {
							var obj = JSON.stringify(data);
							var objJson = JSON.parse(obj);
							var list = objJson.rtnList;
							return {
								value : item.musicTitle,
								label : item.musicTitle
							}
						}));
					}
				})
			},
			open : function() {
				$(this).autocomplete("widget").width("323px");
				$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
			}
		}).data('uiAutocomplete')._renderItem = function(ul, item) {
			return $("<li style='cursor:hand; cursor:pointer;'></li>").data(
					"item.autocomplete", item).append(
					"<a onclick=\"cityValue('" + item.value + "');\">"
							+ unescape(item.label) + "</a>").appendTo(ul);
		};

	}); */
	
	
	$(function() {
		
		$("#schWord").autocomplete({
			source : function(request, response) {
				$.ajax({
					url : '/completion/autoComplete',
					type : 'POST',
					dataType : 'json',
					contentType: 'application/json',
					data : getJsonData(),
					success : function(data) 
								{response($.map(data,function(item) {
									var obj = JSON.stringify(data);
									var objJson = JSON.parse(obj);
									var list = objJson.rtnList;
									return {
										value : item.voca
									}}));
					}
				})
			}
		})
	});
	
	function getJsonData(){
		var jsonQuery = {
				"schWord":$('#schWord').val()
		}
		console.log(jsonQuery);
		return JSON.stringify(jsonQuery);
	}
	
	/*****************************
	 * Body Onload 
	 *****************************/
	function bodyLoad(){
		$(document).ready(function(){
			$('#schWord').keyup(function(event){
				if(event.which == 13){
					btnClick_e();
				}
			});
		});
	} 
	
	function loadIdx() {
		
		var div; //= document.createElement('div');
		var divClass;
		var text; // = document.createTextNode('Group Item ');
		var iTag;// = document.createElement('i');
		
		// $('<i class="icon-arrow1"></i>');
		for(var j=0; j<rtnArray.length; j++){
			div = document.createElement('div');
			divClass = document.createAttribute('class');
			divClass.value = 'title';
			div.setAttributeNode(divClass);
			text = document.createTextNode(rtnArray[j].voca);
			div.appendChild(text);
			
			$('#accordion_2').append(div);
		}
		// content
		var divCtnt = document.createElement('div');
		var divCtntClass = document.createAttribute('class');
		var divCtntId = document.createAttribute('id');
		var ctntText;
		var assignDiv;
		divCtntClass.value = 'content';
		divCtntId.value = 'ctntid';
		divCtnt.setAttributeNode(divCtntClass);
		divCtnt.setAttributeNode(divCtntId);
		$('#accordion_2').append(divCtnt);
		
		jui.ready(['ui.accordion'], function(accordion){
			accordion_2 = accordion('#accordion_2', {
				event: {
					open:function(index, e){
						$(this.root).find('i').attr('class','icon-arrow1');
						$(e.target).find('i').attr('class','icon-arrow3');
						$('#ctntid').text(rtnArray[index].meaning);
						console.log(index);
					}
				},
				autoFold:true
			});
		});
		/*
		<div class="title">
			Group Item #3 <i class="icon-arrow1"></i>
		</div>
		*/
	}
	/*****************************
	 * Search Event Click 
	 *****************************/
	function btnClick_e(){
		var divList = document.getElementById("accordion_2");
		while(divList.hasChildNodes()){
			divList.removeChild(divList.firstChild);
		}
		
		$.ajax({
			url : '/completion/search',
			type : 'POST',
			dataType : 'json',
			contentType: 'application/json',
			data : getJsonData(),
			success : 
				function(data) {
				/*response($.map(data, function(item) {
					var obj = JSON.stringify(data);
					var objJson = JSON.parse(obj);
					var list = objJson.rtnList;
					return {
						value : item.musicTitle,
						label : item.musicTitle
					}
				}));*/
				// console.log(data);
				// typeof 객체와 기본타입 구분
				// instanceof 어떤 클래스의 인스턴스인지 알기 위함 (연산자 필요 return -> true/false)
				var str = JSON.stringify(data);			// Object -> String
				var jsonObj = JSON.parse(str);			// String -> JSONObject
				
				rtnArray = jsonObj;
				loadIdx();
				// console.log(jsonObj instanceof JSON);
			} 
			
		});
	}

	
	
</script>

</head>
<body class="jui" onload="bodyLoad()">

	<label>Search Word</label><br/>
	<input type="text" class="input" id="schWord" placeholder="단어를 입력해주세요"/>
	<a class="btn" onclick="btnClick_e()">Search</a>
	<br/><br/>
	<div id="accordion_2" class="accordion">
	</div>
</body>
</html>
