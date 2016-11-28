<%@page import="com.user.entity.*"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Vector"%>
<%@page import="com.user.session.SessUserSession"%>

<%
    boolean isLoggedIn = false;
    boolean privView = true;
//    boolean privViewDataKaryawan = false;
    boolean privViewDataPegawai = false;
    boolean privViewKeahlian = false;
    boolean privViewProyek = false;
    boolean privViewJadwal = false;
    boolean privUpdateJadwal = false;
    boolean privMaintenanceData = false;
//    boolean privMaintenanceDataKaryawan = false;
    boolean privMaintenanceDataPegawai = false;
    boolean privMaintenanceKeahlian = false;
    boolean privProyek = false;
    boolean privJadwal = false;
    boolean privDetailJadwal = false;
    boolean privPenjadwalanGA = false;
    boolean privPembentukanJadwal = false;
    boolean privManagementUser = false;
    boolean privUsers = false;
    boolean privGroups = false;
    boolean privPrivileges = false;
    boolean privSettings = false;

    String username = "";
    SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);

    if (userSession == null) {
        username = "Guest";
    } else {
        username = userSession.getAppUser().getFullName();
        isLoggedIn = true;
        Vector vUserGroup = PstUserGroup.list(0, 500, "" + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] + " = " + userSession.getAppUser().getOID(), "");
        if (vUserGroup.size() > 0) {
            String[] groupname = new String[vUserGroup.size()];
            for (int i = 0; i < vUserGroup.size(); i++) {
                groupname[i] = (PstAppGroup.fetchExc(((UserGroup) vUserGroup.get(i)).getIdGroup())).getGroupName();
                AppGroup app_group = PstAppGroup.fetchExc(((UserGroup) vUserGroup.get(i)).getIdGroup());
                Vector vGroupPriv = PstGroupPriv.list(0, 500, "" + PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID] + " = " + app_group.getOID(), "");
                if (vGroupPriv.size() > 0) {
                    String[] grouppriv = new String[vGroupPriv.size()];
                    for (int j = 0; j < vGroupPriv.size(); j++) {
                        grouppriv[j] = String.valueOf((PstAppPriv.fetchExc(((GroupPriv) vGroupPriv.get(j)).getIdPriv())).getOID());
                    }
                    if (Arrays.asList(grouppriv).contains("1")) {
                        privManagementUser = true;
                        privUsers = true;
                        privGroups = true;
                        privPrivileges = true;
                    }
                    if (Arrays.asList(grouppriv).contains("2")) {
                        privMaintenanceData = true;
//                        privMaintenanceDataKaryawan = true;
                        privMaintenanceDataPegawai = true;
                        privMaintenanceKeahlian = true;
                        privProyek = true;
                        privJadwal = true;
                        privDetailJadwal = true;
                    }
                    if (Arrays.asList(grouppriv).contains("3")) {
                        privPenjadwalanGA = true;
                        privPembentukanJadwal = true;
                    }
                    if (Arrays.asList(grouppriv).contains("4")) {
                        privView = true;
                        privViewProyek = true;
                        privViewJadwal = true;
                    }
                    if (Arrays.asList(grouppriv).contains("5")) {
                        privView = true;
                        privViewJadwal = true;
                    }
                    if (Arrays.asList(grouppriv).contains("6")) {
                        privView = true;
                        privViewProyek = true;
                    }
                    if (Arrays.asList(grouppriv).contains("7")) {
                        privView = true;
                        privViewJadwal = true;
                        privUpdateJadwal = true;
                    }
                    if (Arrays.asList(grouppriv).contains("10")) {
                        privSettings = true;
                    }
                }
            }
        }
    }

%>
