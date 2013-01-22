<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page = "../include/layout.jsp"/>
<link rel = "stylesheet" href = "../less/datepicker.css">
<script src = "../js/bootstrap-datepicker.js"></script>
<script type = "text/javascript">
	$(function(){
		$("#dp1").datepicker({format: 'dd-mm-yyyy'});	
	});
</script>
<div id = "body-content">
	<form>
  		<fieldset>
    		<legend>
    			Add new voucher
    			<p class = "legend-desc"><i class = "icon-question-sign"></i>Enter details about your voucher and submit to claim your expenses</p>
    		</legend><br>
    		
    		<input class = "span4" type="text" placeholder="Enter a title...">
    		<div class="input-prepend">
				<span class="add-on rupee">`</span>
			  	<input class="span4 prepend-input" id="prependedInput" type="text" placeholder="Enter the amount...">
			</div>
   			<select class = "span4">
   				<option value = ''>Select Voucher Type</option>
   				<option value = 'type1'>Type1</option>
   				<option value = 'type2'>Type2</option>
   			</select><br>
   			<div class = "input-append">
   				<input type="text" class="span4 append-input" placeholder="Select the date..." id="dp1" >
   				<span class = "add-on"><i class="icon-calendar"></i></span>
   			</div>
   			<textarea rows="5" cols = "50" placeholder="Enter a description..."></textarea><br>
			<label>Upload Attachment</label><input class = "span4" type="file">			
    		<br><br><button type="submit" class="btn btn-info">Submit</button>
  		</fieldset>
	</form>
</div>