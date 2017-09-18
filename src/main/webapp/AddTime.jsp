<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	   
 <%@ page import = "java.io.*,java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="style.css" />
<title>Rounded Template</title>
</head>
<body>

<div id="wrap">

<div id="header">
<h1><a href="#">Hutt City Parking</a></h1>
</div>

<div id="menu">
<ul>
<li><a href="Home.jsp">Home</a></li>
<li><a href="AddTime.jsp">Add Time</a></li>
<li><a href="Login.jsp">Log Out</a></li>
</ul>
</div>

<div id="contentwrap">

<div id="content">

<h2>Add Time</h2>

<form method="post" action="LoginCheck">
	<table>
			<tr>
				<td>Park Number</td>
				<td>******************</td>
			</tr>

						<tr>
				<td>Time remaining</td>
	
       
    
				<td>  <%
            // Set refresh, autoload time as 5 seconds
           // response.setIntHeader("Refresh", 1);
            
            // Get current time
            Calendar calendar = new GregorianCalendar();
            
            String am_pm;
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            
            if(calendar.get(Calendar.AM_PM) == 0) 
               am_pm = "AM";
            else
               am_pm = "PM";
               String CT = hour+":"+ minute + am_pm;
               out.println( CT + "\n");
         %></td>
			</tr>
			
			<tr>
				
				<td>Enter minutes to add</td>
				<td><input name="minutes" type="text"></td>
				<td><input type="submit" value="Update"></td>
				
			

			</tr>
		
					<tr>
				
				<td>New expiry time</td>
				<td><output type="text" name="expire"></td>
				<td>*****************</td>
				

			</tr>
  
			<tr>
				
				<td>Amount to pay:</td>
				<td> <%

              // String CT = minutes;
               out.println( CT + "\n");
         %></td>
				

			</tr>
			<tr>
				<td><input type="button" value="Pay using saved credit card" onclick="Paid();"></td>
				<td><input type="submit" value="Add new credit card"></td>
			</tr>
		</table>
	</form>

        
     
        <script type="text/javascript">
         <!--
            function Paid() {
        	 alert ("Thank you, payment was successful.\n Your parking has been extended.");
               
            }
         //-->
      </script>


<p>



</p>

</div>

<div style="clear: both;"> </div>

</div>




</div>

</body>
</html>







