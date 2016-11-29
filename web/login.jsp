<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dimata.qdep.form.*" %>
<%@ page import="com.user.entity.*"%>
<%@ page import="com.user.form.*"%>
<%@ page import="com.user.session.*"%>

<%
    SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
    if (userSession == null) {
        userSession = new SessUserSession();
    } else {
%>
<script language="JavaScript">
    window.location = "index.jsp?page=welcome.jsp";
</script>
<%
    }
%>

<%!
    final static int CMD_NONE = 0;
    final static int CMD_LOGIN = 1;
    final static int MAX_SESSION_IDLE = 100000;
%>

<%
    response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "nocache");
%>

<%
    int iCommandLog = Integer.parseInt((request.getParameter("command") == null) ? "0" : request.getParameter("command"));
    int dologin = SessUserSession.DO_LOGIN_OK;

    if (iCommandLog == CMD_LOGIN) {

        String loginID = FRMQueryString.requestString(request, "username");
        String passwd = FRMQueryString.requestString(request, "password");
        String remoteIP = request.getRemoteAddr();
        SessUserSession userSess = new SessUserSession(remoteIP);
        dologin = userSess.doLogin(loginID, passwd);
        System.out.println(iCommandLog + " | " + loginID + " | " + passwd + " | " + userSess + " | dologin=" + (dologin == SessUserSession.DO_LOGIN_OK));

        switch (dologin) {
            case SessUserSession.DO_LOGIN_OK:
                session.setMaxInactiveInterval(MAX_SESSION_IDLE);
                session.putValue(SessUserSession.HTTP_SESSION_NAME, userSess);
                userSess = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
                System.out.println("userSession after login ----------------->OK");
                break;
            case SessUserSession.DO_LOGIN_ALREADY_LOGIN:
                System.out.println("userSession after login ----------------->ALREADY LOGIN");
                break;
            case SessUserSession.DO_LOGIN_GET_PRIV_ERROR:
                System.out.println("userSession after login ----------------->PRIV ERROR");
                break;
            case SessUserSession.DO_LOGIN_NOT_VALID:
                System.out.println("userSession after login ----------------->NOT VALID");
                break;
            case SessUserSession.DO_LOGIN_NO_PRIV_ASIGNED:
                System.out.println("userSession after login ----------------->NO PRIV ASIGNED");
                break;
            case SessUserSession.DO_LOGIN_SYSTEM_FAIL:
                System.out.println("userSession after login ----------------->LOGIN SYSTEM FAIL");
                break;
        }

    }
%>

<%
    if ((iCommandLog == CMD_LOGIN)) {
        if ((iCommandLog == CMD_LOGIN) && (dologin == SessUserSession.DO_LOGIN_OK)) {
%>
<script language="JavaScript">
    window.location = "index.jsp";
</script>
<%
        }
    }
%>

<br>
<div class="col-md-6 col-md-offset-3">
    <form class="form-horizontal" method="post" name="frmLogin" action="">
        <input type="hidden" name="command" value="<%=CMD_LOGIN%>">
        <div class="panel panel-default">
            <div class="panel-heading">User Login</div>
            <div class="panel-body">
                <div class="form-group has-error text-center">
                    <span class="help-block">
                        <% if ((iCommandLog == CMD_LOGIN) && (dologin != SessUserSession.DO_LOGIN_OK)) {
                        %>
                        <%=SessUserSession.soLoginTxt[dologin]%>                                                           
                        <%
                            }
                        %>
                    </span>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label" for="code">Username</label>  
                    <div class="col-md-7">
                        <input name="username" type="text" placeholder="Username" class="form-control" autocomplete="off" required="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label" for="code">Password</label> 
                    <div class="col-md-7">
                        <input type="password" name="password" placeholder="Password" class="form-control" autocomplete="off">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-3"></div>
                    <div class="col-md-7">
                        <button type="submit" class="btn btn-primary btn-save">Login</button>
                        <button type="reset" class="btn btn-default btn-cancel">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>