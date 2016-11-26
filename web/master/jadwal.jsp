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

<%@ page import = "com.master.entity.*" %>
<%@ page import = "com.master.form.*" %>

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
    int recordToGet = 10; //jumlah record maksimal yang ditampilkan dalam list tabel data
    int statusInput = 0;
    
    int iErrCode = ExtendedFRMMessage.NONE; //untuk menyimpan kode error yang muncul selama proses maintenance data
    String msgString = ""; //untuk menyimpan pesan error utama yang muncul selama proses maintenance data
    String orderClause = PstJadwal.fieldNames[PstJadwal.FLD_ID_JADWAL];//untuk menyimpan orderClause dalam proses menampilkan data dalam list tabel

    String imageDirectory = this.getServletContext().getRealPath("/") + "images"; //untuk menyimpan string directory tempat menyimpan file image
    long maxRequestSize = 102400; //untuk menyimpan data ukuran file maksimal yang diikutkan dalam proses maintenance data

    int iCommand = 0; //untuk menyimpan kode command yang akan dilakukan setelah proses di-submit
    int start = 0; //untuk menyimpan indeks start di list mana data yang sedang menjadi fokus berada
    int prevCommand = 0; //untuk menyimpan nilai dari command yang sebelumnya dilakukan
    long IDJadwal = 0; //untuk menyimpan ID objek yang sedang dimaintenance (0: untuk data baru)
    
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
                else if (item.getFieldName().matches("hidden_jadwal_id")) IDJadwal = Long.parseLong(item.getString());
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

    CtrlJadwal ctrlJadwal = new CtrlJadwal(paramValues); //memanggil control untuk melakukan manipulasi data dengan mengirim Hashtable yang sudah ada nilai inputan user
    ExtendedControlLine ctrLine = new ExtendedControlLine(); //membuat sebuah control line untuk tempat command
    Vector listJadwal = new Vector(1,1); //membuat sebuah vector yang berisikan data yang akan ditampilkan dalam list tabel
    String whereClause = PstJadwal.fieldNames[PstJadwal.FLD_STATUS]+ " = '"+Jadwal.STATUS_NEW+"'"; //untuk menyimpan whereClause dalam proses menampilkan data dalam list tabel
    
    /*switch statement */
    iErrCode = ctrlJadwal.action(iCommand, IDJadwal, imageDirectory); //melakukan proses dengan mengirimkan command yang dilakukan, IDJadwal, dan folder directory tempat menyimpa file, dan mengembalikan kode error
    /* end switch*/

    FrmJadwal frmJadwal = ctrlJadwal.getForm(); //mengambil form dari control untuk proses menampilkan tampilan berikutnya

    /*count list All Product*/
    int vectSize = PstJadwal.getCount(whereClause); //mengambil jumlah data yang tersedia
    Jadwal jadwal = ctrlJadwal.getJadwal(); //mengambil objek jadwal yang sedang dimaintenance
    msgString =  ctrlJadwal.getMessage(); //mengambil nilai message utama dalam proses manipulasi data

    
    /*switch list Jadwal*/ //proses untuk mendapatkan nilai start dari tabel yang akan ditampilkan
    if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE)&& (IDJadwal == 0))
	start = PstJadwal.findLimitStart(jadwal.getOID(), recordToGet, whereClause, orderClause);

    if((iCommand == Command.FIRST || iCommand == Command.PREV )||
        (iCommand == Command.NEXT || iCommand == Command.LAST)){
        start = ctrlJadwal.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */ //mendapatkan list data yang akan ditampilkan
    listJadwal = PstJadwal.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listJadwal.size() < 1 && start > 0){
        if (vectSize - recordToGet > recordToGet)
            start = start - recordToGet;   //go to Command.PREV
        else{
            start = 0 ;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listJadwal = PstJadwal.list(start,recordToGet, whereClause , orderClause);
    }

    //Setting status input apakah melakukan penambahan record baru, edit record lama, atau status pending (konfirmasi delete, cancel delete, atau masih ada error saat save
    if (iCommand == Command.ADD) statusInput = 1;
    else if (iCommand == Command.ASK || iCommand == Command.EDIT) statusInput = 2;
    else if (iCommand == Command.SAVE && frmJadwal.errorSize()>0) statusInput = 9;

%>

<%@ include file = "../session.jsp"%> 
<%
if(privJadwal==false){
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
                    document.frmjadwal.hidden_jadwal_id.value="0";
                    document.frmjadwal.command.value="<%=Command.ADD%>";
                    document.frmjadwal.prev_command.value="<%=prevCommand%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
                function cmdDelete(IDJadwal){
                    document.frmjadwal.hidden_jadwal_id.value=IDJadwal;
                    document.frmjadwal.command.value="<%=Command.DELETE%>";
                    document.frmjadwal.prev_command.value="<%=prevCommand%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
                function cmdSave(){
                    document.frmjadwal.command.value="<%=Command.SAVE%>";
                    document.frmjadwal.prev_command.value="<%=prevCommand%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }                
                function cmdEdit(IDJadwal){
                    document.frmjadwal.hidden_jadwal_id.value=IDJadwal;
                    document.frmjadwal.command.value="<%=Command.EDIT%>";
                    document.frmjadwal.prev_command.value="<%=prevCommand%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
                function cmdListFirst(){
                    document.frmjadwal.command.value="<%=Command.FIRST%>";
                    document.frmjadwal.prev_command.value="<%=Command.FIRST%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
                function cmdListPrev(){
                    document.frmjadwal.command.value="<%=Command.PREV%>";
                    document.frmjadwal.prev_command.value="<%=Command.PREV%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
                function cmdListNext(){
                    document.frmjadwal.command.value="<%=Command.NEXT%>";
                    document.frmjadwal.prev_command.value="<%=Command.NEXT%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
                function cmdListLast(){
                    document.frmjadwal.command.value="<%=Command.LAST%>";
                    document.frmjadwal.prev_command.value="<%=Command.LAST%>";
                    document.frmjadwal.action="index.jsp?page=master/jadwal.jsp";
                    document.frmjadwal.submit();
                }
</script>
<form role="form" name="frmjadwal" method ="post" action="" enctype="multipart/form-data">
<input type="hidden" name="command" value="<%=iCommand%>">
<input type="hidden" name="vectSize" value="<%=vectSize%>">
<input type="hidden" name="start" value="<%=start%>">
<input type="hidden" name="prev_command" value="<%=prevCommand%>">
<input type="hidden" name="hidden_jadwal_id" value="<%=IDJadwal%>">  
      <table width="100%" border="0">
        <tr>
          <td><table width="100%" border="0">
            <tr>
              <td width="30%" bgcolor="#CCCCCC">Kode Jadwal</td>
              <td bgcolor="#CCCCCC">Nama Jadwal</td>
            </tr>
            <%
            for(int i=0; i<listJadwal.size(); i++){
                Jadwal view = (Jadwal)listJadwal.get(i);
            %>
            <tr>
              <td><a href="javascript:cmdEdit('<%=view.getOID()%>')"><%=view.getOID()%></a></td>
              <td><%=view.getNamaJadwal()%></td>
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
                                if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDJadwal == 0))
                                    cmd = PstJadwal.findLimitCommand(start, recordToGet, vectSize);
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
              <%if((iCommand==Command.ADD)||(iCommand==Command.EDIT)||(frmJadwal.errorSize()>0)){%>
              <table width="100%" border="0">
            <tr>
              <td colspan="2" bgcolor="#CCCCCC">Form Data Jadwal</td>
            </tr>
            <tr>
              <td width="30%">Nama Jadwal</td>
              <td><label>
                <input type="text" name="<%=frmJadwal.fieldNames[FrmJadwal.FRM_NAMA_KEAHLIAN]%>" id="textfield" value="<%=jadwal.getNamaJadwal()%>">
              </label></td>
            </tr>
            <tr>
              <td colspan="2">
                <input onclick="javascript:cmdSave()" type="submit" name="button2" id="button2" value="Save">
                <%if(IDJadwal!=0){%>
                <input onclick="javascript:cmdDelete('<%=IDJadwal%>')" type="submit" name="button3" id="button3" value="Delete">
              <%}%>
                  </td>
            </tr>
          </table>
          <%}%>
          </td>
        </tr>
      </table>
    </form>
