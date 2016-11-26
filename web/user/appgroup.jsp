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
    String orderClause = PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID];//untuk menyimpan orderClause dalam proses menampilkan data dalam list tabel

    String imageDirectory = this.getServletContext().getRealPath("/") + "images"; //untuk menyimpan string directory tempat menyimpan file image
    long maxRequestSize = 102400; //untuk menyimpan data ukuran file maksimal yang diikutkan dalam proses maintenance data

    int iCommand = 0; //untuk menyimpan kode command yang akan dilakukan setelah proses di-submit
    int start = 0; //untuk menyimpan indeks start di list mana data yang sedang menjadi fokus berada
    int prevCommand = 0; //untuk menyimpan nilai dari command yang sebelumnya dilakukan
    long IDAppGroup = 0; //untuk menyimpan ID objek yang sedang dimaintenance (0: untuk data baru)
    
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
                else if (item.getFieldName().matches("hidden_appgroup_id")) IDAppGroup = Long.parseLong(item.getString());
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

    CtrlAppGroup ctrlAppGroup = new CtrlAppGroup(paramValues); //memanggil control untuk melakukan manipulasi data dengan mengirim Hashtable yang sudah ada nilai inputan user
    ExtendedControlLine ctrLine = new ExtendedControlLine(); //membuat sebuah control line untuk tempat command
    Vector listAppGroup = new Vector(1,1); //membuat sebuah vector yang berisikan data yang akan ditampilkan dalam list tabel
    String whereClause = ""; //untuk menyimpan whereClause dalam proses menampilkan data dalam list tabel
    
    /*switch statement */
    iErrCode = ctrlAppGroup.action(iCommand, IDAppGroup, imageDirectory); //melakukan proses dengan mengirimkan command yang dilakukan, IDAppGroup, dan folder directory tempat menyimpa file, dan mengembalikan kode error
    /* end switch*/

    FrmAppGroup frmAppGroup = ctrlAppGroup.getForm(); //mengambil form dari control untuk proses menampilkan tampilan berikutnya

    /*count list All Product*/
    int vectSize = PstAppGroup.getCount(whereClause); //mengambil jumlah data yang tersedia
    AppGroup appgroup = ctrlAppGroup.getAppGroup(); //mengambil objek appgroup yang sedang dimaintenance
    msgString =  ctrlAppGroup.getMessage(); //mengambil nilai message utama dalam proses manipulasi data

    
    /*switch list AppGroup*/ //proses untuk mendapatkan nilai start dari tabel yang akan ditampilkan
    if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE)&& (IDAppGroup == 0))
	start = PstAppGroup.findLimitStart(appgroup.getOID(), recordToGet, whereClause, orderClause);

    if((iCommand == Command.FIRST || iCommand == Command.PREV )||
        (iCommand == Command.NEXT || iCommand == Command.LAST)){
        start = ctrlAppGroup.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */ //mendapatkan list data yang akan ditampilkan
    listAppGroup = PstAppGroup.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listAppGroup.size() < 1 && start > 0){
        if (vectSize - recordToGet > recordToGet)
            start = start - recordToGet;   //go to Command.PREV
        else{
            start = 0 ;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listAppGroup = PstAppGroup.list(start,recordToGet, whereClause , orderClause);
    }

    //Setting status input apakah melakukan penambahan record baru, edit record lama, atau status pending (konfirmasi delete, cancel delete, atau masih ada error saat save
    if (iCommand == Command.ADD) statusInput = 1;
    else if (iCommand == Command.ASK || iCommand == Command.EDIT) statusInput = 2;
    else if (iCommand == Command.SAVE && frmAppGroup.errorSize()>0) statusInput = 9;

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
    public String ctrCheckBoxAppPriv (Vector userPrivs, int statusInput, long IDAppGroup){
	Vector listAppPriv = new Vector(1,1);
        listAppPriv = PstAppPriv.list(0, 0, "", "");
        ControlCheckBox chkBx=new ControlCheckBox();
	chkBx.setCellSpace("5");
	chkBx.setCellStyle("5");
	chkBx.setWidth(1);
	chkBx.setTableAlign("left");
	chkBx.setCellWidth("3%");
        
        try{
            String fldName = FrmAppGroup.fieldNames[FrmAppGroup.FRM_GROUP_PRIV];
            Vector checkValues = new Vector();
            Vector checkCaptions = new Vector();
            if(listAppPriv!=null){
                for(int i=0; i< listAppPriv.size(); i++){
                    AppPriv priv = (AppPriv) listAppPriv.get(i);
                    checkValues.add(Long.toString(priv.getOID()));
                    checkCaptions.add(" "+priv.getPrivName());
                }
            }
            Vector checkeds = new Vector();
            Vector privs = new Vector();
            Vector result = new Vector();
            if (statusInput == 9) {
                if (!userPrivs.isEmpty()) result = new Vector(userPrivs);
                for (int i = 0; i < result.size(); i++) {
                   GroupPriv userPriv = (GroupPriv) result.get(i);
                   privs.add(String.valueOf(userPriv.getIdGroup()));
                }
            } else if (statusInput == 2) {
                result = new Vector(SessAppGroup.getGroupPriv(IDAppGroup));
                for (int i = 0; i < result.size(); i++) {
                   AppPriv priv = (AppPriv) result.get(i);
                   privs.add(String.valueOf(priv.getOID()));
                }
            }
            if(privs!=null){
                checkeds = new Vector(privs);
            }
            chkBx.setTableWidth("100%");
            return chkBx.draw(fldName, checkValues, checkCaptions, checkeds);
        } catch (Exception exc){
            return "Tidak ada privilege yang diinputkan";
        }
    }
%>

<%@ include file = "../session.jsp"%> 
<%
if(privGroups==false){
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
                    document.frmappgroup.hidden_appgroup_id.value="0";
                    document.frmappgroup.command.value="<%=Command.ADD%>";
                    document.frmappgroup.prev_command.value="<%=prevCommand%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
                function cmdDelete(IDAppGroup){
                    document.frmappgroup.hidden_appgroup_id.value=IDAppGroup;
                    document.frmappgroup.command.value="<%=Command.DELETE%>";
                    document.frmappgroup.prev_command.value="<%=prevCommand%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
                function cmdSave(){
                    document.frmappgroup.command.value="<%=Command.SAVE%>";
                    document.frmappgroup.prev_command.value="<%=prevCommand%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }                
                function cmdEdit(IDAppGroup){
                    document.frmappgroup.hidden_appgroup_id.value=IDAppGroup;
                    document.frmappgroup.command.value="<%=Command.EDIT%>";
                    document.frmappgroup.prev_command.value="<%=prevCommand%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
                function cmdListFirst(){
                    document.frmappgroup.command.value="<%=Command.FIRST%>";
                    document.frmappgroup.prev_command.value="<%=Command.FIRST%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
                function cmdListPrev(){
                    document.frmappgroup.command.value="<%=Command.PREV%>";
                    document.frmappgroup.prev_command.value="<%=Command.PREV%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
                function cmdListNext(){
                    document.frmappgroup.command.value="<%=Command.NEXT%>";
                    document.frmappgroup.prev_command.value="<%=Command.NEXT%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
                function cmdListLast(){
                    document.frmappgroup.command.value="<%=Command.LAST%>";
                    document.frmappgroup.prev_command.value="<%=Command.LAST%>";
                    document.frmappgroup.action="index.jsp?page=user/appgroup.jsp";
                    document.frmappgroup.submit();
                }
</script>
<form role="form" name="frmappgroup" method ="post" action="" enctype="multipart/form-data">
<input type="hidden" name="command" value="<%=iCommand%>">
<input type="hidden" name="vectSize" value="<%=vectSize%>">
<input type="hidden" name="start" value="<%=start%>">
<input type="hidden" name="prev_command" value="<%=prevCommand%>">
<input type="hidden" name="hidden_appgroup_id" value="<%=IDAppGroup%>">  
      <table width="100%" border="0">
        <tr>
          <td><table width="100%" border="0">
            <tr>
              <td bgcolor="#CCCCCC">No</td>
              <td bgcolor="#CCCCCC">Nama Group</td>
              <td bgcolor="#CCCCCC">Privilege</td>
            </tr>
            <%
            for(int i=0; i<listAppGroup.size(); i++){
                AppGroup view = (AppGroup)listAppGroup.get(i);
                Vector listGroupPriv = PstGroupPriv.list(0, 0, PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]+" = '"+view.getOID()+"'", "");
            %>
            <tr>
              <td><%=i+start+1%></td>
              <td><a href="javascript:cmdEdit('<%=view.getOID()%>')"><%=view.getGroupName()%></a></td>
              <td>
                  <%
                  for(int j=0; j<listGroupPriv.size(); j++){
                      GroupPriv gp = (GroupPriv)listGroupPriv.get(j);
                      AppPriv priv = PstAppPriv.fetchExc(gp.getIdPriv());
                  %>
                  <%=priv.getPrivName()%>,
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
                                if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDAppGroup == 0))
                                    cmd = PstAppGroup.findLimitCommand(start, recordToGet, vectSize);
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
              <%if((iCommand==Command.ADD)||(iCommand==Command.EDIT)||(frmAppGroup.errorSize()>0)){%>
              <table width="100%" border="0">
            <tr>
              <td colspan="2" bgcolor="#CCCCCC">Form Data AppGroup</td>
            </tr>
            <tr>
              <td>Nama Group</td>
              <td><label>
                <input type="text" name="<%=frmAppGroup.fieldNames[FrmAppGroup.FRM_GROUP_NAME]%>" id="textfield" value="<%=appgroup.getGroupName()%>"/>
              </label></td>
            </tr>
            <tr>
              <td>Deskripsi</td>
              <td><label>
                <input type="text" name="<%=frmAppGroup.fieldNames[FrmAppGroup.FRM_DESCRIPTION]%>" id="textfield2" value="<%=appgroup.getDescription()%>"/>
              </label></td>
            </tr>
            <tr>
              <td>Privilege</td>
              <td><label>
                <%=ctrCheckBoxAppPriv(frmAppGroup.getGroupPriv(IDAppGroup), statusInput, IDAppGroup)%>
              </label></td>
            </tr>                                                            
            <tr>
              <td colspan="2">
                <input onclick="javascript:cmdSave()" type="submit" name="button2" id="button2" value="Save">
                <%if(IDAppGroup!=0){%>
                <input onclick="javascript:cmdDelete('<%=IDAppGroup%>')" type="submit" name="button3" id="button3" value="Delete">
              <%}%>
                  </td>
            </tr>
          </table>
          <%}%>
          </td>
        </tr>
      </table>
    </form>
