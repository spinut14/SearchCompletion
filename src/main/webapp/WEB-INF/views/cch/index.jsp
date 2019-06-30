<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width">
    <meta name="description" content="Location of Korea Post">
    <meta name="keywords" content="HTML,CSS,XML,JavaScript">
    <meta name="author" content="chulhwan cho">
    <title>Korean Dictionary Search By Ealsticsearch</title>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" >
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
    </script>
    
  </head>
  <body>
    <header>
      <div class="container">
        <div id="toptitle">
          <h2>표준국어대사전 찾기<span class="highlight"> by Elasticsearch</span></h2>
        </div>
        <nav>
          <ul>
            <li class="current"><a href="/main">Main</a></li>
            <li><a href="about.html">About</a></li>
            <li><a href="service.html">Services</a></li>
           </ul>
          </nav>
      </div>
    </header>

    <section id="subtitle">
      <div class="container">
        <h3>Find Word</h3>
      </div>
    </section>

    <section id="searchArea">
      <div class="container">
        <form>
          <input type="text" id="schWord" placeholder="단어를 입력해주세요">
          <button type="submit" class="button_1">검색</button>
        </form>
      </div>
    </section>

    <section id="boxes">
      <div class="container">
        <div class="box">
          <h3>HTML5 Markup</h3>
        </div>
        <div class="box">
          <h3>Graphic Design</h3>
        </div>
      </div>
    </section>

    <footer>
      <p>Acme Web Design, Copyright &copy; 2019</p>

  </body>
</html>
