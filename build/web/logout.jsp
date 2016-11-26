<%@ page language="java" %> 
<%@ include file = "session.jsp" %> 
<%@ page import="com.user.session.*" %>
<%@ page import="com.dimata.qdep.form.*" %>

<%
    
try {
        if(userSession.isUserLoggedIn()==true){
            System.out.println("doLogout"); 
            userSession.printAppUser();
            userSession.doLogout(); 
            session.removeValue(SessUserSession.HTTP_SESSION_NAME);
            isLoggedIn =false;
        }

    } catch (Exception exc){
      System.out.println(" ==> Exception during logout user");
    }

%>

<%

response.sendRedirect("/proyek/index.jsp?page=login.jsp");

%>

