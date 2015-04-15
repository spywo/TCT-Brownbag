<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title>Autodesk ICP Community</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/community.css" rel="stylesheet">
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery-1.11.2.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>    

    <script src="js/sockjs-0.3.4.min.js"></script>
	<script src="js/stomp.min.js"></script>
	<script src="js/community.js"></script>
  </head>
  <body>
  	 <div class="container-fluid ">
    
    	<%@include file="login.jsp"%>
    	<%@include file="home.jsp"%>
    
	    <div id="Loading_Modal" class="modal fade" tabindex="-1" role="dialog" data-keyboard="false" data-backdrop="static">
		        <div class="modal-dialog">
		            <div class="modal-content">
		                <div class="modal-body">                	              
		                  		<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>                  	                  
		                </div>
		            </div>
		        </div>
		    </div>
		</div>	
     </div>  
        
  </body>
</html>