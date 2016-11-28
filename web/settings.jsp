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
    String orderClause = PstAppConfig.fieldNames[PstAppConfig.FLD_CONFIG_ID];//untuk menyimpan orderClause dalam proses menampilkan data dalam list tabel

    String imageDirectory = this.getServletContext().getRealPath("/") + "images"; //untuk menyimpan string directory tempat menyimpan file image
    long maxRequestSize = 102400; //untuk menyimpan data ukuran file maksimal yang diikutkan dalam proses maintenance data

    int iCommand = 0; //untuk menyimpan kode command yang akan dilakukan setelah proses di-submit
    int start = 0; //untuk menyimpan indeks start di list mana data yang sedang menjadi fokus berada
    int prevCommand = 0; //untuk menyimpan nilai dari command yang sebelumnya dilakukan
    long IDAppConfig = 0; //untuk menyimpan ID objek yang sedang dimaintenance (0: untuk data baru)

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
                } else if (item.getFieldName().matches("hidden_appconfig_id")) {
                    IDAppConfig = Long.parseLong(item.getString());
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
            }
        }
    }

    CtrlAppConfig ctrlAppConfig = new CtrlAppConfig(paramValues); //memanggil control untuk melakukan manipulasi data dengan mengirim Hashtable yang sudah ada nilai inputan user
    BootstrapPaginator paginator = new BootstrapPaginator(); //membuat sebuah control paginator untuk tempat command
    Vector listAppConfig = new Vector(1, 1); //membuat sebuah vector yang berisikan data yang akan ditampilkan dalam list tabel
    String whereClause = ""; //untuk menyimpan whereClause dalam proses menampilkan data dalam list tabel

    /*switch statement */
    iErrCode = ctrlAppConfig.action(iCommand, IDAppConfig); //melakukan proses dengan mengirimkan command yang dilakukan, IDAppConfig, dan folder directory tempat menyimpa file, dan mengembalikan kode error
    /* end switch*/

    FrmAppConfig frmAppConfig = ctrlAppConfig.getForm(); //mengambil form dari control untuk proses menampilkan tampilan berikutnya

    /*count list All Product*/
    int vectSize = PstAppConfig.getCount(whereClause); //mengambil jumlah data yang tersedia
    AppConfig appconfig = ctrlAppConfig.getAppConfig(); //mengambil objek appconfig yang sedang dimaintenance
    msgString = ctrlAppConfig.getMessage(); //mengambil nilai message utama dalam proses manipulasi data

    /*switch list AppConfig*/ //proses untuk mendapatkan nilai start dari tabel yang akan ditampilkan
    if ((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDAppConfig == 0)) {
        start = PstAppConfig.findLimitStart(appconfig.getOID(), recordToGet, whereClause, orderClause);
    }

    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlAppConfig.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */ //mendapatkan list data yang akan ditampilkan
    listAppConfig = PstAppConfig.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listAppConfig.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listAppConfig = PstAppConfig.list(start, recordToGet, whereClause, orderClause);
    }

    //Setting status input apakah melakukan penambahan record baru, edit record lama, atau status pending (konfirmasi delete, cancel delete, atau masih ada error saat save
    if (iCommand == Command.ADD) {
        statusInput = 1;
    } else if (iCommand == Command.ASK || iCommand == Command.EDIT) {
        statusInput = 2;
    } else if (iCommand == Command.SAVE && frmAppConfig.errorSize() > 0) {
        statusInput = 9;
    }

%>

<%@ include file = "../session.jsp"%> 
<%    if (!privSettings) {
%>
<script>
    window.alert("Anda Tidak Memiliki Privilege Halaman Ini!");
    window.location = "index.jsp?page";
</script>
<%
    }
%>

