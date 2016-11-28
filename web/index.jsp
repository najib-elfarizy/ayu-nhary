<!DOCTYPE html>
<%@ include file = "session.jsp" %> 
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Penjadwalan Proyek</title>
        <!--<link rel="stylesheet" type="text/css" href="style.css" media="all" />-->
        <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/main.css" rel="stylesheet" type="text/css"/>
        <link href="assets/cssmenu.css" rel="stylesheet" type="text/css"/>
        
        <script src="assets/jquery-1.11.1.min.js" type="text/javascript"></script>
        <script src="assets/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="assets/bootbox.min.js" type="text/javascript"></script>
        <script src="assets/main.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <a href='index.jsp'>
                            <img class="img-responsive" src="images/logo_SME1.jpg"/>
                        </a>
                    </div>
                    <div class="col-sm-4 hidden-xs">
                        <h3><%= PstAppConfig.getByName("app_brand").getConfigValue() %></h3>
                        <p><%= PstAppConfig.getByName("app_desc").getConfigValue() %></p>
                    </div>
                </div>
            </div>

            <div id='cssmenu'>
                <ul>
                    <li class='active has-sub'><a href='index.jsp'><span>Home</span></a>
                        <ul>
                            <%if (isLoggedIn == false) {%> 
                            <li class='has-sub'><a href='index.jsp?page=login.jsp'><span>Login</span></a>
                            </li>
                            <li class='has-sub'><a href='index.jsp?page=register.jsp'><span>Register</span></a>
                            </li>
                            <%} else {%>
                            <li class='has-sub'><a href='logout.jsp'><span>Logout</span></a>         
                            </li><%}%>
                        </ul>
                    </li>
                    <%if (privView) {%>
                    <li class='active has-sub'><a href='#'><span>View</span></a>
                        <ul>

                            <li class='has-sub'><a href='index.jsp?page=view/proyek.jsp'><span>Proyek</span></a>
                            </li>


                            <li class='has-sub'><a href='index.jsp?page=view/jadwal.jsp'><span>Jadwal</span></a>
                            </li>

                        </ul>
                    </li>
                    <%}%>
                    <%if (privMaintenanceData) {%>
                    <li class='active has-sub'><a href='#'><span>Maintenance Data</span></a>
                        <ul>
                            <%if (privMaintenanceKeahlian) {%>
                            <!--<li class='has-sub'><a href='index.jsp?page=master/karyawan.jsp'><span>Karyawan</span></a></li>-->
                            <li class='has-sub'><a href='index.jsp?page=master/pegawai.jsp'><span>Pegawai</span></a></li>
                            <%}%>
                            <%if (privProyek) {%>
                            <li class='has-sub'><a href='index.jsp?page=master/proyek.jsp'><span>Proyek</span></a>
                            </li>
                            <%}%>
                            <%if (privJadwal) {%>
                            <li class='has-sub'><a href='index.jsp?page=master/jadwal.jsp'><span>Jadwal</span></a>
                            </li>
                            <%}%>
                            <%if (privDetailJadwal) {%>
                            <li class='has-sub'><a href='index.jsp?page=master/detailjadwal.jsp'><span>Detail Jadwal</span></a>
                            </li>
                            <%}%>
                        </ul>
                    </li>
                    <%}%>
                    <%if (privPenjadwalanGA) {%>
                    <li class='active has-sub'><a href='#'><span>Penjadwalan</span></a>
                        <ul>
                            <%if (privPembentukanJadwal) {%>
                            <li class='has-sub'><a href='index.jsp?page=penjadwalan.jsp'><span>Jadwal Proyek</span></a>
                            </li>
                            <%}%>
                        </ul>
                    </li>
                    <%}%>
                    <%if (privManagementUser) {%>
                    <li class='active has-sub'><a href='#'><span>User Management</span></a>
                        <ul>
                            <%if (privUsers) {%>
                            <li class='has-sub'><a href='index.jsp?page=user/appuser.jsp'><span>User</span></a>
                            </li>
                            <%}%>
                            <%if (privGroups) {%>
                            <li class='has-sub'><a href='index.jsp?page=user/appgroup.jsp'><span>Group</span></a>
                            </li>
                            <%}%>
                            <%if (privPrivileges) {%>
                            <li class='has-sub'><a href='index.jsp?page=user/apppriv.jsp'><span>Privilege</span></a>
                            </li>
                            <%}%>
                        </ul>
                    </li>
                    <%}%>
                    <li class='active has-sub'><a href='#'><span>Help</span></a>
                        <ul>
                            <li class='has-sub'><a href='index.jsp?page=guide.jsp'><span>Guide</span></a>
                            </li>
                            <li class='has-sub'><a href='index.jsp?page=aboutus.jsp'><span>About Us</span></a>
                            </li>
                        </ul>
                    </li>
                    <%if (privSettings) {%>
                    <li class="pull-right"><a href='index.jsp?page=settings.jsp'><i class="glyphicon glyphicon-cog"></i></a></li>
                    <%}%>
                </ul>
            </div>

            <div id="isi" class="content">
                <%
                    String loadpage = request.getParameter("page");
                    if (loadpage != null && loadpage != "") {
                %>
                <jsp:include page='<%=loadpage%>' />
                <%
                } else {
                %>
                <%@include file="welcome.jsp"%>
                <%
                    }
                %>                            
            </div>
        </div>

        <div class="footer">
            <div>
                <p><%= PstAppConfig.getByName("app_footer").getConfigValue() %></p>
            </div>
        </div>
    </body>
</html>