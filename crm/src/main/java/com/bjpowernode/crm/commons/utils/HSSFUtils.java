package com.bjpowernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-18 20:13
 */
public class HSSFUtils {
    /**
     * 获取cellValue
     * @param cell
     * @return
     *//**//*
    public static String getCellValueForStr(HSSFCell cell) {
        String ret = "";
        if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            ret = cell.getCellFormula();
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            ret = cell.getCellFormula();
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){
            ret = cell.getBooleanCellValue()+"";
        }else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            ret = cell.getNumericCellValue()+"";
        }else {
            ret = "";
        }

        ret*//*urn ret;
    }*/
    /**
     * 从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String ret="";
        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            ret=cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            ret=cell.getNumericCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            ret=cell.getBooleanCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            ret=cell.getCellFormula();
        }else{
            ret="";
        }

        return ret;
    }
}
