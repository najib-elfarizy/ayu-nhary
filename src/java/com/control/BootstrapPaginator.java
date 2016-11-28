/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.control;

import com.dimata.util.Command;

/**
 *
 * @author xStudio
 */
public class BootstrapPaginator {

    private String listFirstCommand;
    private String listPrevCommand;
    private String listNextCommand;
    private String listLastCommand;
    private String pagingInfo;

    public BootstrapPaginator() {
        initDefault();
    }

    private void initDefault() {
        listPrevCommand = "javascript:cmdListPrev()";
        listNextCommand = "javascript:cmdListNext()";
        listFirstCommand = "javascript:cmdListFirst()";
        listLastCommand = "javascript:cmdListLast()";
    }

    public String getPagingInfo() {
        return pagingInfo;
    }

    public String render(int iCommand, int vectsize, int start, int maxtoget) {
        StringBuilder str = new StringBuilder();
        if (vectsize > 0) {
            if (vectsize > maxtoget) {

                //  if( iCommand==Command.LIST){
                if (start <= 0) {
                    iCommand = Command.FIRST;
                } else {
                    if ((start + maxtoget) >= vectsize) {
                        iCommand = Command.LAST;
                    }
                }
                //   }

                str.append("<ul class=\"pagination\">");

                int noOfPages = (int) Math.ceil(vectsize * 1.0 / maxtoget);
                for (int i = 0; i < noOfPages; i++) {

                }
                switch (iCommand) {
                    case Command.LIST:
                        pagingInfo = "List " + (start + 1) + " to " + (start + maxtoget) + " of " + vectsize + " rows";

                        str.append("<li><a href=\"" + listFirstCommand + "\">First</a></li>");
                        str.append("<li><a href=\"" + listPrevCommand + "\">Prev</a></li>");
                        str.append("<li><a href=\"" + listNextCommand + "\">Next</a></li>");
                        str.append("<li><a href=\"" + listLastCommand + "\">Last</a></li>");

                        break;
                    case Command.FIRST:
                        pagingInfo = "List " + (start + 1) + " to " + (start + maxtoget) + " of " + vectsize + " rows";

                        str.append("<li class=\"active\"><a>First</a></li>");
                        str.append("<li class=\"active\"><a>Prev</a></li>");
                        str.append("<li><a href=\"" + listNextCommand + "\">Next</a></li>");
                        str.append("<li><a href=\"" + listLastCommand + "\">Last</a></li>");

                        break;
                    case Command.LAST:
                        if ((vectsize % maxtoget) > 0) {
                            start = vectsize - (vectsize % maxtoget);
                        }
                        pagingInfo = "List " + (start + 1) + " to " + (vectsize) + " of " + vectsize + " rows";

                        str.append("<li><a href=\"" + listFirstCommand + "\">First</a></li>");
                        str.append("<li><a href=\"" + listPrevCommand + "\">Prev</a></li>");
                        str.append("<li class=\"active\"><a>Next</a></li>");
                        str.append("<li class=\"active\"><a>Last</a></li>");

                        break;
                    case Command.PREV:
                        if ((start - maxtoget) < 0) {
                            pagingInfo = "List " + (start + 1) + " to " + (start + maxtoget) + " of " + vectsize + " rows";

                            str.append("<li class=\"active\"><a>First</a></li>");
                            str.append("<li class=\"active\"><a>Prev</a></li>");
                            str.append("<li><a href=\"" + listNextCommand + "\">Next</a></li>");
                            str.append("<li><a href=\"" + listLastCommand + "\">Last</a></li>");
                        } else {
                            //start = start-maxtoget;
                            pagingInfo = "List " + (start + 1) + " to " + (start + maxtoget) + " of " + vectsize + " rows";

                            str.append("<li><a href=\"" + listFirstCommand + "\">First</a></li>");
                            str.append("<li><a href=\"" + listPrevCommand + "\">Prev</a></li>");
                            str.append("<li><a href=\"" + listNextCommand + "\">Next</a></li>");
                            str.append("<li><a href=\"" + listLastCommand + "\">Last</a></li>");
                        }
                        break;
                    case Command.NEXT:
                        if ((start + maxtoget) >= vectsize) {
                            if ((vectsize % maxtoget) > 0) {
                                start = vectsize - (vectsize % maxtoget);
                            }
                            pagingInfo = "List " + (start + 1) + " to " + (vectsize) + " of " + vectsize + " rows";

                            str.append("<li><a href=\"" + listFirstCommand + "\">First</a></li>");
                            str.append("<li><a href=\"" + listPrevCommand + "\">Prev</a></li>");
                            str.append("<li class=\"active\"><a>Next</a></li>");
                            str.append("<li class=\"active\"><a>Last</a></li>");
                        } else {
                            pagingInfo = "List " + (start + 1) + " to " + (start + maxtoget) + " of " + vectsize + " rows";

                            str.append("<li><a href=\"" + listFirstCommand + "\">First</a></li>");
                            str.append("<li><a href=\"" + listPrevCommand + "\">Prev</a></li>");
                            str.append("<li><a href=\"" + listNextCommand + "\">Next</a></li>");
                            str.append("<li><a href=\"" + listLastCommand + "\">Last</a></li>");
                        }
                        break;
                }

                str.append("</ul>");
                return str.toString();
            } else {
                pagingInfo = "List " + (start + 1) + " to " + (start + maxtoget) + " of " + vectsize + " rows";
                return str.toString();
            }
        }
        pagingInfo = "No data found";
        return "";
    }
}
