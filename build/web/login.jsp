<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dimata.qdep.form.*" %>
<%@ page import="com.user.entity.*"%>
<%@ page import="com.user.form.*"%>
<%@ page import="com.user.session.*"%>

<%
SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
if(userSession==null){
    userSession = new SessUserSession();
}else{
%>
<script language="JavaScript">
window.location = "index.jsp?page=welcome.jsp";
</script>
<%
}
%>

<%!
 final static int CMD_NONE =0;
 final static int CMD_LOGIN=1;
 final static int MAX_SESSION_IDLE=100000;
%>

<%
response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "nocache");
%>

<%
   int iCommandLog = Integer.parseInt((request.getParameter("command")==null) ? "0" : request.getParameter("command"));
   int dologin = SessUserSession.DO_LOGIN_OK;

     if(iCommandLog==CMD_LOGIN){

        String loginID = FRMQueryString.requestString(request,"username");
        String passwd  = FRMQueryString.requestString(request,"password");
        String remoteIP = request.getRemoteAddr();
        SessUserSession userSess = new SessUserSession(remoteIP );
        dologin=userSess.doLogin(loginID, passwd);
        System.out.println(iCommandLog+" | "+loginID+" | "+passwd+" | "+userSess+" | dologin="+ (dologin==SessUserSession.DO_LOGIN_OK));
        
        switch (dologin){
            case SessUserSession.DO_LOGIN_OK:
                session.setMaxInactiveInterval(MAX_SESSION_IDLE);
                session.putValue(SessUserSession.HTTP_SESSION_NAME, userSess);
                userSess = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
                System.out.println("userSession after login ----------------->OK");
                break;
            case SessUserSession.DO_LOGIN_ALREADY_LOGIN :
                System.out.println("userSession after login ----------------->ALREADY LOGIN");
                break;
            case SessUserSession.DO_LOGIN_GET_PRIV_ERROR :
                System.out.println("userSession after login ----------------->PRIV ERROR");
                break;
            case SessUserSession.DO_LOGIN_NOT_VALID :
                System.out.println("userSession after login ----------------->NOT VALID");
                break;
            case SessUserSession.DO_LOGIN_NO_PRIV_ASIGNED :
                System.out.println("userSession after login ----------------->NO PRIV ASIGNED");
                break;
            case SessUserSession.DO_LOGIN_SYSTEM_FAIL :
                System.out.println("userSession after login ----------------->LOGIN SYSTEM FAIL");
                break;                
        }
            
    }
%>

<script language="JavaScript">
function cmdLogin()
{
document.frmLogin.action = "index.jsp?page=login.jsp";
document.frmLogin.submit();
}
</script>

<%
if(  (iCommandLog==CMD_LOGIN) ) {
if(  (iCommandLog==CMD_LOGIN) && (dologin == SessUserSession.DO_LOGIN_OK))	{
%>
<script language="JavaScript">
window.location = "index.jsp"
</script>
<% 
}} 
%>


<form id="login" method="post" name="frmLogin" action=""><input type="hidden" name="command" value="<%=CMD_LOGIN%>">
    <table width="318" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="68">&nbsp;</td>
          </tr>
  </table>
<table bgcolor="#009999"   align="center">          
<tr>               
                <td width="73">Username</td>
                <td   bgcolor="#009999"   width="144"><input name="username" type="text" id="username"  /></td>
              </tr>
              <tr>                
                <td>Password</td>
                <td   bgcolor="#009999"  ><input name="password" type="password" id="password" /></td>
              </tr>
              <tr>
                
                <td align="center">&nbsp;</td>
                <td ><input type="submit" name="login" id="login" value="Login" onClick="javascript:cmdLogin()" /></td>
              </tr>
  </table>
<table width="318" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
            <td>&nbsp;</td>
          </tr>
  </table>
         <p align="center">
        <%
if(  (iCommandLog==CMD_LOGIN) && (dologin != SessUserSession.DO_LOGIN_OK)) {
%>
<%=SessUserSession.soLoginTxt[dologin]%>                                                           
<%
} 
%></p></form>

