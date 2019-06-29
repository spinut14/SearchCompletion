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
	/* Jquery autocomplete */
	var availableWord = new Array();
/*	$( function() {
		$("#schWord").autocomplete({
			source: availableWord
		});
	});
*/
	/* 검색어 조회 function */ 
	/*function searchCompletion(){
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
                if(availableWord.length > 0){
                	delete availableWord[0];
            	}
                for(var i=0; i<list.length; i++){
                	console.log(list[i]);
                	availableWord.push(list[i]);
                }
        		$("#schWord").autocomplete({
        			source: availableWord
        		});
            },
            error: function(status){
            	console.log(status);
            }
        });
	}*/
	$(function() {
		$("#schWord").autocomplete({
			source: function (request, response){
				$.ajax({
					url: '/completion/search',
		            type: 'POST',
		            dataType: 'json',
		            data: "schWord="+$('#schWord').val(),
		            success: function(data) {
		                /*console.log(data);
		                var obj = JSON.stringify(data);
		                var objJson = JSON.parse(obj);
		                var list = objJson.rtnList;
		                if(availableWord.length > 0){
		                	delete availableWord[0];
		            	}*/
		            	response(
	            			$.map(data, function (item){
	            				var obj = JSON.stringify(data);
	    		                var objJson = JSON.parse(obj);
	    		                var list = objJson.rtnList;
	            				return {	            					
	            						value: item.musicTitle,
				                		label: item.musicTitle
	            				}
		                	})
		                );
		                
		                /*for(var i=0; i<list.length; i++){
		                	console.log(list[i]);
		                	availableWord.push(list[i]);
		                }
		        		$("#schWord").autocomplete({
		        			source: availableWord
		        		});
		                */
		            }     
				})
			},
			open: function() {
	            $( this ).autocomplete("widget").width("323px");
	            $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
	        }
		})
		.data('uiAutocomplete')._renderItem = function( ul, item ) {
        return $( "<li style='cursor:hand; cursor:pointer;'></li>" )
            .data( "item.autocomplete", item )
            .append("<a onclick=\"cityValue('" + item.value + "');\">"  + unescape(item.label) + "</a>")
        .appendTo( ul );
	    };


	});
	
	
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
	<script type="text/javascript">

</script>

</head>
<body>
	<h1>Korean Dictionary AutoCompletion KDY</h1>	
	<div class="ui-widget">
		<label for="schWord">Title : </label>
		<input id="schWord" type="text"/>
	</div>


</body>
</html>
