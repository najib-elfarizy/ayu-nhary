/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.control;

/**
 *
 * @author No Name
 */
import java.util.*;

import javax.servlet.http.*;

import com.dimata.util.*;

public class ExtendedControlDate {

    public static final int SEQUENT_MDY = 0; // month, date, year
    public static final int SEQUENT_DMY = 1; // date, month, year

    /*
        Date 		=> date name, initial date, month format, css class,
                                   month-date delim, date-year delim, date mailstone, date range
        DateTime 	=> hour format, minute interval, delim

    private int startYear	= getIntYear(new Date());
    private int endYear = getIntYear(new Date()) + 1;

     */

    public static String drawDate(String dtName, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, int mailstone, int range) {
        return drawDate( dtName,  dtInit,  mnFormat,  cssClass,  mdDelim,  dyDelim,  mailstone,  range,"");
    }

    public static String drawDate(String dtName, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, int mailstone, int range,String attribute) {
        if(dtName == null) dtName = new String("dt");

        String mmCtrlName = dtName + "_mn";
        String ddCtrlName = dtName + "_dy";
        String yyCtrlName = dtName + "_yr";


        int startYear = Validator.getIntYear(new Date());
        int endYear = Validator.getIntYear(new Date()) + 1;

        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
            startYear = yy + mailstone;
            endYear = startYear + range;
            if(range < 0) {
                endYear = yy + mailstone;
                startYear = endYear + range;
            }
        }else {
            endYear = yy + mailstone;
            startYear = endYear - range;
        }

        int mmValue = -1;
        int ddValue = -1;
        int yyValue = -1;