<form class="form-horizontal" role="form" id="form-config" method ="post" action="" enctype="multipart/form-data">
    <input type="hidden" name="command" value="<%=iCommand%>">
    <input type="hidden" name="vectSize" value="<%=vectSize%>">
    <input type="hidden" name="start" value="<%=start%>">
    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
    <input type="hidden" name="hidden_appconfig_id" value="<%=IDAppConfig%>">
    <%if ((iCommand == Command.ADD) || (iCommand == Command.EDIT) || (frmAppConfig.errorSize() > 0)) {%>
    <div class="panel panel-default">
        <%if (IDAppConfig != 0) {%>
        <div class="panel-heading">Edit Config</div>
        <% } else { %>
        <div class="panel-heading">Config Baru</div>
        <% } %>
        <div class="panel-body">
            <div class="form-group has-error text-center">
                <span class="help-block"><%=msgString%></span>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="code">Code</label>  
                <div class="col-md-7">
                    <input name="<%=frmAppConfig.fieldNames[FrmAppConfig.FRM_CONFIG_NAME]%>" type="text" placeholder="Config Code" value="<%=appconfig.getConfigName()%>" class="form-control" autocomplete="off" required="">
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label" for="value">Value</label>  
                <div class="col-md-7">
                    <input name="<%=frmAppConfig.fieldNames[FrmAppConfig.FRM_CONFIG_VALUE]%>" type="text" placeholder="Config Value" value="<%=appconfig.getConfigValue()%>" class="form-control" autocomplete="off" required="">
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-3"></div>
                <div class="col-md-7">
                    <button type="submit" class="btn btn-primary btn-save">Simpan</button>
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
                <h3 class="panel-title">App Settings</h3>
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
                    <th>Code</th>
                    <th>Value</th>
                    <th>
                        <em class="glyphicon glyphicon-cog"></em>
                    </th>
                </tr> 
            </thead>
            <tbody>
                <%
                    for (int i = 0; i < listAppConfig.size(); i++) {
                        AppConfig view = (AppConfig) listAppConfig.get(i);
                %>
                <tr>
                    <td align="center"><%=i + start + 1%></td>
                    <td><%=view.getConfigName()%></td>
                    <td><%=view.getConfigValue()%></td>
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
                            if ((iCommand == Command.SAVE) && (iErrCode == ExtendedFRMMessage.NONE) && (IDAppConfig == 0)) {
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
        $('#form-config').find('input[name=hidden_appconfig_id]').val("0");
        $('#form-config').find('input[name=command]').val("<%=Command.NONE%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    });
    $('.btn-save').click(function() {
        cmdSave();
    });

    function cmdAdd() {
        $('#form-config').find('input[name=hidden_appconfig_id]').val("0");
        $('#form-config').find('input[name=command]').val("<%=Command.ADD%>");
        $('#form-config').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdDelete(IDAppConfig) {
        $('#form-config').find('input[name=hidden_appconfig_id]').val(IDAppConfig);
        $('#form-config').find('input[name=command]').val("<%=Command.DELETE%>");
        $('#form-config').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdSave() {
        $('#form-config').find('input[name=command]').val("<%=Command.SAVE%>");
        $('#form-config').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdEdit(IDAppConfig) {
        $('#form-config').find('input[name=hidden_appconfig_id]').val(IDAppConfig);
        $('#form-config').find('input[name=command]').val("<%=Command.EDIT%>");
        $('#form-config').find('input[name=prev_command]').val("<%=prevCommand%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdListFirst() {
        $('#form-config').find('input[name=command]').val("<%=Command.FIRST%>");
        $('#form-config').find('input[name=prev_command]').val("<%=Command.FIRST%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdListPrev() {
        $('#form-config').find('input[name=command]').val("<%=Command.PREV%>");
        $('#form-config').find('input[name=prev_command]').val("<%=Command.PREV%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdListNext() {
        $('#form-config').find('input[name=command]').val("<%=Command.NEXT%>");
        $('#form-config').find('input[name=prev_command]').val("<%=Command.NEXT%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
    function cmdListLast() {
        $('#form-config').find('input[name=command]').val("<%=Command.LAST%>");
        $('#form-config').find('input[name=prev_command]').val("<%=Command.LAST%>");
        $('#form-config').attr('action', "index.jsp?page=settings.jsp");
        $('#form-config').submit();
    }
</script>