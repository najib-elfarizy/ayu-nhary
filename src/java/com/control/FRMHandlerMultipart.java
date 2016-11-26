/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.control;

/**
 *
 * @author No Name
 */
/*
 * FRMHandler.java
 *
 * Created on March 13, 2002, 10:31 AM
 */

/**
 *
 * @author  gmudiasa
 * @version
 */
import java.util.*;

import java.text.*;

import com.dimata.qdep.form.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FRMHandlerMultipart implements I_ExtendedFRMType {

    public static final String YR = "_yr";
    public static final String MN = "_mn";
    public static final String DY = "_dy";
    public static final String MM = "_mm";
    public static final String HH = "_hh";

    /**
     * NIA
     */
    public static final String HR = "_hr";
    public static final String MI = "_mi";
    public static final String SEC = "_sec";

    public static String USER_DECIMAL_SYMBOL 	= ".";
    public static String USER_DIGIT_GROUP 		= ",";

    public static final String SYSTEM_DECIMAL_SYMBOL 	= ".";
    public static final String SYSTEM_DIGIT_GROUP 	= ",";

    private String formName;
    private String[] fieldNames;
    private int[] fieldTypes;
    private ArrayList<Integer> fieldDates = new ArrayList<Integer>();

    private Hashtable originalParamValues;
    private Hashtable finalParamValues;
    private Hashtable hsErrors;
    private String msgStyle = "errfont";

    //added by eka,
    //secure from any injection
    private String[] specialChar = {"+or+", "+and+", "<", ">", "'", "/*", "\"", "--", ";"};
    private String[] specialCharReplace = {" or ", " and ", "&lt;", "&gt;", "", "", "", "", ""};

    public FRMHandlerMultipart() {
    }

    /** Creates new FRMHandler */
    public FRMHandlerMultipart(I_FRMInterface ifrm, Hashtable paramValues) {
        formName = ifrm.getFormName();
        fieldNames = ifrm.getFieldNames();
        fieldTypes = ifrm.getFieldTypes();

        checkFieldDates();

        originalParamValues = paramValues;
        finalParamValues = new Hashtable();
        hsErrors = new Hashtable();

    }

    private void checkFieldDates() {
        int j = 0;
        int i;
        for (i = 0; i < fieldTypes.length; i++)
            if ((fieldTypes[i] & FILTER_TYPE) == TYPE_DATE) {
                fieldDates.add(new Integer(i));
                j++;
            }
    }

    // ----------------------------------------------- Request parameter
    public void setAndCheckParamValues() {
        try {
            cleanUpParamValues();
            convertDateParamValues();
            checkParamEntry();
            checkParamFormat();
            checkParamType();
        } catch(Exception e) {
        }
    }

    private void cleanUpParamValues() {
        String value;
        Enumeration K = originalParamValues.keys();
        while (K.hasMoreElements()) {
            boolean entry = true;
            String key = K.nextElement().toString();
            Vector values = new Vector();
            for (int i = 0; i < ((Vector)originalParamValues.get(key)).size(); i++) {
                value = (String)((Vector)originalParamValues.get(key)).elementAt(i);
                value = checkValueForInjection(value);

                if(value == null) value = "";
                else value = value.trim();
                values.add(value);
            }
            originalParamValues.put(key, values);
        }
    }

    private void convertDateParamValues() {
        Enumeration K = originalParamValues.keys();
        while (K.hasMoreElements()) {
            boolean entry = true;
            String key = K.nextElement().toString();
            for (int i = 0; i < fieldDates.size(); i++) {
                if (key.contains(fieldNames[fieldDates.get(i)])) {
                    entry = false;
                    break;
                }
            }
            if (entry) finalParamValues.put(key, originalParamValues.get(key));
        }
        for (int i = 0; i < fieldDates.size(); i++) {
            K = originalParamValues.keys();
            Hashtable aDate = new Hashtable();
            while (K.hasMoreElements()) {
                String key = K.nextElement().toString();
                if (key.contains(fieldNames[fieldDates.get(i)])) {
                    aDate.put(key, originalParamValues.get(key));
                }
            }
            String value = getParamDate(fieldNames[fieldDates.get(i)], aDate);
            Vector values = new Vector();
            values.add(value);
            finalParamValues.put(fieldNames[fieldDates.get(i)], values);
        }
    }

    public String checkValueForInjection(String value){
        if(value!=null && value.length()>0){
            String specChar = "";
            int index = -1;
            boolean stop = false;
            for(int i=0; i<specialChar.length; i++){
                specChar =  specialChar[i];
                stop = false;
                while(!stop){
                    index = value.indexOf(specChar);
                    if(index>-1){
                        value = replaceString(index, value, specChar, specialCharReplace[i]);
                    } else {
                        stop = true;
                    }
                }
            }
        }
        return value;
    }

    public String replaceString(int index, String value, String specChar, String charReplace){
        if(index==0){
            value = charReplace + value.substring(index+specChar.length(), value.length());
        } else {
            String st1 = "";
	        String st2 = "";
            st1 = value.substring(0, index);
            st2 = value.substring(index + specChar.length(), value.length());
            value = st1 + charReplace + st2;
        }
        return value;
    }

    private void checkParamType() {
        for (int idx = 0; idx < fieldNames.length; idx++) {
            String value = "";
            if (finalParamValues.containsKey(fieldNames[idx])) value = ((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
            switch (fieldTypes[idx] & FILTER_TYPE){
                case TYPE_INT:
                    if(value.length()<1){
                        if((fieldTypes[idx] & FILTER_ENTRY)==ENTRY_REQUIRED){
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                        }
                    }else{
                        try{
                            value = deFormatStringInteger(value);
                            int val = Integer.parseInt(value);
                        }catch(Exception e){
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_TYPE));
                        }
                    }
                    break;

                case TYPE_STRING:
                    break;

                case TYPE_FLOAT:
                    if(value.length()<1){
                        if((fieldTypes[idx] & FILTER_ENTRY)==ENTRY_REQUIRED){
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                        }
                    }else{
                        try{
                            value = deFormatStringDecimal(value);
                            float val = Float.parseFloat(value);
                        }catch(Exception e){
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_TYPE));
                        }
                    }
                    break;

                case TYPE_BOOL:
                    break;

                case TYPE_DATE:
                    break;

                case TYPE_BLOB:
                    break;

                case TYPE_LONG:
                    if(value.length()<1){
                        if((fieldTypes[idx] & FILTER_ENTRY)==ENTRY_REQUIRED){
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                        }
                    }else{
                        try{
                            value = deFormatStringInteger(value);
                            long val = Long.parseLong(value);
                        }catch(Exception e){
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_TYPE));
                        }
                    }
                    break;

                case TYPE_NUMERIC:
                    break;

                case TYPE_COLLECTION:
                    break;

                default:
            }
        }
    }

    private void checkParamFormat() {
        for (int idx = 0; idx < fieldNames.length; idx++) {
            String value = "";
            if (finalParamValues.containsKey(fieldNames[idx])) value = ((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
            switch (fieldTypes[idx] & FILTER_FORMAT){
                case FORMAT_TEXT:
                    break;

                case FORMAT_EMAIL:
                    if (!value.contains("@")) this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_FORMAT) + " (Dengan @)");
                    break;

                case FORMAT_CURENCY:
                    break;

                case FORMAT_ISBN:
                    String query = "^([0-9]{3})((\\-){1})([0-9]{3})((\\-){1})([0-9]{3})((\\-){1})([0-9]{1})$";
                    Pattern pattern = Pattern.compile(query);
                    Matcher matcher = pattern.matcher(value);
                    if (!matcher.find()) this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_FORMAT) + " (NNN-NNN-NNN-N)");
                    break;

                default:
            }
        }
    }

    private void checkParamEntry() {
        for (int idx = 0; idx < fieldNames.length; idx++) {
            String value = "";
            if (finalParamValues.containsKey(fieldNames[idx])) value = ((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
            switch (fieldTypes[idx] & FILTER_ENTRY){
                case ENTRY_OPTIONAL:
                    break;

                case ENTRY_REQUIRED:
                    if(((fieldTypes[idx] & FILTER_TYPE)==TYPE_STRING) || ((fieldTypes[idx] & FILTER_TYPE)==TYPE_DATE)){
                        if(value.length() < 1) {
                            this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                        }
                    }

                    if((fieldTypes[idx] & FILTER_TYPE)==TYPE_FLOAT){
                        try{
                            value = deFormatStringDecimal(value);
                            float val = Float.parseFloat(value);
                            //if(val<(0.00000001)){
                            //    this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                            //}
                        }catch(Exception e){
                            //this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_TYPE));
                        }
                    }
                    if((fieldTypes[idx] & FILTER_TYPE)==TYPE_LONG){
                        try{
                            value = deFormatStringInteger(value);
                            long val = Long.parseLong(value);
                            if(val<1){
                                this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                            }
                        }catch(Exception e){
                            // this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_TYPE));
                        }
                    }
                    if((fieldTypes[idx] & FILTER_TYPE)==TYPE_INT){
                        try{
                            value = deFormatStringInteger(value);
                            int val = Integer.parseInt(value);
                            if(val<1){
                                this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_REQUIRED));
                            }
                        }catch(Exception e){
                            //this.addError(idx, ExtendedFRMMessage.getErr(ExtendedFRMMessage.ERR_TYPE));
                        }
                    }

                    break;

                case ENTRY_CONDITIONAL:
                    break;

                default:
            }
        }
    }

    public int getParamInt(String parName, Hashtable aDate) {
        if (aDate.containsKey(parName)) {
            String sVal = aDate.get(parName).toString();
            sVal = sVal.substring(1, sVal.length()-1);
            try{
                return Integer.parseInt(sVal);
            }catch(Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public String getParamValue(int idx) {
        if(idx >= finalParamValues.size()) return "";
        return (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
    }

    public int getCommand() {
        String sVal = (String)((Vector)finalParamValues.get("command")).firstElement().toString();
        try{
            sVal = deFormatStringInteger(sVal);
            return Integer.parseInt(sVal);
        }catch(Exception e){
            return 0;
        }
    }

    protected String getString(int idx) {
        String sParam = (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
        try{
            return sParam;
        }catch(Exception e){
            return "";
        }
    }

    protected int getInt(int idx) {
        String sVal = (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
        try{
            sVal = deFormatStringInteger(sVal);
            return Integer.parseInt(sVal);
        }catch(Exception e){
            return 0;
        }

    }

    public int getInt(String fieldName) {
        String sVal = (String)((Vector)finalParamValues.get(fieldName)).firstElement().toString();
        try{
            sVal = deFormatStringInteger(sVal);
            return Integer.parseInt(sVal);
        }catch(Exception e){
            return 0;
        }
    }

    protected long getLong(int idx) {
        String sVal = "";
        if (finalParamValues.containsKey(fieldNames[idx])) {
            sVal = ((String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement()).toString();
        }
        try{
            sVal = deFormatStringInteger(sVal);
            return Long.parseLong(sVal);
        }catch(Exception e){
            return 0;
        }
    }

    public long getLong(String fieldName) {
        String sVal = "";
        if (finalParamValues.containsKey(fieldName))
            sVal = (String)((Vector)finalParamValues.get(fieldName)).firstElement().toString();
        try{
            sVal = deFormatStringInteger(sVal);
            return Long.parseLong(sVal);
        }catch(Exception e){
            return 0;
        }
    }

    protected float getFloat(int idx) {
        String sVal = (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
        try{
            sVal = deFormatStringDecimal(sVal);
            return Float.parseFloat(sVal);
        }catch(Exception e){
            return 0;
        }
    }

    protected double getDouble(int idx) {
        String sVal = (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
        try{
            sVal = deFormatStringDecimal(sVal);
            return Double.parseDouble(sVal);
        }catch(Exception e){
            return 0;
        }
    }

    protected boolean getBoolean(int idx) {
        String sVal = (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
        try{
            if(sVal.equalsIgnoreCase("true") || sVal.equals("1"))
                return true;

            return false;
        }catch(Exception e){
            return false;
        }
    }

    protected Date getDate(int idx) {
        String sVal = (String)((Vector)finalParamValues.get(fieldNames[idx])).firstElement().toString();
        try{
            return new Date(Long.parseLong(sVal));
        }catch(Exception e){
            return null;
        }
    }

    protected Vector getVectorInt(int idx){
        try{
            Vector ints = new Vector();
            Vector sVals = (Vector)finalParamValues.get(fieldNames[idx]);
            if( (sVals==null) || (sVals.size()<1))
                return ints;

            for(int i=0; i < sVals.size(); i++){
                try{
                    ints.add(new Integer(Integer.parseInt((String)sVals.elementAt(i).toString())));
                }catch (Exception exc){
                    System.out.println("EXC : getVectorInt i="+i+" - "+exc);
                }
            }

            return ints;

        }catch(Exception e){
        }

        return new Vector(1,1);

    }

    /**
     * KAR
     **/
    protected Vector getVectorLong(int idx){
        try{
            Vector longs = new Vector();
            Vector sVals = (Vector)finalParamValues.get(fieldNames[idx]);
            if( (sVals==null) || (sVals.size()<1))
                return longs;

            for(int i=0; i < sVals.size(); i++){
                try{
                    longs.add(new Long(Long.parseLong((String)sVals.elementAt(i).toString())));
                }catch (Exception exc){
                    System.out.println("EXC : getVectorLong i="+i+" - "+exc);
                }
            }

            return longs;

        }catch(Exception e){
        }

        return new Vector(1,1);

    }
    
    protected Vector getVectorString(int idx){
        try{
            Vector strings = new Vector();
            Vector sVals = (Vector)finalParamValues.get(fieldNames[idx]);
            if( (sVals==null) || (sVals.size()<1))
                return strings;

            for(int i=0; i < sVals.size(); i++){
                try{
                    strings.add(new String(((String)sVals.elementAt(i).toString())));
                }catch (Exception exc){
                    System.out.println("EXC : getVectorLong i="+i+" - "+exc);
                }
            }

            return strings;

        }catch(Exception e){
        }

        return new Vector(1,1);

    }
    
    protected Vector getVectorString(String fieldName){
        try{
            Vector strings = new Vector();
            Vector sVals = (Vector)finalParamValues.get(fieldName);
            if( (sVals==null) || (sVals.size()<1))
                return strings;

            for(int i=0; i < sVals.size(); i++){
                try{
                    strings.add(new String(((String)sVals.elementAt(i).toString())));
                }catch (Exception exc){
                    System.out.println("EXC : getVectorLong i="+i+" - "+exc);
                }
            }

            return strings;

        }catch(Exception e){
        }

        return new Vector(1,1);

    }    

    // ----------------------------------------------- Error form handler
    public int errorSize() {
        return hsErrors.size();
    }


    public void clearError() {
        this.hsErrors.clear();
    }

    public Hashtable getParamValue() {
        return finalParamValues;
    }

    public Hashtable getErrors() {
        return hsErrors;
    }


    public String getErrorMsg(int i) {
        String msg = "";
        String key = String.valueOf(i);
        try {
            msg = (String)hsErrors.get(key);
        } catch(Exception e) {
        }

        if(msg == null || msg.length() < 1) {
            return "";
        }
        return "<span class=\""+msgStyle+"\">"+msg+"</span>";
    }
    //update by satrya 2012-11-10
    public String getErrorMsgModif(int i) {
        String msg = "";
        String key = String.valueOf(i);
        try {
            msg = (String)hsErrors.get(key);
        } catch(Exception e) {
        }

        if(msg == null || msg.length() < 1) {
            return "";
        }
        return "<span class=\""+msgStyle+"\">" + msg + " ,If Employe is a Daily Worker (DL)  please replace 'DL-' with '12' ,for  example 'DL-333' become to '12333'.     If Employe's  Status  'Resigned'  please input the barcode number, barcode number is unique for example -R(BarcodeNumb/PinNumber)</span>";
    }


    public void addError(int fldIndex, String errString) {
        if(hsErrors.containsKey(String.valueOf(fldIndex)))
            return;
        hsErrors.put(String.valueOf(fldIndex), errString);
    }

    public void removeError(int fldIndex) {
        if(!hsErrors.containsKey(String.valueOf(fldIndex)))
            return;
        hsErrors.remove(String.valueOf(fldIndex));
    }


    // ----------------------------------------------- Composed form object handler
    private String getParamDate(String baseName, Hashtable aDate) {
        try {
            int yr = getParamInt(baseName + YR, aDate);
            int mn = getParamInt(baseName + MN, aDate);
            int dy = getParamInt(baseName + DY, aDate);
            int hh = getParamInt(baseName + HH, aDate)!=0? getParamInt(baseName + HH, aDate):getParamInt(baseName + HR, aDate);
            int mm = getParamInt(baseName + MM, aDate)!=0? getParamInt(baseName + MM, aDate):getParamInt(baseName + MI, aDate) ;

            if((yr<1 || mn<1 || dy<1) && hh<1){
                return "";
            }
            else if((yr<1 || mn<1 || dy<1) && hh>1){
                Date dtX= new Date();
                yr= dtX.getYear();
                mn= dtX.getMonth();
                dy=dtX.getDate();

            }
            Date dt = new Date(yr - 1900, mn -1, dy, hh, mm);
            return String.valueOf(dt.getTime());
        }catch(Exception e) {
            return "Error getParamInt";
        }
    }


    /**
     * NIA
     **/
    private Date getTime(String baseName, Hashtable aDate) {
        try{
            int hr = getParamInt(baseName + HR, aDate);
            int mi = getParamInt(baseName + MI, aDate);
            int sec = getParamInt(baseName + SEC, aDate);

            //System.out.println("-----------------------");
            //System.out.println("hr : "+hr);
            //System.out.println("mi : "+mi);
            //System.out.println("sec : "+sec);
            //System.out.println("-----------------------");

            Date dt = new Date();

            dt.setHours(hr);
            dt.setMinutes(mi);
            dt.setSeconds(sec);

            return dt;
        }catch(Exception e){
            System.out.println("get Date "+e.toString());
            //return new Date();
            return null;
        }
    }

    public static  String deFormatStringDecimal(String str){
        try{
            StringTokenizer strToken = new StringTokenizer(str, USER_DIGIT_GROUP);
            String strValue = "";
            while (strToken.hasMoreTokens()){
                strValue = strValue + strToken.nextToken();
            }
            strValue = strValue.replace(USER_DECIMAL_SYMBOL.charAt(0),SYSTEM_DECIMAL_SYMBOL.charAt(0) );

            return strValue;
        }catch(Exception exc){
            System.out.println("error deFormatStringDecimal"+exc);
            return "";
        }
    }

    private String deFormatStringInteger(String str){
        try{
            StringTokenizer strToken = new StringTokenizer(str, USER_DIGIT_GROUP);
            String strValue = "";
            while (strToken.hasMoreTokens()){
                strValue = strValue + strToken.nextToken();
            }
            return strValue;

        }catch(Exception exc){
            System.out.println("error deFormatStringDecimal"+exc);
            return "";
        }
    }

    public  static String userFormatStringDecimal(double number) {
        String format = "##"+SYSTEM_DIGIT_GROUP+"###"+SYSTEM_DECIMAL_SYMBOL+"00";
        DecimalFormat df = new DecimalFormat(format);

        boolean bBetweenZeroToOne = false;
        if((0 < number) && (number < 1))
        {
            bBetweenZeroToOne = true;
        }

        String add = "";
        try{
            StringTokenizer strToken = new StringTokenizer(format, SYSTEM_DIGIT_GROUP);
            String str = "";
            while (strToken.hasMoreTokens()){
                str = strToken.nextToken();
            }

            if(str!=null && str.length()>0){
                int lg = str.length();
                if(str.equals("#") || str.equals("##")){
                    String num = String.valueOf(number);
                    StringTokenizer tok = new StringTokenizer(num, ".");
                    while(tok.hasMoreTokens()){
                        str = tok.nextToken();
                    }

                    if(str!=null && str.length()>0){
                        if(Integer.parseInt(str)==0){
                            add=".0";
                        }
                    }
                }
            }

            String strValue = (df.format((double)number).toString())+add;
            if(strValue != null && strValue.length()>0){
                add = strValue.substring(0,strValue.indexOf(SYSTEM_DECIMAL_SYMBOL));
                add = add.replace(SYSTEM_DIGIT_GROUP.charAt(0),USER_DIGIT_GROUP.charAt(0) );
                add = add + USER_DECIMAL_SYMBOL + strValue.substring(strValue.indexOf(SYSTEM_DECIMAL_SYMBOL)+1,strValue.length());
            }

            if(number==0 || bBetweenZeroToOne)
            {
                add = "0" + add;
            }

        }
        catch(Exception e){
            System.out.println("Exception on FrmHandler.userFormatStringDecimal : "+e.toString());
            add="";
        }
        return add;
    }

    public static String userFormatStringLong(long number){
        String format = "##"+SYSTEM_DIGIT_GROUP+"###";
        DecimalFormat df = new DecimalFormat(format);
        String strValue = "";
        try{
            strValue = df.format(number).toString();
            strValue = strValue.replace(SYSTEM_DIGIT_GROUP.charAt(0),USER_DIGIT_GROUP.charAt(0));
        }
        catch(Exception e){
            System.out.println("Exception on FrmHandler.userFormatStringLong : "+e.toString());
            strValue="";
        }
        return  strValue;
    }

    public String getMsgStyle(){ return msgStyle; }

    public void setMsgStyle(String msgStyle){ this.msgStyle = msgStyle; }

    public  static String userFormatStringDecimalWithoutPoint(double number) {
        String format = "##"+SYSTEM_DIGIT_GROUP+"###"+SYSTEM_DECIMAL_SYMBOL+"00";
        DecimalFormat df = new DecimalFormat(format);

        boolean bBetweenZeroToOne = false;
        if( (0 < number) && (number < 0)) {
            bBetweenZeroToOne = true;
        }

        String add = "";
        try{
            StringTokenizer strToken = new StringTokenizer(format, SYSTEM_DIGIT_GROUP);
            String str = "";
            while (strToken.hasMoreTokens()){
                str = strToken.nextToken();
            }

            if(str!=null && str.length()>0){
                int lg = str.length();
                if(str.equals("#") || str.equals("##")){
                    String num = String.valueOf(number);
                    StringTokenizer tok = new StringTokenizer(num, ".");
                    while(tok.hasMoreTokens()){
                        str = tok.nextToken();
                    }

                    if(str!=null && str.length()>0){
                        if(Integer.parseInt(str)==0){
                            add=".0";
                        }
                    }

                }
            }

            String strValue = (df.format((double)number).toString())+add;
            if(strValue != null && strValue.length()>0){
                add = strValue.substring(0,strValue.indexOf(SYSTEM_DECIMAL_SYMBOL));
                add = add.replace(SYSTEM_DIGIT_GROUP.charAt(0),USER_DIGIT_GROUP.charAt(0) );
                add = add + USER_DECIMAL_SYMBOL + strValue.substring(strValue.indexOf(SYSTEM_DECIMAL_SYMBOL)+1,strValue.length());
            }

            if(number==0 || bBetweenZeroToOne) {
                add = "0" + add;
            }

        }
        catch(Exception e){
            System.out.println("Exception on FrmHandler.userFormatStringDecimalWithoutPoint : "+e.toString());
            add="";
        }

        add = add.substring(0,add.indexOf(","));
        if(add.length()==0){
            add = "0";
        }
        return add;
    }

    public void setDigitSeparator(String digitSeparator){
        this.USER_DIGIT_GROUP = digitSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator){
        this.USER_DECIMAL_SYMBOL = decimalSeparator;
    }

    public static void main(String args[]){
        String result = userFormatStringDecimal(0.01);
        System.out.println(result);
    }
}