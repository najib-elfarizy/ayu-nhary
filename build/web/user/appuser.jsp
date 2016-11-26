<%@ page language = "java" %>
<%@ page import = "java.io.*" %>
<%@ page import = "java.util.*" %>

<%@ page import = "org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import = "org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import = "org.apache.commons.fileupload.FileItem" %>

<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@ page import = "com.control.*" %>

<%@ page import = "com.user.entity.*" %>
<%@ page import = "com.user.form.*" %>
<%@ page import = "com.user.session.*" %>

<%!
    /*
    Method untuk memilih image mana yang ditampilkan
    Input:
    - int statusInput: opsi 1, 2, dan 9
    - String defaultFilename: untu menyimpan nama file yang digunakan sebagai default
    - String tempFilename: untuk menyimpan nama file yang disimpan pada saat proses maintenance data sedang berlangsung
    - String filename: untuk menyimpan nama file yang sudah diconfirm untuk disimpan
    Output:
    - String nama file yang akan ditampilkan
    Proses:
    - Apabila status input = 1, defaultFilename
    - Apabila status input = 9, tempFilename
    - Apabila status input = 2 dan filename tersedia, filename
    - Apabila tidak tersedia, defaultFilename
    Opsi Perubahan:
    - ---
    */
    private String choosePicture (int statusInput, String directoryName, String defaultFilename, String tempFilename, String filename) {
        String realFilename = "";
        File file1 = new File(directoryName + "/" + tempFilename);
        if (file1.exists()) realFilename = tempFilename;
        else {
            if (!filename.matches("")) {
                File file2 = new File(directoryName + "/" + filename);
                if (file2.exists()) realFilename = filename;
            }
            else realFilename = defaultFilename;
        }
        return realFilename;
    }
%>

<%!
    /*
    Method untuk melakukan pengecekan apakah input form dengan nama yang sama sudah ada atau belum pada saat pembacaan input dari user
    Input:
    - String a: nilai string yang ingin diketahui sudah ada atau belum
    - Hashtable originalParamValues: data dalam bentuk Hashtable yang berisikan data inputan user yang sudah dibaca sampai saat method ini dijalankan
    Output:
    - boolean apakan nama input form tersebut ada atau tidak
    Proses:
    - Melakukan looping terhadap data yang sudah tersimpan, dan mematch dengan string nama yang ingin dibandingkan
    Opsi Perubahan:
    - ---
    */
    private boolean fieldNameExist(String a, Hashtable originalParamValues) {
        Enumeration K = originalParamValues.keys();
        while (K.hasMoreElements()) {
            if (K.nextElement().equals(a)) return true;
        }
        return false;
    }
%>

