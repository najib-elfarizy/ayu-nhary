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
    private String choosePicture(int statusInput, String directoryName, String defaultFilename, String tempFilename, String filename) {
        String realFilename = "";
        File file1 = new File(directoryName + "/" + tempFilename);
        if (file1.exists()) {
            realFilename = tempFilename;
        } else {
            if (!filename.matches("")) {
                File file2 = new File(directoryName + "/" + filename);
                if (file2.exists()) {
                    realFilename = filename;
                }
            } else {
                realFilename = defaultFilename;
            }
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
            if (K.nextElement().equals(a)) {
                return true;
            }
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
    String orderClause = PstPegawai.fieldNames[PstPegawai.FLD_NIP];//untuk menyimpan orderClause dalam proses menampilkan data dalam list tabel

    String imageDirectory = this.getServletContext().getRealPath("/") + "images"; //untuk menyimpan string directory tempat menyimpan file image
    long maxRequestSize = 102400; //untuk menyimpan data ukuran file maksimal yang diikutkan dalam proses maintenance data

    int iCommand = 0; //untuk menyimpan kode command yang akan dilakukan setelah proses di-submit
    int start = 0; //untuk menyimpan indeks start di list mana data yang sedang menjadi fokus berada
    int prevCommand = 0; //untuk menyimpan nilai dari command yang sebelumnya dilakukan
    long IDPegawai = 0; //untuk menyimpan ID objek yang sedang dimaintenance (0: untuk data baru)

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
                if (item.getFieldName().matches("command")) {
                    iCommand = Integer.parseInt(item.getString());
                } else if (item.getFieldName().matches("vectSize")) {
                    start = Integer.parseInt(item.getString());
                } else if (item.getFieldName().matches("start")) {
                    start = Integer.parseInt(item.getString());
                } else if (item.getFieldName().matches("prev_command")) {
                    prevCommand = Integer.parseInt(item.getString());
                } else if (item.getFieldName().matches("hidden_pegawai_id")) {
                    IDPegawai = Long.parseLong(item.getString());
                } else {
                    String fieldName = item.getFieldName();

                    Vector values;
                    if (!fieldNameExist(fieldName, paramValues)) {
                        values = new Vector();
                    } else {
                        values = (Vector) paramValues.get(fieldName);
                    }

                    value = item.getString();
                    values.add(value);
                    paramValues.put(item.getFieldName(), values);
                }
            } else { //untuk membaca inputan user yang berupa file dengan memanfaatkan item.getFieldName() dan item.getSize() untuk melihat ukuran. File disimpan secara temporary dengan nama 0_$nama field tabel database$
                if (item.getSize() != 0) {
                    String namaFile = "";
                    if (item.getFieldName().matches("fileFRM_FIELD_GAMBAR_COVER")) {
                        namaFile = "GAMBAR_COVER";
                    }
                    identitasFile = imageDirectory + "/0_" + namaFile + ".jpg";
                    File uploadedFile = new File(identitasFile);
                    item.write(uploadedFile);
                }
            }
        }
    }

    CtrlPegawai ctrlPegawai = new CtrlPegawai(paramValues); //memanggil control untuk melakukan manipulasi data dengan mengirim Hashtable yang sudah ada nilai inputan user
    ExtendedControlLine ctrLine = new ExtendedControlLine(); //membuat sebuah control line untuk tempat command
    BootstrapPaginator paginator = new BootstrapPaginator(); //membuat sebuah control paginator untuk tempat command
    Vector listPegawai = new Vector(1, 1); //membuat sebuah vector yang berisikan data yang akan ditampilkan dalam list tabel
    String whereClause = ""; //untuk menyimpan whereClause dalam proses menampilkan data dalam list tabel

    /*switch statement */
    iErrCode = ctrlPegawai.action(iCommand, IDPegawai, imageDirectory); //melakukan proses dengan mengirimkan command yang dilakukan, IDPegawai, dan folder directory tempat menyimpa file, dan mengembalikan kode error
    /* end switch*/

    FrmPegawai frmPegawai = ctrlPegawai.getForm(); //mengambil form dari control untuk proses menampilkan tampilan berikutnya

    /*count list All Product*/
    int vectSize = PstPegawai.getCount(whereClause); //mengambil jumlah data yang tersedia
    Pegawai pegawai = ctrlPegawai.getPegawai(); //mengambil objek pegawai yang sedang dimaintenance
    msgString = ctrlPegawai.getMessage(); //mengambil nilai message utama dalam proses manipulasi data


    /*switch list Pegawai*/ //proses untuk mendapatkan nilai start dari tabel yang akan ditampilkan
    if ((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDPegawai == 0)) {
        start = PstPegawai.findLimitStart(pegawai.getOID(), recordToGet, whereClause, orderClause);
    }

    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlPegawai.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */ //mendapatkan list data yang akan ditampilkan
    listPegawai = PstPegawai.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listPegawai.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listPegawai = PstPegawai.list(start, recordToGet, whereClause, orderClause);
    }

    //Setting status input apakah melakukan penambahan record baru, edit record lama, atau status pending (konfirmasi delete, cancel delete, atau masih ada error saat save
    if (iCommand == Command.ADD) {
        statusInput = 1;
    } else if (iCommand == Command.ASK || iCommand == Command.EDIT) {
        statusInput = 2;
    } else if (iCommand == Command.SAVE && frmPegawai.errorSize() > 0) {
        statusInput = 9;
    }

