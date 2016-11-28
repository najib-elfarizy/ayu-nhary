
<h2>Sistem Penjadwalan <%= PstAppConfig.getByName("app_brand").getConfigValue() %></h2>
<%if (userSession == null) {%>
<h4>Silahkan <a href="index.jsp?page=login.jsp">login</a> untuk menggunakan aplikasi</h4>
<%} else {%>
<h4>Selamat datang <%=userSession.getAppUser().getFullName()%>!</h4>
<%}%>