<%
    /*variable declaration*/
    int recordToGet = 5; //jumlah record maksimal yang ditampilkan dalam list tabel data
    int statusInput = 0;
    
    int iErrCode = ExtendedFRMMessage.NONE; //untuk menyimpan kode error yang muncul selama proses maintenance data
    String msgString = ""; //untuk menyimpan pesan error utama yang muncul selama proses maintenance data
    String orderClause = PstAppUser.fieldNames[PstAppUser.FLD_USER_ID];//untuk menyimpan orderClause dalam proses menampilkan data dalam list tabel

    String imageDirectory = this.getServletContext().getRealPath("/") + "images"; //untuk menyimpan string directory tempat menyimpan file image
    long maxRequestSize = 102400; //untuk menyimpan data ukuran file maksimal yang diikutkan dalam proses maintenance data

    int iCommand = 0; //untuk menyimpan kode command yang akan dilakukan setelah proses di-submit
    int start = 0; //untuk menyimpan indeks start di list mana data yang sedang menjadi fokus berada
    int prevCommand = 0; //untuk menyimpan nilai dari command yang sebelumnya dilakukan
    long IDAppUser = 0; //untuk menyimpan ID objek yang sedang dimaintenance (0: untuk data baru)
    
    Hashtable paramValues = new Hashtable(); //Hashtable untuk menyimpan data inputan user secara sementara

    boolean isMultipart = ServletFileUpload.isMultipartContent(request); //Melihat apakah tipe form input data adalah Multipart/form-data atau tidak. Ini penting apabila record yang dimaintenance terdapat file yang ikut dimaintenance

    if (isMultipart) {
        String value = "";
        String identitasFile = "";

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        File repository = (File) this.getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Set overall request size constraint
        upload.setSizeMax(maxRequestSize);

        // Parse the request
        List<FileItem> items = upload.parseRequest(request); //memilah request dari user berdasarkan jenis inputnya

        Iterator<FileItem> iter = items.iterator(); //melakukan iterasi terhadap list data inputan user
        while (iter.hasNext()) {
            FileItem item = iter.next();

            if (item.isFormField()) { //untuk membaca inputan user yang bukan file dengan item.getFieldName() dan item.getString()
                if (item.getFieldName().matches("command")) iCommand = Integer.parseInt(item.getString());
                else if (item.getFieldName().matches("vectSize")) start = Integer.parseInt(item.getString());
                else if (item.getFieldName().matches("start")) start = Integer.parseInt(item.getString());
                else if (item.getFieldName().matches("prev_command")) prevCommand = Integer.parseInt(item.getString());
                else if (item.getFieldName().matches("hidden_appuser_id")) IDAppUser = Long.parseLong(item.getString());
                else {
                    String fieldName = item.getFieldName();

                    Vector values;
                    if (!fieldNameExist(fieldName, paramValues)) values = new Vector();
                    else values = (Vector) paramValues.get(fieldName);
                    
                    value =  item.getString();
                    values.add(value);
                    paramValues.put(item.getFieldName(), values);
                }
            } else { //untuk membaca inputan user yang berupa file dengan memanfaatkan item.getFieldName() dan item.getSize() untuk melihat ukuran. File disimpan secara temporary dengan nama 0_$nama field tabel database$
                if (item.getSize() != 0) {
                    String namaFile = "";
                    if (item.getFieldName().matches("fileFRM_FIELD_GAMBAR_COVER")) namaFile = "GAMBAR_COVER";
                    identitasFile = imageDirectory + "/0_" + namaFile + ".jpg";
                    File uploadedFile = new File(identitasFile);
                    item.write(uploadedFile);
                }
            }
        }
    }

    CtrlAppUser ctrlAppUser = new CtrlAppUser(paramValues); //memanggil control untuk melakukan manipulasi data dengan mengirim Hashtable yang sudah ada nilai inputan user
    ExtendedControlLine ctrLine = new ExtendedControlLine(); //membuat sebuah control line untuk tempat command
    Vector listAppUser = new Vector(1,1); //membuat sebuah vector yang berisikan data yang akan ditampilkan dalam list tabel
    String whereClause = ""; //untuk menyimpan whereClause dalam proses menampilkan data dalam list tabel
    
    /*switch statement */
    iErrCode = ctrlAppUser.action(iCommand, IDAppUser, imageDirectory); //melakukan proses dengan mengirimkan command yang dilakukan, IDAppUser, dan folder directory tempat menyimpa file, dan mengembalikan kode error
    /* end switch*/

    FrmAppUser frmAppUser = ctrlAppUser.getForm(); //mengambil form dari control untuk proses menampilkan tampilan berikutnya

    /*count list All Product*/
    int vectSize = PstAppUser.getCount(whereClause); //mengambil jumlah data yang tersedia
    AppUser appuser = ctrlAppUser.getAppUser(); //mengambil objek appuser yang sedang dimaintenance
    msgString =  ctrlAppUser.getMessage(); //mengambil nilai message utama dalam proses manipulasi data

    
    /*switch list AppUser*/ //proses untuk mendapatkan nilai start dari tabel yang akan ditampilkan
    if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE)&& (IDAppUser == 0))
	start = PstAppUser.findLimitStart(appuser.getOID(), recordToGet, whereClause, orderClause);

    if((iCommand == Command.FIRST || iCommand == Command.PREV )||
        (iCommand == Command.NEXT || iCommand == Command.LAST)){
        start = ctrlAppUser.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */ //mendapatkan list data yang akan ditampilkan
    listAppUser = PstAppUser.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listAppUser.size() < 1 && start > 0){
        if (vectSize - recordToGet > recordToGet)
            start = start - recordToGet;   //go to Command.PREV
        else{
            start = 0 ;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listAppUser = PstAppUser.list(start,recordToGet, whereClause , orderClause);
    }

    //Setting status input apakah melakukan penambahan record baru, edit record lama, atau status pending (konfirmasi delete, cancel delete, atau masih ada error saat save
    if (iCommand == Command.ADD) statusInput = 1;
    else if (iCommand == Command.ASK || iCommand == Command.EDIT) statusInput = 2;
    else if (iCommand == Command.SAVE && frmAppUser.errorSize()>0) statusInput = 9;

%>

<%!
    /*
    Method untuk menampilkan input data dalam bentuk check box
    - bisa memilih lebih dari satu data
    Input:
    - Vector tipeBukus: Data dalam bentuk vector yang berisikan data dari dalam database atau data yang baru disubmit tapi masih ada error
    - int statusInput: status input (1: Add, 2: Edit dan Ask, 9: Save, tapi masih ada error input
    - long IDBuku: untuk menunjuk pada id objek yang sedang ditampilkan dengan detail
    Output:
    - Sebuah check box untuk melakukan input data dalam bentuk check box
    Proses yang tercakup:
    - Menampilkan data dalam check box dari dalam database
    - Memilih data awal yang dicek di antara data yang ditampilkan di dalam check box
        - Kalau penambahan record: isinya kosong
        - Kalau data di-submit tapi masih ada error: tampilkan data dari nilai yang baru diinputkan
        - Kalau menampilkan record untuk diedit: isinya dari data di dalam database
    Opsi perubahan:
    - ---
    */
    public String ctrCheckBoxAppGroup (Vector userGroups, int statusInput, long IDAppUser){
	Vector listAppGroup = new Vector(1,1);
        listAppGroup = PstAppGroup.list(0, 0, "", "");
        ControlCheckBox chkBx=new ControlCheckBox();
	chkBx.setCellSpace("5");
	chkBx.setCellStyle("5");
	chkBx.setWidth(2);
	chkBx.setTableAlign("left");
	chkBx.setCellWidth("3%");
        
        try{
            String fldName = FrmAppUser.fieldNames[FrmAppUser.FRM_USER_GROUP];
            Vector checkValues = new Vector();
            Vector checkCaptions = new Vector();
            if(listAppGroup!=null){
                for(int i=0; i< listAppGroup.size(); i++){
                    AppGroup group = (AppGroup) listAppGroup.get(i);
                    checkValues.add(Long.toString(group.getOID()));
                    checkCaptions.add(" "+group.getGroupName());
                }
            }
            Vector checkeds = new Vector();
            Vector groups = new Vector();
            Vector result = new Vector();
            if (statusInput == 9) {
                if (!userGroups.isEmpty()) result = new Vector(userGroups);
                for (int i = 0; i < result.size(); i++) {
                   UserGroup userGroup = (UserGroup) result.get(i);
                   groups.add(String.valueOf(userGroup.getIdGroup()));
                }
            } else if (statusInput == 2) {
                result = new Vector(SessAppUser.getUserGroup(IDAppUser));
                for (int i = 0; i < result.size(); i++) {
                   AppGroup group = (AppGroup) result.get(i);
                   groups.add(String.valueOf(group.getOID()));
                }
            }
            if(groups!=null){
                checkeds = new Vector(groups);
            }
            chkBx.setTableWidth("100%");
            return chkBx.draw(fldName, checkValues, checkCaptions, checkeds);
        } catch (Exception exc){
            return "Tidak ada group yang diinputkan";
        }
    }
%>

<%@ include file = "../session.jsp"%> 
<%
if(privUsers==false){
%>
<script>
window.alert("Anda Tidak Memiliki Privilege Halaman Ini!");
window.location="index.jsp?page";
</script>
<%
}
%>

<script language="JavaScript">
                function cmdAdd(){
                    document.frmappuser.hidden_appuser_id.value="0";
                    document.frmappuser.command.value="<%=Command.ADD%>";
                    document.frmappuser.prev_command.value="<%=prevCommand%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
                function cmdDelete(IDAppUser){
                    document.frmappuser.hidden_appuser_id.value=IDAppUser;
                    document.frmappuser.command.value="<%=Command.DELETE%>";
                    document.frmappuser.prev_command.value="<%=prevCommand%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
                function cmdSave(){
                    document.frmappuser.command.value="<%=Command.SAVE%>";
                    document.frmappuser.prev_command.value="<%=prevCommand%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }                
                function cmdEdit(IDAppUser){
                    document.frmappuser.hidden_appuser_id.value=IDAppUser;
                    document.frmappuser.command.value="<%=Command.EDIT%>";
                    document.frmappuser.prev_command.value="<%=prevCommand%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
                function cmdListFirst(){
                    document.frmappuser.command.value="<%=Command.FIRST%>";
                    document.frmappuser.prev_command.value="<%=Command.FIRST%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
                function cmdListPrev(){
                    document.frmappuser.command.value="<%=Command.PREV%>";
                    document.frmappuser.prev_command.value="<%=Command.PREV%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
                function cmdListNext(){
                    document.frmappuser.command.value="<%=Command.NEXT%>";
                    document.frmappuser.prev_command.value="<%=Command.NEXT%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
                function cmdListLast(){
                    document.frmappuser.command.value="<%=Command.LAST%>";
                    document.frmappuser.prev_command.value="<%=Command.LAST%>";
                    document.frmappuser.action="index.jsp?page=user/appuser.jsp";
                    document.frmappuser.submit();
                }
</script>
<form role="form" name="frmappuser" method ="post" action="" enctype="multipart/form-data">
<input type="hidden" name="command" value="<%=iCommand%>">
<input type="hidden" name="vectSize" value="<%=vectSize%>">
<input type="hidden" name="start" value="<%=start%>">
<input type="hidden" name="prev_command" value="<%=prevCommand%>">
<input type="hidden" name="hidden_appuser_id" value="<%=IDAppUser%>">  
      <table width="100%" border="0">
        <tr>
          <td><table width="100%" border="0">
            <tr>
              <td bgcolor="#CCCCCC">No</td>
              <td bgcolor="#CCCCCC">Nama User</td>
              <td bgcolor="#CCCCCC">Login ID</td>
              <td bgcolor="#CCCCCC">Group</td>
            </tr>
            <%
            for(int i=0; i<listAppUser.size(); i++){
                AppUser view = (AppUser)listAppUser.get(i);
                Vector listUserGroup = PstUserGroup.list(0, 0, PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID]+" = '"+view.getOID()+"'", "");
            %>
            <tr>
              <td><%=i+start+1%></td>
              <td><%=view.getFullName()%></td>
              <td><a href="javascript:cmdEdit('<%=view.getOID()%>')"><%=view.getLoginId()%></a></td>
              <td>
                  <%
                  for(int j=0; j<listUserGroup.size(); j++){
                      UserGroup ug = (UserGroup)listUserGroup.get(j);
                      AppGroup group = PstAppGroup.fetchExc(ug.getIdGroup());
                  %>
                  <%=group.getGroupName()%>,
                  <%
                  }
                  %>
                  </td>
            </tr>
            <%
            }
            %>
            <tr>
              <td colspan="4"><div style='float:right;'><span class="command">
                    <%
                        int cmd = 0;
                        if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| (iCommand == Command.NEXT || iCommand == Command.LAST))
                            cmd =iCommand;
                        else{
                            if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                cmd = Command.FIRST;
                            else{
                                if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDAppUser == 0))
                                    cmd = PstAppUser.findLimitCommand(start, recordToGet, vectSize);
                                else
                                    cmd = prevCommand;
                            }
                        }
                    %>
                    <%
                        ctrLine.setLocationImg("images");
                        ctrLine.initDefault();
                    %>
                    <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%> </span></div><label>
                <input onclick="javascript:cmdAdd()" type="submit" name="button" id="button" value="Add">
              </label></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td><%=msgString%></td>
        </tr>
        <tr>
          <td>
              <%if((iCommand==Command.ADD)||(iCommand==Command.EDIT)||(frmAppUser.errorSize()>0)){%>
              <table width="100%" border="0">
            <tr>
              <td colspan="2" bgcolor="#CCCCCC">Form Data AppUser</td>
            </tr>
            <tr>
              <td>Login ID</td>
              <td><label>
                <input name="<%=frmAppUser.fieldNames[FrmAppUser.FRM_LOGIN_ID] %>" value="<%=appuser.getLoginId()%>" type="text" id="login"/>
              </label></td>
            </tr>
            <tr>
              <td>Password</td>
              <td><label>
                <input name="<%=frmAppUser.fieldNames[FrmAppUser.FRM_PASSWORD] %>" value="<%=appuser.getPassword()%>" type="password" id="password"/>
              </label></td>
            </tr>
            <tr>
              <td>Confirm Password</td>
              <td><label>
                <input name="<%=frmAppUser.fieldNames[FrmAppUser.FRM_CFRM_PASSWORD] %>" value="<%if(iCommand==Command.EDIT){%><%=appuser.getPassword()%><%}%>" type="password" id="confirmpassword"/>
              </label></td>
            </tr>
            <tr>
              <td>Full Name</td>
              <td><label>
                <input name="<%=frmAppUser.fieldNames[FrmAppUser.FRM_FULL_NAME] %>" value="<%=appuser.getFullName()%>" type="text" id="fullname"/>
              </label></td>
            </tr>
            <tr>
              <td>E-mail</td>
              <td><label>
                <input name="<%=frmAppUser.fieldNames[FrmAppUser.FRM_EMAIL] %>" value="<%=appuser.getEmail()%>" type="text" id="email"/>
              </label></td>
            </tr>
            <tr>
              <td>User Status</td>
              <td><label>
                <select id="userstatus" name="<%= frmAppUser.fieldNames[FrmAppUser.FRM_USER_STATUS]%>" class="input-select">
                    <%
                    String newSelected = "";
                    String loginSelected = "";
                    String logoutSelected = "";
                    if(appuser.getUserStatus()==AppUser.STATUS_NEW) newSelected = "selected";
                    if(appuser.getUserStatus()==AppUser.STATUS_LOGIN) loginSelected = "selected";
                    if(appuser.getUserStatus()==AppUser.STATUS_LOGOUT) logoutSelected = "selected";
                    %>
                    <option value="<%=AppUser.STATUS_NEW%>" <%=newSelected%>>New</option>
                    <option value="<%=AppUser.STATUS_LOGIN%>" <%=loginSelected%>>Login</option>
                    <option value="<%=AppUser.STATUS_LOGOUT%>" <%=logoutSelected%>>Logout</option>
                </select>
              </label></td>
            </tr>
            <tr>
              <td>Group</td>
              <td><label>
                <%=ctrCheckBoxAppGroup(frmAppUser.getUserGroup(IDAppUser), statusInput, IDAppUser)%>
              </label></td>
            </tr>                                                            
            <tr>
              <td colspan="2">
                <input onclick="javascript:cmdSave()" type="submit" name="button2" id="button2" value="Save">
                <%if(IDAppUser!=0){%>
                <input onclick="javascript:cmdDelete('<%=IDAppUser%>')" type="submit" name="button3" id="button3" value="Delete">
              <%}%>
                  </td>
            </tr>
          </table>
          <%}%>
          </td>
        </tr>
      </table>
    </form>