%>

<%@ include file = "../session.jsp"%> 
<%
    if (privMaintenanceDataPegawai == false) {
%>
<script>
    window.alert("Anda Tidak Memiliki Privilege Halaman Ini!");
    window.location = "index.jsp?page";
</script>
<%    }
%>

<form class="form-horizontal" role="form" id="form-pegawai" method ="post" action="" enctype="multipart/form-data">
    <input type="hidden" name="command" value="<%=iCommand%>">
    <input type="hidden" name="vectSize" value="<%=vectSize%>">
    <input type="hidden" name="start" value="<%=start%>">
    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
    <input type="hidden" name="hidden_pegawai_id" value="<%=IDPegawai%>">
    <%if ((iCommand == Command.ADD) || (iCommand == Command.EDIT) || (frmPegawai.errorSize() > 0)) {%>
    <div class="panel panel-default">
        <%if (IDPegawai != 0) {%>
        <div class="panel-heading">Edit Pegawai</div>
        <% } else { %>
        <div class="panel-heading">Pegawai Baru</div>
        <% } %>
        <div class="panel-body">
            <div class="form-group has-error text-center">
                <span class="help-block"><%=msgString%></span>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="code">Nama Pegawai</label>  
                <div class="col-md-7">
                    <input name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_NAMA]%>" type="text" placeholder="Nama Pegawai" value="<%=pegawai.getNamaPegawai()%>" class="form-control" autocomplete="off" required="">
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="code">Jenis Kelamin</label>  
                <div class="col-md-3">
                    <select class="form-control" name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_JENIS_KELAMIN]%>">
                        <option value="L">Laki-Laki</option>
                        <% if("P".equals(pegawai.getJenisKelamin())) { %>
                            <option selected value="P">Perempuan</option
                        <% } else { %>>
                            <option value="P">Perempuan</option
                        <% } %>>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="value">Tanggal Lahir</label>  
                <div class="col-md-7">
                    <input type="hidden" name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_TEMPAT_LAHIR]%>" value="<%=pegawai.getTempatLahir()%>">
                    <%=ControlDate.drawDate(frmPegawai.fieldNames[FrmPegawai.FRM_TANGGAL_LAHIR], pegawai.getTanggalLahir() == null ? new Date() : pegawai.getTanggalLahir(), "formElemen", 0, -40)%>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="value">Alamat</label>  
                <div class="col-md-7">
                    <input type="text" name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_ALAMAT]%>" placeholder="Alamat Pegawai" value="<%=pegawai.getAlamat()%>" class="form-control" autocomplete="off">
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="value">Pendidikan</label>  
                <div class="col-md-7">
                    <input type="text" name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_PENDIDIKAN]%>" placeholder="Pendidikan Pegawai" value="<%=pegawai.getPendidikan()%>" class="form-control" autocomplete="off">
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="value">Email</label>  
                <div class="col-md-7">
                    <input type="text" name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_EMAIL]%>" placeholder="Email Pegawai" value="<%=pegawai.getEmail()%>" class="form-control" autocomplete="off">
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="value">Jabatan</label>  
                <div class="col-md-7">
                    <input type="text" name="<%=frmPegawai.fieldNames[FrmPegawai.FRM_JABATAN]%>" placeholder="Jabatan Pegawai" value="<%=pegawai.getJabatan()%>" class="form-control" autocomplete="off">
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-3"></div>
                <div class="col-md-7">
                    <button type="button" class="btn btn-primary btn-save">Simpan</button>
                    <button type="button" class="btn btn-default btn-cancel">Cancel</button>
                </div>
            </div>
        </div>
    </div>
    <% } %>
</form>

