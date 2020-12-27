<%@page import="calc.TestConnection" %>
<%
    String header = "Glassfish";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>First JSP App</title>
</head>
<body>
<h2><%= header %></h2>
<p>Today <% TestConnection.test(); %></p>
</body>
</html>