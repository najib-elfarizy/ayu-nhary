/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.master.form;

import com.control.FRMHandlerMultipart;
import com.control.I_ExtendedFRMType;
import com.dimata.qdep.form.I_FRMInterface;
import com.master.entity.Proyek;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class FrmProyek extends FRMHandlerMultipart implements I_FRMInterface, I_ExtendedFRMType {
    
    private Proyek ent;
    private Hashtable parameters;
    
    public static final String FRM_NAME                 = "frm_proyek";
    
     public static final int FRM_ID_PROYEK              = 0;
    public static final int FRM_NAMA_PROYEK            = 1;
    public static final int FRM_WAKTU_MULAI            = 2;
    public static final int FRM_WAKTU_SELESAI          = 3;
    public static final int FRM_TOTAL                  = 4;
    public static final int FRM_TANGGAL_MULAI          = 5;
    public static final int FRM_TANGGAL_SELESAI         = 6;

    public static final String[] fieldNames = {
         "ID_PROYEK",
        "NAMA_PROYEK",
        "WAKTU_MULAI",
        "WAKTU_SELESAI",
        "TOTAL",
        "TANGGAL_MULAI",
        "TANGGAL_SELESAI"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_DATE
    };
    
    public FrmProyek(){
    }

    public FrmProyek(Proyek ent){
        this.ent = ent;
    }

    public FrmProyek(Hashtable paramValues, Proyek ent){
        super(new FrmProyek(ent), paramValues);
	this.ent = ent;
    }

    public String getFormName() { return FRM_NAME; }

    public int[] getFieldTypes() { return fieldTypes; }

    public String[] getFieldNames() { return fieldNames; }

    public int getFieldSize() { return fieldNames.length; }

    public Proyek getEntityObject(){ return ent; }

    @Override
    public Hashtable getParamValue() { return parameters; }

    public Proyek requestEntityObject(Proyek ent) {
        this.setAndCheckParamValues(); 
        ent.setNamaProyek(this.getString(FRM_NAMA_PROYEK));   
        ent.setWaktuMulai(dateTime(this.getDate(FRM_TANGGAL_MULAI),this.getDate(FRM_WAKTU_MULAI)));
        Calendar mulai = DateToCalendar(ent.getWaktuMulai());
        ent.setWaktuSelesai(dateTime(this.getDate(FRM_TANGGAL_SELESAI),this.getDate(FRM_WAKTU_SELESAI)));
        Calendar selesai = DateToCalendar(ent.getWaktuSelesai());
        ent.setTotal(totalWaktuJam(mulai,selesai));
        return ent;
    }
    
    private Date dateTime(Date date, Date time) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    System.out.println(year);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    return new Date(
         date.getYear(), month, day, 
         time.getHours(), time.getMinutes(), time.getSeconds()
       );
    }
    
    private static Calendar DateToCalendar(Date date){ 
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return cal;
    }
    
    private static float totalWaktuJam(Calendar mulai, Calendar selesai){
        float yearmulai = mulai.get(Calendar.YEAR) * 8760;
        float monthmulai = (mulai.get(Calendar.MONTH)+1) * 720;
        float daymulai = mulai.get(Calendar.DAY_OF_MONTH) * 24;
        float hourmulai = mulai.get(Calendar.HOUR_OF_DAY);
        float minutemulai = (mulai.get(Calendar.MINUTE)+1) / 60;
        float yearselesai = selesai.get(Calendar.YEAR) * 8760;
        float monthselesai = (selesai.get(Calendar.MONTH)+1) * 720;
        float dayselesai = selesai.get(Calendar.DAY_OF_MONTH) * 24;
        float hourselesai = selesai.get(Calendar.HOUR_OF_DAY);
        float minuteselesai = (selesai.get(Calendar.MINUTE)+1) / 60;
        float year = (yearselesai - yearmulai);        
        float month = (monthselesai - monthmulai);        
        float day = (dayselesai - daymulai);       
        float hour = (hourselesai - hourmulai);
        float minute = (minuteselesai - minutemulai);
        float total = year + month + day + hour + minute;
        return total;
    }
   
}