<div class="panel panel-default panel-table">
    <div class="panel-heading">
        <div class="row">
            <div class="col col-xs-6">
                <h3 class="panel-title">Data Pegawai</h3>
            </div>
            <div class="col col-xs-6 text-right">
                <button type="button" class="btn btn-sm btn-primary btn-add">Add New</button>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-striped table-bordered table-list">
            <thead>
                <tr>
                    <th>No</th>
                    <th>NIP</th>
                    <th>Nama</th>
                    <th>Alamat</th>
                    <th>Email</th>
                    <th>Pendidikan</th>
                    <th>Jabatan</th>
                    <th>
                        <em class="glyphicon glyphicon-cog"></em>
                    </th>
                </tr> 
            </thead>
            <tbody>
                <%
                    for (int i = 0; i < listPegawai.size(); i++) {
                        Pegawai view = (Pegawai) listPegawai.get(i);
                %>
                <tr>
                    <td align="center"><%=i + start + 1%></td>
                    <td><%=view.getNip()%></td>
                    <td><%=view.getNamaPegawai()%></td>
                    <td><%=view.getAlamat()%></td>
                    <td><%=view.getEmail()%></td>
                    <td><%=view.getPendidikan()%></td>
                    <td><%=view.getJabatan()%></td>
                    <td align="center">
                        <a href="javascript:cmdEdit('<%=view.getOID()%>')" title="Edit <%=view.getOID()%>" class="btn btn-sm btn-default">
                            <em class="glyphicon glyphicon-pencil"></em>
                        </a>
                        <button value="<%=view.getOID()%>" title="Hapus <%=view.getOID()%>" class="btn btn-sm btn-danger btn-delete">
                            <em class="glyphicon glyphicon-trash"></em>
                        </button>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

    </div>
    <div class="panel-footer">
        <div class="row">
            <div class="col col-xs-4">
                <%
                    int cmd = 0;
                    if ((iCommand == Command.FIRST || iCommand == Command.PREV) || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                        cmd = iCommand;
                    } else {
                        if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                            cmd = Command.FIRST;
                        } else {
                            if ((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDPegawai == 0)) {
                                cmd = PstAppConfig.findLimitCommand(start, recordToGet, vectSize);
                            } else {
                                cmd = prevCommand;
                            }
                        }
                    }
                %>
                <%= paginator.render(cmd, vectSize, start, recordToGet)%>
            </div>
            <div class="col col-xs-8">
                <div class="pull-right">
                    <%= paginator.getPagingInfo() %>
                </div>
            </div>
        </div>
    </div>
</div>
                
<script language="JavaScript">
    $('.btn-delete').click(function() {
        var IDAppConfig = $(this).val();
        bootbox.confirm("Hapus " + IDAppConfig + "?", function(result) {
            if (result) {
                cmdDelete(IDAppConfig);
            }
        });
    });
    $('.btn-add').click(function() {
        cmdAdd();
    });
    $('.btn-cancel').click(function() {
        $('#form-pegawai').find('input[name=hidden_pegawai_id]').val("0");
        $('#form-pegawai').find('input[name=command]').val("<%=Command.NONE%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    });
    $('.btn-save').click(function() {
        cmdSave();
    });

    function cmdAdd() {
        $('#form-pegawai').find('input[name=hidden_pegawai_id]').val("0");
        $('#form-pegawai').find('input[name=command]').val("<%=Command.ADD%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdDelete(IDAppConfig) {
        $('#form-pegawai').find('input[name=hidden_pegawai_id]').val(IDAppConfig);
        $('#form-pegawai').find('input[name=command]').val("<%=Command.DELETE%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdSave() {
        $('#form-pegawai').find('input[name=command]').val("<%=Command.SAVE%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdEdit(IDAppConfig) {
        $('#form-pegawai').find('input[name=hidden_pegawai_id]').val(IDAppConfig);
        $('#form-pegawai').find('input[name=command]').val("<%=Command.EDIT%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdListFirst() {
        $('#form-pegawai').find('input[name=command]').val("<%=Command.FIRST%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=Command.FIRST%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdListPrev() {
        $('#form-pegawai').find('input[name=command]').val("<%=Command.PREV%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=Command.PREV%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdListNext() {
        $('#form-pegawai').find('input[name=command]').val("<%=Command.NEXT%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=Command.NEXT%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
    function cmdListLast() {
        $('#form-pegawai').find('input[name=command]').val("<%=Command.LAST%>");
        $('#form-pegawai').find('input[name=prev_command]').val("<%=Command.LAST%>");
        $('#form-pegawai').attr('action', "index.jsp?page=master/pegawai.jsp");
        $('#form-pegawai').submit();
    }
</script>
