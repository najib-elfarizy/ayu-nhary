<!DOCTYPE html>
<%@ include file = "session.jsp" %> 
<html>
    <head>
        <meta charset="utf-8" />
        <title>Penjadwalan Proyek</title>
        <link rel="stylesheet" type="text/css" href="style.css" media="all" />
        <link href="cssmenu.css" rel="stylesheet" type="text/css"/>
        <script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
        <script src="script.js"></script>
    </head>
    <body>
        <div id="header">

        </div>
        <div id="body">

            <div class="body">
                <div class="section">
                    <img src="images/gears.jpg" alt="" />		
                </div>	
                <div class="article">
                    <a href='index.jsp'><img src="images/graph2.jpg" alt="" /></a>		
                    <h4>CV. EQ Computer</h4>	
                    <p>CV. EQ Computer adalah perusahaan yang dipercaya dengan kualitas yang dihasilkan. hal ini menjadikan CV. EQ Computer menjadi salah satu perusahaan yang dipercaya di wilayah Sesetan dan sekitarnya.</p>
                </div>
                <div class="section">
                    <img src="images/globe.jpg" alt="" />			
                </div>		
            </div>

            <div style="min-height: 270px; width: 960px; margin-left: auto; margin-right: auto; margin-top: -30px;">
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
                                <%if (privProyek) {%>
                                <li class='has-sub'><a href='index.jsp?page=master/proyek.jsp'><span>Proyek</span></a>
                                </li>
                                <%}%>
                                <%if (privMaintenanceKeahlian) {%>
                                <!--<li class='has-sub'><a href='index.jsp?page=master/karyawan.jsp'><span>Karyawan</span></a></li>-->
                                <li class='has-sub'><a href='index.jsp?page=master/pegawai.jsp'><span>Pegawai</span></a></li>
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
                                <li class='has-sub'><a href='#'><span>Guide</span></a>
                                </li>
                                <li class='has-sub'><a href='#'><span>About Us</span></a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <div id="isi" style="padding: 10px; min-height: 270px;">
                    <%
                        String loadpage;
                        loadpage = request.getParameter("page");
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
        </div>
        <div id="footer">

            <div>
                <p>&copy Copyright 2015. All rights reserved<br/>
                    CV. EQ Computer - Denpasar Bali</p>
            </div>
        </div>
    </body>
</html>