/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.control;

/**
 *
 * @author No Name
 */

import javax.servlet.http.*;


public class ExtendedControlRadio
{

    /**
       Table Dimention :
     		-> rows, cols

		Radio Button :
        	-> name, value, caption, checked
     */
    //public static String draw(String name, String initVal, int size, int maxChar)
	public static String draw()
    {
        /*
        if(name == null) name = new String("txt_input");
        String sz = "";
        String mc = "";
          */

        int cols = 3;
        int rows = 4;

        String strHtml = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n";

/*
		  <tr>
		    <td><input type="radio" name="radiobutton" value="radiobutton" checked> sdfdsf </td>
		    <td><input type="radio" name="radiobutton" value="radiobutton">sdfdsf </td>
		    <td><input type="radio" name="radiobutton" value="radiobutton">sdfdsf </td>
		  </tr>
*/
		for(int i = 0; i < rows; i++) {
                strHtml = strHtml + "  <tr>\n";
			for(int j = 0; j < cols; j++) {
            	strHtml = strHtml + "    <td><input type=\"radio\" name=\"radiobutton\" value=\"radiobutton\" checked>" + j + "</td>\n";
	        } // end for cols
			strHtml = strHtml + "  </tr>\n";
        } //end for rows

        return strHtml;
    } // end draw


    /**
     * Draw a string of radio button groupp
     * @param name String name of radio
     * @param initValue String initial value to checkeding
     * @param keys String[] keys to display at radio
     * @param vals String[] values to radio
     */
    public static String draw(String name, String initValue, String[] keys, String[] vals){
        String html="<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n<tr><td>";
        for(int i=0;i<keys.length;i++){
            String key="";
            String val="";
            try{
                key=keys[i];
                val=vals[i];
            }catch(NullPointerException e){}
            String selected=(val.equals(initValue)?"checked":"");
            html+="<label><input type=\"radio\" name=\""+name+"\" value=\""+val+"\" "+selected+">"+key;
            html+="</label>";
        }
        html+="</td></tr></table>";
        return html;
    }


    public static String getStr(String name, HttpServletRequest req) {
        String value = "";
        try {
	        value = req.getParameter(name);
        }catch(Exception e) {
        }
        return value;
    }

}