        if(dtInit!=null){
            mmValue = Validator.getIntMonth(dtInit);
            ddValue = Validator.getIntDate(dtInit);
            yyValue = Validator.getIntYear(dtInit);
        }


        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                if(dtInit==null){
                    htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
                }
                for(int i = 1; i <= 12; i++) {
                    if(dtInit!=null){
                        s = (i == mmValue) ? "selected" :  "";
                    }
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

            case 4: // long name January, Febuary, ...
                if(dtInit==null){
                    htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
                }
                for(int i = 1; i <= 12; i++) {
                    if(dtInit!=null){
                        s = (i == mmValue) ? "selected" :  "";
                    }
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongInaMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                if(dtInit==null){
                    htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
                }
                for(int i = 1; i <= 12; i++) {
                    if(dtInit!=null){
                        s = (i == mmValue) ? "selected" :  "";
                    }
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortInaMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>" + mdDelim + "\n";
                break;
        } //end switch

        // day combobox
        htmlSelect = htmlSelect + "\n<select name=\"" + ddCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";

        if(dtInit==null){
            htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
        }
        for(int i = 1; i <= 31; i++) {
            if(dtInit!=null){
                s = (i == ddValue) ? "selected" :  "";
            }
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>"+ dyDelim +"\n";

        //year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\""+attribute+">\n";

        if(dtInit==null){
            htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
        }
        for(int i = startYear; i <= endYear; i++) {
            if(dtInit!=null){
                s = (i == yyValue) ? "selected" :  "";
            }
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }

    public static String drawDate_old(String dtName, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, int mailstone, int range,String attribute) {
        if(dtName == null) dtName = new String("dt");

        String mmCtrlName = dtName + "_mn";
        String ddCtrlName = dtName + "_dy";
        String yyCtrlName = dtName + "_yr";


        int startYear = Validator.getIntYear(new Date());
        int endYear = Validator.getIntYear(new Date()) + 1;

        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
            startYear = yy + mailstone;
            endYear = startYear + range;
            if(range < 0) {
                endYear = yy + mailstone;
                startYear = endYear + range;
            }
        }else {
            endYear = yy + mailstone;
            startYear = endYear - range;
        }

        int mmValue = -1;
        int ddValue = -1;
        int yyValue = -1;

        if(dtInit!=null){
            mmValue = Validator.getIntMonth(dtInit);
            ddValue = Validator.getIntDate(dtInit);
            yyValue = Validator.getIntYear(dtInit);
        }


        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                if(dtInit==null){
                    htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
                }
                for(int i = 1; i <= 12; i++) {
                    if(dtInit!=null){
                        s = (i == mmValue) ? "selected" :  "";
                    }
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

            case 4: // long name January, Febuary, ...
                if(dtInit==null){
                    htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
                }
                for(int i = 1; i <= 12; i++) {
                    if(dtInit!=null){
                        s = (i == mmValue) ? "selected" :  "";
                    }
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                if(dtInit==null){
                    htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
                }
                for(int i = 1; i <= 12; i++) {
                    if(dtInit!=null){
                        s = (i == mmValue) ? "selected" :  "";
                    }
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>" + mdDelim + "\n";
                break;
        } //end switch

        // day combobox
        htmlSelect = htmlSelect + "\n<select name=\"" + ddCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";

        if(dtInit==null){
            htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
        }
        for(int i = 1; i <= 31; i++) {
            if(dtInit!=null){
                s = (i == ddValue) ? "selected" :  "";
            }
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>"+ dyDelim +"\n";

        //year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\""+attribute+">\n";

        if(dtInit==null){
            htmlSelect = htmlSelect + "\t<option selected value=\"-1\">Pilih...</option>\n";
        }
        for(int i = startYear; i <= endYear; i++) {
            if(dtInit!=null){
                s = (i == yyValue) ? "selected" :  "";
            }
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }

    public static String drawStaticDate(String dtName, Date stDate, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, String attribute) {
        if(stDate == null) stDate = new Date();
        if(dtInit == null) dtInit = new Date();
        if(dtName == null) dtName = new String("dt");

        String mmCtrlName = dtName + "_mn";
        String ddCtrlName = dtName + "_dy";
        String yyCtrlName = dtName + "_yr";

        int startYear	= Validator.getIntYear(stDate);
        int endYear = Validator.getIntYear(new Date());

        int mmValue = Validator.getIntMonth(dtInit);
        int ddValue = Validator.getIntDate(dtInit);
        int yyValue = Validator.getIntYear(dtInit);

        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

            case 4: // long name January, Febuary, ...
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>" + mdDelim + "\n";
                break;
        } //end switch

        // day combobox
        htmlSelect = htmlSelect + "\n<select name=\"" + ddCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";
        for(int i = 1; i <= 31; i++) {
            s = (i == ddValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>"+ dyDelim +"\n";

        //year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\""+attribute+">\n";
        for(int i = startYear; i <= endYear; i++) {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }



    public static String drawDateWithStyle(String dtName, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, int mailstone, int range) {
        return drawDateWithStyle( dtName,  dtInit,  mnFormat,  cssClass,  mdDelim,  dyDelim,  mailstone,  range,"");
    }

    public static String drawDateWithStyle(String dtName, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, int mailstone, int range, String attribute) {
        if(dtInit == null) dtInit = new Date();
        if(dtName == null) dtName = new String("dt");

        String mmCtrlName = dtName + "_mn";
        String ddCtrlName = dtName + "_dy";
        String yyCtrlName = dtName + "_yr";

        int startYear	= Validator.getIntYear(dtInit);
        int endYear = Validator.getIntYear(dtInit) + 1;

        int mmValue = Validator.getIntMonth(dtInit);
        int ddValue = Validator.getIntDate(dtInit);
        int yyValue = Validator.getIntYear(dtInit);

        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
            startYear = yy + mailstone;
            endYear = startYear + range;
        }else {
            endYear = yy + mailstone;
            startYear = endYear - range;
        }


        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

            case 4: // long name January, Febuary, ...
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>" + mdDelim + "\n";
                break;
        } //end switch

        // day combobox
        htmlSelect = htmlSelect + "\n<select name=\"" + ddCtrlName + "\" class=\""+cssClass+"\" "+attribute+">\n";
        for(int i = 1; i <= 31; i++) {
            s = (i == ddValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>"+ dyDelim +"\n";

        //year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\""+attribute+">\n";
        for(int i = startYear; i <= endYear; i++) {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }


    public static String drawDateMY(String dtName, Date dtInit, String mnFormat,
    String cssClass, String mdDelim, String dyDelim, int mailstone, int range) {
        if(dtInit == null) dtInit = new Date();
        if(dtName == null) dtName = new String("dt");

        String mmCtrlName = dtName + "_mn";
        String yyCtrlName = dtName + "_yr";
        String ddCtrlName = dtName + "_dy";

        int startYear	= Validator.getIntYear(dtInit);
        int endYear = Validator.getIntYear(dtInit) + 1;

        int mmValue = Validator.getIntMonth(dtInit);
        int yyValue = Validator.getIntYear(dtInit);

        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
            if(range >= 0){
                startYear = yy + mailstone;
                endYear = startYear + range;
            }else{
                endYear = yy + mailstone;
                startYear = endYear + range;
            }
        }else {
            endYear = yy + mailstone;
            startYear = endYear - range;
        }


        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

            case 4: // long name January, Febuary, ...
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>"+ mdDelim +"\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>" + mdDelim + "\n";
                break;
        } //end switch

        // day
        htmlSelect = htmlSelect + "\n<input type=\"hidden\" name=\"" + ddCtrlName + "\" value=\"1\" >\n";

        //year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\">\n";
        for(int i = startYear; i <= endYear; i++) {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }

    //drawDateYear(SessPL.plFieldText[SessPL.PL_ANNUALS], new Date(),"",0,interval)
    /*
    public static String drawDateYear(String dtName, Date dtInit, String cssClass,int mailstone,int range)
   {
        int startYear = Validator.getIntYear(dtInit);
        int endYear = Validator.getIntYear(dtInit) + 1;

        int yyValue = Validator.getIntYear(dtInit);
        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
             startYear = yy + mailstone;
             endYear = startYear + range;
        }else {
             endYear = yy + range;
             startYear = yy + mailstone;
        }
        System.out.println("startYear : "+startYear+", endYear : "+endYear);
        String s = "";

        String htmlSelect = "\n<select name=\"" + dtName + "\" class=\""+cssClass+"\">\n";
        for(int i = startYear; i <= endYear; i++) {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
   }
     */

    public static String drawDateYear(String dtName, Date dtInit, String cssClass,int mailstone,int range) {
        int startYear	= Validator.getIntYear(dtInit);
        int endYear = Validator.getIntYear(dtInit) + 1;

        int yyValue = Validator.getIntYear(dtInit);
        System.out.println("yyValue "+yyValue);
        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
            startYear = yy + mailstone;
            endYear = startYear + range;
            if(range < 0) {
                endYear = yy + mailstone;
                startYear = endYear + range;
            }
        }else {
            endYear = yy + range;
            startYear = yy + mailstone;
        }
        String s = "";

        String htmlSelect = "\n<select name=\"" + dtName + "\" class=\""+cssClass+"\">\n";
        for(int i = startYear; i <= endYear; i++) {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }

    public static String drawDateYear(String dtName, int dtInit, String cssClass,int mailstone,int range) {
        int startYear	= dtInit;
        int endYear = dtInit;

        int yy = Validator.getIntYear(new Date());
        if(mailstone >= 0) {
            startYear = yy + mailstone;
            endYear = startYear + range;
        }else {
            endYear = yy + range;
            startYear = yy + mailstone;
        }
        String s = "";

        String htmlSelect = "\n<select name=\"" + dtName + "\" class=\""+cssClass+"\">\n";
        s = (dtInit == 0) ? "selected" :  "";
        htmlSelect = htmlSelect + "\t<option value=\"0\""+ s +">-select-</option>\n";
        for(int i = startYear; i <= endYear; i++) {
            s = (i == dtInit) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }

    public static String drawDateMY(String dtName, Date dtInit) {
        return drawDateMY(dtName, dtInit, "MMM", "", "", ",", 0, 1);
    }
    public static String drawDateMY(String dtName, Date dtInit,String style) {
        return drawDateMY(dtName, dtInit, "MMM", style, "", ",", 0, 1);
    }


    public static String drawStaticDate(String dtName, Date dtInit) {
        Date stDate = Validator.composeDate("2001","01","01");
        return drawStaticDate(dtName, stDate, dtInit, "MMM", "", "", "", "");
    }

    public static String drawStaticDate(String dtName, Date dtInit, String cssClass) {
        Date stDate = Validator.composeDate("2001","01","01");
        return drawStaticDate(dtName, stDate, dtInit, "MMM", cssClass, "", "", "");
    }


    public static String drawDate(String dtName, Date dtInit) {
        return drawDate(dtName, dtInit, "MMM", "", "", ",", 0, 1);
    }

    public static String drawDate(String dtName, Date dtInit, String cssClass) {
        return drawDate(dtName, dtInit, "MMM", cssClass, "", ",", 0, 1);
    }

    public static String drawDateWithStyle(String dtName, Date dtInit, String cssClass) {
        return drawDateWithStyle(dtName, dtInit, "MMM", cssClass, "", ",", 0, 1);
    }

    public static String drawDateWithStyle(String dtName, Date dtInit, String cssClass, String attribute) {
        return drawDateWithStyle(dtName, dtInit, "MMM", cssClass, "", ",", 0, 1,attribute);
    }

    public static String drawDate(String dtName, Date dtInit, String cssClass, String mnFormat) {
        return drawDate(dtName, dtInit, mnFormat, cssClass, "", ",", 0, 1);
    }

    /**
     * e.g. set mailstone = +5 and range = -10
     * will return string: min year = (inityear + 5) - 10 to max year = (inityear + 5)
     */
    public static String drawDate(String dtName, Date dtInit, int mailstone, int range) {
        return drawDate(dtName, dtInit, "MMM", "", "", ",", mailstone, range);
    }

    public static String drawDateWithStyle(String dtName, Date dtInit, int mailstone, int range,String cssStyle) {
        return drawDate(dtName, dtInit, "MMM", cssStyle, "", ",", mailstone, range);
    }

    public static String drawDateWithStyle(String dtName, Date dtInit, int mailstone, int range,String cssStyle, String attribute) {
        return drawDate(dtName, dtInit, "MMM", cssStyle, "", ",", mailstone, range,attribute);
    }

    public static String drawDate(String dtName, Date dtInit, String cssClass, int mailstone, int range) {
        return drawDate(dtName, dtInit, "MMM", cssClass, "", ",", mailstone, range);
    }


    //public static String drawTime(String dtName, Date dtInit, String mnFormat, String cssClass, String mdDelim, String dyDelim, int mailstone, int range)
    public static String drawTime(String dtName, Date dtInit, String cssClass,
    int hrFormat, int miInterval) {
        return drawTime(dtName, dtInit, cssClass,
        hrFormat, miInterval, 0) ;
    }

    public static String drawTime(String dtName, Date dtInit, String cssClass,
    int hrFormat, int miInterval, int secInterval) {
        if(dtInit == null) dtInit = new Date();
        if(dtName == null) dtName = new String("dt");
        if(miInterval < 1 || miInterval > 30) miInterval = 1;

        String hrCtrlName = dtName + "_hr";
        String miCtrlName = dtName + "_mi";
        String secCtrlName = dtName + "_sec";
        String merCtrlName = dtName + "_mer";

        int hrValue = dtInit.getHours();
        int miValue = dtInit.getMinutes();
        int secValue = dtInit.getSeconds();
        String ampmValue = "am";
        //if((hrValue > 12) || (hrValue == 12 && miValue == 0))
        //    ampmValue = "pm";

        String s = "";
        String htmlSelect = "";


        // hours combobox
        int ampmHourValue = 0;
        int maxHour = hrValue;
        htmlSelect = htmlSelect + "\n<select name=\"" + hrCtrlName + "\" class=\""+cssClass+"\">\n";
        if(hrFormat <= 12) {
            if(hrValue >= 12)
                ampmHourValue = hrValue - 12;
            else
                ampmHourValue = hrValue;
            maxHour = hrFormat + 1;
        }else {
            ampmHourValue = hrValue;
            maxHour = hrFormat;
        }


        for(int i = 0; i < maxHour; i++) {
            s = (i == ampmHourValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>:\n";

        //minutes combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + miCtrlName + "\" class=\""+cssClass+"\">\n";
        for(int i = 0; i < 60; i=i+miInterval) {
            s = (i == miValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
            //i += miInterval - 1;
        }
        htmlSelect = htmlSelect + "</select>\n";

        //seconds combobox
        if(secInterval>0) {
            htmlSelect = htmlSelect  + "\n:<select name=\"" + secCtrlName + "\" class=\""+cssClass+"\">\n";
            for(int i = 0; i < 60; i=i+secInterval) {
                s = (i == secValue) ? "selected" :  "";
                htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + zeroFormat(i) + "</option>\n";
                //i += secInterval - 1;
            }
            htmlSelect = htmlSelect + "</select>\n";
        }

        //am-pm combobox
        if(hrFormat <= 12) {
            htmlSelect = htmlSelect  + "\n<select name=\"" + merCtrlName + "\">\n";
            s = (ampmValue.equals("am")) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"am\""+ s +">AM</option>\n";
            s = (ampmValue.equals("pm")) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"pm\""+ s +">PM</option>\n";
            htmlSelect = htmlSelect + "</select>\n";
        }

        return htmlSelect;
    }


    public static String drawTime(String dtName, Date dtInit) {
        return drawTime(dtName, dtInit, "", 24, 5);
    }

    public static String drawTimeSec(String dtName, Date dtInit, String style) {
        return drawTime(dtName, dtInit, style, 24, 1, 1);
    }

    public static String drawTime(String dtName, Date dtInit, String style) {
        return drawTime(dtName, dtInit, style, 24, 5);
    }

    public static String drawTime(String dtName, Date dtInit, int miInterval) {
        return drawTime(dtName, dtInit, "", 24, miInterval);
    }


    public static Date getDate(String dtName, HttpServletRequest req) {
        Date dt = null;
        try {
            String mm = req.getParameter(dtName + "_mn");
            String dd = req.getParameter(dtName + "_dy");
            String yy = req.getParameter(dtName + "_yr");

            dt = Validator.composeDate(yy, mm, dd);
        }catch(Exception e) {
            //System.out.println("Invalid getDate() : " + e.toString());
        }
        return dt;
    }



    public static Date getDateTime(String dtName, HttpServletRequest req) {
        Date dt = null;
        try {
            String mm = req.getParameter(dtName + "_mn");
            String dd = req.getParameter(dtName + "_dy");
            String yy = req.getParameter(dtName + "_yr");
            String hr = (((String)req.getParameter(dtName + "_hr"))==null) ? "0" : ((String)(req.getParameter(dtName + "_hr"))) ;
            String mi = (((String)req.getParameter(dtName + "_mi"))==null) ? "0" : ((String)(req.getParameter(dtName + "_mi"))) ;
//            String sec = (((String)req.getParameter(dtName + "_sec"))==null) ? "0" : ((String)(req.getParameter(dtName + "_sec"))) ;

//            dt = Validator.composeDateTime(yy, mm, dd, hr, mi, sec);
            dt = Validator.composeDateTime(yy, mm, dd, hr, mi);
        }catch(Exception e) {
            //System.out.println("Invalid getDateTime() : " + e.toString());
        }
        return dt;
    }

    public static long getDateLong(String dtName, HttpServletRequest req) {
        try {
            Date dt = getDateTime(dtName, req);
            return dt.getTime();
        }catch(Exception e) {
            //System.out.println("Invalid getDateLong() : " + e.toString());
        }
        return new Date().getTime();
    }

    public static String getDateLongString(String dtName, HttpServletRequest req) {
        return String.valueOf(getDateLong(dtName, req));
    }


    private static String zeroFormat(int i) {
        String num = String.valueOf(i);
        if(i > -1 && num.length() == 1)
            num = "0" + i;
        return num;
    }

    public static Date getTime(String dtName, HttpServletRequest req) {
        Date dt = null;
        try {
            String hr = (((String)req.getParameter(dtName + "_hr"))==null) ? "0" : ((String)(req.getParameter(dtName + "_hr"))) ;
            String mi = (((String)req.getParameter(dtName + "_mi"))==null) ? "0" : ((String)(req.getParameter(dtName + "_mi"))) ;
            String sec = (((String)req.getParameter(dtName + "_sec"))==null) ? "0" : ((String)(req.getParameter(dtName + "_sec"))) ;

            dt = Validator.composeTime(hr, mi, sec);
        }catch(Exception e) {
            //System.out.println("Invalid getTime() : " + e.toString());
        }
        return dt;
    }


    public static final int COMPLETE = 0;
    public static final int MONTH_YEAR = 1;
    public static final int YEAR = 2;


    /**
     * this method used to create controlDate that handle by textbox
     */
    public static String drawDateTextBox(String dtName, Date dtInit, String cssClass, int dateSequent){
        String result = "";

        int mmValue = 0;
        int ddValue = 0;
        int yyValue = 0;
        if(dtInit!=null){
            ddValue = Validator.getIntDate(dtInit);
            mmValue = Validator.getIntMonth(dtInit);
            yyValue = Validator.getIntYear(dtInit);
        }else{
            Date newDate = new Date();
            ddValue = Validator.getIntDate(newDate);
            mmValue = Validator.getIntMonth(newDate);
            yyValue = Validator.getIntYear(newDate);
        }

        String ddCtrlName = dtName + "_dy";
        String mmCtrlName = dtName + "_mn";
        String yyCtrlName = dtName + "_yr";

        // textbox for date
        String strTextDt = "<input name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";

        // textbox for month
        String strTextMn = "<input name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";

        // textbox for year
        String strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";

        switch(dateSequent){
            case SEQUENT_MDY :
                result = strTextMn + strTextDt + strTextYr;
                break;

            case SEQUENT_DMY :
                result = strTextDt + strTextMn + strTextYr;
                break;
        }

        return result;
    }

    public static String drawDateTextBox(String dtName, Date dtInit, String cssClass, int dateSequent, int typeDate){
        String result = "";

        int mmValue = 0;
        int ddValue = 0;
        int yyValue = 0;
        if(dtInit!=null){
            ddValue = Validator.getIntDate(dtInit);
            mmValue = Validator.getIntMonth(dtInit);
            yyValue = Validator.getIntYear(dtInit);
        }else{
            Date newDate = new Date();
            ddValue = Validator.getIntDate(newDate);
            mmValue = Validator.getIntMonth(newDate);
            yyValue = Validator.getIntYear(newDate);
        }

        String ddCtrlName = dtName + "_dy";
        String mmCtrlName = dtName + "_mn";
        String yyCtrlName = dtName + "_yr";

        // textbox for date
        String strTextDt = "";
        // textbox for month
        String strTextMn = "";
        // textbox for year
        String strTextYr = "";

        switch(typeDate){
            case COMPLETE:
                strTextDt = "<input name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";
                strTextMn = "<input name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";
                strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";
                break;
            case MONTH_YEAR:
                strTextDt = "<input type=\"hidden\" name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";
                strTextMn = "<input name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";
                strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";
                break;
            case YEAR:
                strTextDt = "<input type=\"hidden\" name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";
                strTextMn = "<input type=\"hidden\" name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";
                strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";
                break;
        }


        switch(dateSequent){
            case SEQUENT_MDY :
                result = strTextMn + strTextDt + strTextYr;
                break;

            case SEQUENT_DMY :
                result = strTextDt + strTextMn + strTextYr;
                break;
        }

        return result;
    }

    public static String drawDateTextBox(String dtName, Date dtInit, String cssClass, int dateSequent, int typeDate,String att){
        String result = "";

        int mmValue = 0;
        int ddValue = 0;
        int yyValue = 0;
        if(dtInit!=null){
            ddValue = Validator.getIntDate(dtInit);
            mmValue = Validator.getIntMonth(dtInit);
            yyValue = Validator.getIntYear(dtInit);
        }else{
            Date newDate = new Date();
            ddValue = Validator.getIntDate(newDate);
            mmValue = Validator.getIntMonth(newDate);
            yyValue = Validator.getIntYear(newDate);
        }

        String ddCtrlName = dtName + "_dy";
        String mmCtrlName = dtName + "_mn";
        String yyCtrlName = dtName + "_yr";

        // textbox for date
        String strTextDt = "";
        // textbox for month
        String strTextMn = "";
        // textbox for year
        String strTextYr = "";

        switch(typeDate){
            case COMPLETE:
                strTextDt = "<input name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";
                strTextMn = "<input name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";
                strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";
                break;
            case MONTH_YEAR:
                strTextDt = "<input type=\"hidden\" name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";
                strTextMn = "<input name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";
                strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";
                break;
            case YEAR:
                strTextDt = "<input type=\"hidden\" name=\""+ddCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(ddValue,2)+"\">&nbsp;";
                strTextMn = "<input type=\"hidden\" name=\""+mmCtrlName+"\" class=\""+cssClass+"\" size=\"2\" maxlength=\"2\" value=\""+generateStrFromInt(mmValue,2)+"\">&nbsp;";
                strTextYr = "<input name=\""+yyCtrlName+"\" class=\""+cssClass+"\" size=\"4\" maxlength=\"4\" value=\""+generateStrFromInt(yyValue,4)+"\">&nbsp;";
                break;
        }


        switch(dateSequent){
            case SEQUENT_MDY :
                result = strTextMn + strTextDt + strTextYr;
                break;

            case SEQUENT_DMY :
                result = strTextDt + strTextMn + strTextYr;
                break;
        }

        return result;
    }

    /**
     * generate string value of int
     */
    private static String generateStrFromInt(int value, int digitInto){
        String result = String.valueOf(value);
        if(result.length()<digitInto){
            int strLen = result.length();
            String tmpResult = "";
            for(int i=0; i<(digitInto-strLen); i++){
                tmpResult = "0"+tmpResult;
            }
            result = tmpResult + result;
        }
        return result;
    }

    public static String drawDateMY(String dtName, Date dtInit, String mnFormat,
    String cssClass, int mailstone, int range) {
        if(dtInit == null) dtInit = new Date();
        if(dtName == null) dtName = new String("dt");


        String mmCtrlName = dtName + "_mn";
        String yyCtrlName = dtName + "_yr";
        String ddCtrlName = dtName + "_dy";
        System.out.println("dtInit "+dtInit);
        int startYear	= Validator.getIntYear(dtInit);
        int endYear = Validator.getIntYear(dtInit) + 1;

        int mmValue = Validator.getIntMonth(dtInit);
        int yyValue = Validator.getIntYear(dtInit);
        System.out.println("yyValue "+yyValue);
        int yy = Validator.getIntYear(new Date());
        System.out.println("yy "+yy);
        if(range>=0){
            startYear = yy + mailstone;
            endYear = startYear + range;
        }else{
            endYear = yy + mailstone;
            startYear = endYear + range;
        }

                /*if(mailstone >= 0) {
                startYear = yy + mailstone;
            endYear = startYear + range;
            if(range < 0) {
                    endYear = yy + mailstone;
                startYear = endYear + range;
            }
        }else {
            endYear = yy + mailstone;
            startYear = endYear - range;
        }*/


        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>\n";
                break;

            case 4: // long name January, Febuary, ...
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>\n";
                break;
        } //end switch

        // day
        htmlSelect = htmlSelect + "\n<input type=\"hidden\" name=\"" + ddCtrlName + "\" value=\"1\" >\n";

        //year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\">\n";
        System.out.println("yyValue "+yyValue);
        System.out.println("startYear "+startYear);
        for(int i = startYear; i <= endYear; i++) {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }


    /**
     * generate draw data (Month and Year)
     * @param <CODE>dtName</CODE>control/component name
     * @param <CODE>dtInit</CODE>initial value of this control
     * @param <CODE>mnFormat</CODE>format month
     * @param <CODE>cssClass</CODE>css class used
     * @param <CODE>mailstone</CODE>start/mailstone year used for this control
     * @param <CODE>range</CODE>range(how many) year will display in this control
     * @param <CODE>attr</CODE>additional attribute used by this control i.e : javascript etc
     * @return HTML text of for generate combo MONTH and YEAR
     * @created by Edhy
     */
    public static String drawDateMY(String dtName, Date dtInit, String mnFormat,
    String cssClass, int mailstone, int range, String attr) {
        if(dtInit == null)
        {
            dtInit = new Date();
        }

        if(dtName == null)
        {
            dtName = new String("dt");
        }

        String mmCtrlName = dtName + "_mn";
        String yyCtrlName = dtName + "_yr";
        String ddCtrlName = dtName + "_dy";

        int startYear	= Validator.getIntYear(dtInit);
        int endYear = Validator.getIntYear(dtInit) + 1;

        int mmValue = Validator.getIntMonth(dtInit);
        int yyValue = Validator.getIntYear(dtInit);
        int yy = Validator.getIntYear(new Date());

        if(range>=0)
        {
            startYear = yy + mailstone;
            endYear = startYear + range;
        }
        else
        {
            endYear = yy + mailstone;
            startYear = endYear + range;
        }

        String htmlSelect = "<select name=\"" + mmCtrlName + "\" class=\""+cssClass+"\" "+attr+">\n";
        int monthFormatLength = mnFormat.length();
        String s = "";

        // month combobox
        switch(monthFormatLength) {
            case 2 : // number format
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\"" + s + " >" + zeroFormat(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>\n";
                break;

            case 4: // long name January, Febuary, ...
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getLongEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>\n";
                break;

                // case 3 or others, as default - short name, Jan, Feb, ...
            default :
                for(int i = 1; i <= 12; i++) {
                    s = (i == mmValue) ? "selected" :  "";
                    htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + YearMonth.getShortEngMonthName(i) + "</option>\n";
                }
                htmlSelect = htmlSelect + "</select>\n";
                break;
        } //end switch

        // day, default date used in 1
        htmlSelect = htmlSelect + "\n<input type=\"hidden\" name=\"" + ddCtrlName + "\" value=\"1\" >\n";

        // year combobox
        htmlSelect = htmlSelect  + "\n<select name=\"" + yyCtrlName + "\" class=\""+cssClass+"\" "+attr+">\n";
        System.out.println("yyValue "+yyValue);
        System.out.println("startYear "+startYear);
        for(int i = startYear; i <= endYear; i++)
        {
            s = (i == yyValue) ? "selected" :  "";
            htmlSelect = htmlSelect + "\t<option value=\"" + i + "\""+ s +">" + i + "</option>\n";
        }
        htmlSelect = htmlSelect + "</select>\n";

        return htmlSelect;
    }

    public static void main(String args[]){
        ExtendedControlDate cnt = new ExtendedControlDate();
        System.out.println("generateStrFromInt(51,2) : "+cnt.generateStrFromInt(51,2));
    }

//yang saya tambahkan


} // end of WP_ControlDate()