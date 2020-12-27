
<%@page import="ru.javabegin.training.web.beans.Author"%>
<%@page import="ru.javabegin.training.web.beans.AuthorList"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Онлайн библиотека</title>
        <link href="../css/style_main.css" rel="stylesheet" type="text/css">
    </head>
    <body>

        <div class="container">


            <div class="header">
                <img alt="Место для логотипа" name="logo" width="100%" height="90"/>

                <form class="search_form" name="search_form" method="POST">
                    <img src="../images/search.jpg"/> 
                    <input type="text" name="search_String" value="" size="100" />
                    <input type="submit" value="Поиск" name="search_button" />
                    <select name="search_option">
                        <option>Название</option>
                        <option>Автор</option>
                    </select>
                </form>
            </div>




            <div class="sidebar1">
                <h4>Список авторов:</h4>
                <ul class="nav">
                    <% AuthorList authorList = new AuthorList();
                        for (Author author : authorList.getAuthorList()) {
                    %>
                    <li><a href="#"><%=author.getName()%></a></li>

                    <%}%>
                </ul>
                <p>&nbsp;</p>
            </div>




            <div class="content">
                <h1>&nbsp;</h1>
                <p>&nbsp;</p>
            </div>


                    
        </div><!-- end .container -->
                
    </body>
</body>

</body>
</html>
