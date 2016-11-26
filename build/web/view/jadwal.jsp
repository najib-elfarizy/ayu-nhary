<%@page import="java.text.SimpleDateFormat"%>
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
    int recordToGet = 0; //jumlah record maksimal yang ditampilkan dalam list tabel data
    int statusInput = 0;
    
    int iErrCode = ExtendedFRMMessage.NONE; //untuk menyimpan kode error yang muncul selama proses maintenance data
    String msgString = ""; //untuk menyimpan pesan error utama yang muncul selama proses maintenance data
    String orderClause = PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL];//untuk menyimpan orderClause dalam proses menampilkan data dalam list tabel

    String imageDirectory = this.getServletContext().getRealPath("/") + "images"; //untuk menyimpan string directory tempat menyimpan file image
    long maxRequestSize = 102400; //untuk menyimpan data ukuran file maksimal yang diikutkan dalam proses maintenance data

    int iCommand = 0; //untuk menyimpan kode command yang akan dilakukan setelah proses di-submit
    int start = 0; //untuk menyimpan indeks start di list mana data yang sedang menjadi fokus berada
    int prevCommand = 0; //untuk menyimpan nilai dari command yang sebelumnya dilakukan
    long IDDetailJadwal = 0; //untuk menyimpan ID objek yang sedang dimaintenance (0: untuk data baru)
    
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
                else if (item.getFieldName().matches("hidden_detailjadwal_id")) IDDetailJadwal = Long.parseLong(item.getString());
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

    CtrlDetailJadwal ctrlDetailJadwal = new CtrlDetailJadwal(paramValues); //memanggil control untuk melakukan manipulasi data dengan mengirim Hashtable yang sudah ada nilai inputan user
    ExtendedControlLine ctrLine = new ExtendedControlLine(); //membuat sebuah control line untuk tempat command
    Vector listDetailJadwal = new Vector(1,1); //membuat sebuah vector yang berisikan data yang akan ditampilkan dalam list tabel
    Jadwal confirm = (Jadwal)(PstJadwal.list(0, 1, PstJadwal.fieldNames[PstJadwal.FLD_STATUS]+" = '"+Jadwal.STATUS_CONFIRM+"'", "")).get(0);
    String whereClause = PstDetailJadwal.fieldNames[PstDetailJadwal.FLD_ID_JADWAL]+" = '"+confirm.getOID()+"'"; //untuk menyimpan whereClause dalam proses menampilkan data dalam list tabel
    
    /*switch statement */
    iErrCode = ctrlDetailJadwal.action(iCommand, IDDetailJadwal, imageDirectory); //melakukan proses dengan mengirimkan command yang dilakukan, IDDetailJadwal, dan folder directory tempat menyimpa file, dan mengembalikan kode error
    /* end switch*/

    FrmDetailJadwal frmDetailJadwal = ctrlDetailJadwal.getForm(); //mengambil form dari control untuk proses menampilkan tampilan berikutnya

    /*count list All Product*/
    int vectSize = 0; //mengambil jumlah data yang tersedia
    DetailJadwal detailjadwal = ctrlDetailJadwal.getDetailJadwal(); //mengambil objek detailjadwal yang sedang dimaintenance
    msgString =  ctrlDetailJadwal.getMessage(); //mengambil nilai message utama dalam proses manipulasi data

    Jadwal selectJadwal = PstJadwal.fetchExc(detailjadwal.getIdJadwal());
    Proyek selectProyek = PstProyek.fetchExc(detailjadwal.getIdProyek());
    Karyawan selectKaryawan = PstKaryawan.fetchExc(detailjadwal.getNIK());
    
    /*switch list DetailJadwal*/ //proses untuk mendapatkan nilai start dari tabel yang akan ditampilkan
    if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE)&& (IDDetailJadwal == 0))
	start = PstDetailJadwal.findLimitStart(detailjadwal.getOID(), recordToGet, whereClause, orderClause);

    if((iCommand == Command.FIRST || iCommand == Command.PREV )||
        (iCommand == Command.NEXT || iCommand == Command.LAST)){
        start = ctrlDetailJadwal.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */ //mendapatkan list data yang akan ditampilkan
    listDetailJadwal = PstDetailJadwal.list(start, recordToGet, whereClause, orderClause);
    Vector listJadwal = PstJadwal.listAll();
    Vector listProyek = PstProyek.listAll();
    Vector listKaryawan = PstKaryawan.listAll();

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listDetailJadwal.size() < 1 && start > 0){
        if (vectSize - recordToGet > recordToGet)
            start = start - recordToGet;   //go to Command.PREV
        else{
            start = 0 ;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listDetailJadwal = PstDetailJadwal.list(start,recordToGet, whereClause , orderClause);
    }

    //Setting status input apakah melakukan penambahan record baru, edit record lama, atau status pending (konfirmasi delete, cancel delete, atau masih ada error saat save
    if (iCommand == Command.ADD) statusInput = 1;
    else if (iCommand == Command.ASK || iCommand == Command.EDIT) statusInput = 2;
    else if (iCommand == Command.SAVE && frmDetailJadwal.errorSize()>0) statusInput = 9;

%>
<%
    String[] keys = new String[]{"09.00-13.00 ", "13.00-17.00 "};
    String[] vals = new String[]{"0","1"};                      
%>


<script language="JavaScript">
                
                function cmdListFirst(){
                    document.frmdetailjadwal.command.value="<%=Command.FIRST%>";
                    document.frmdetailjadwal.prev_command.value="<%=Command.FIRST%>";
                    document.frmdetailjadwal.action="index.jsp?page=master/detailjadwal.jsp";
                    document.frmdetailjadwal.submit();
                }
                function cmdListPrev(){
                    document.frmdetailjadwal.command.value="<%=Command.PREV%>";
                    document.frmdetailjadwal.prev_command.value="<%=Command.PREV%>";
                    document.frmdetailjadwal.action="index.jsp?page=master/detailjadwal.jsp";
                    document.frmdetailjadwal.submit();
                }
                function cmdListNext(){
                    document.frmdetailjadwal.command.value="<%=Command.NEXT%>";
                    document.frmdetailjadwal.prev_command.value="<%=Command.NEXT%>";
                    document.frmdetailjadwal.action="index.jsp?page=master/detailjadwal.jsp";
                    document.frmdetailjadwal.submit();
                }
                function cmdListLast(){
                    document.frmdetailjadwal.command.value="<%=Command.LAST%>";
                    document.frmdetailjadwal.prev_command.value="<%=Command.LAST%>";
                    document.frmdetailjadwal.action="index.jsp?page=master/detailjadwal.jsp";
                    document.frmdetailjadwal.submit();
                }
</script>
<form role="form" name="frmdetailjadwal" method ="post" action="" enctype="multipart/form-data">
<input type="hidden" name="command" value="<%=iCommand%>">
<input type="hidden" name="vectSize" value="<%=vectSize%>">
<input type="hidden" name="start" value="<%=start%>">
<input type="hidden" name="prev_command" value="<%=prevCommand%>">
<input type="hidden" name="hidden_detailjadwal_id" value="<%=IDDetailJadwal%>">  
      <table width="100%" border="0">
        <tr>
          <td><table width="100%" border="0">
            <tr>
              <td width="5%" bgcolor="#CCCCCC">No</td>
              <td width="30%" bgcolor="#CCCCCC">Proyek</td>
              <td width="30%" bgcolor="#CCCCCC">Karyawan</td>
              <td bgcolor="#CCCCCC">Waktu Mulai</td>
              <td bgcolor="#CCCCCC">Waktu Selesai</td>
            </tr>
            <%
            for(int i=0; i<listDetailJadwal.size(); i++){
                DetailJadwal view = (DetailJadwal)listDetailJadwal.get(i);
                Proyek proyek = PstProyek.fetchExc(view.getIdProyek());
                Karyawan karyawan = PstKaryawan.fetchExc(view.getNIK());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy '/' HH:mm");
                Date waktuMulai = proyek.getWaktuMulai();
                Date waktuSelesai = proyek.getWaktuSelesai();
            %>
            <tr>
              <td><%=i+start+1%></td>
              <td><%=proyek.getNamaProyek()%></td>
              <td><%=karyawan.getNamaKaryawan()%></td>
              <td><%=dateFormat.format(waktuMulai)%></td>
              <td><%=dateFormat.format(waktuSelesai)%></td>
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
                                if((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDDetailJadwal == 0))
                                    cmd = PstDetailJadwal.findLimitCommand(start, recordToGet, vectSize);
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
                
              </label></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td></td>
        </tr>
        <tr>
          <td>
          </td>
        </tr>
      </table>
    </form>
