package com.jsti.tunnel.monitor.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow; 

public class WordUtil {
	
    public static ByteArrayOutputStream exportWordStream(String templateUrl,Map<String, Object> textMap) { 
		try {
			XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(templateUrl)); 
			//解析替换文本段落对象
        	changePlaceholderByValue(document, textMap); 
        	changeTable(document, textMap); 
        	
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();//二进制OutputStream
        	document.write(baos);//文档写入流 
        	baos.flush();
        	baos.close();
            return baos; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null; 
    }
    
    
    /**
             *  替换段落文本
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     */
    public static void changePlaceholderByValue(XWPFDocument document, Map<String, Object> textMap){
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if(checkIsPlaceholder(text)){
            	if(text.indexOf("picture")>0){
            	}else{
            		List<XWPFRun> runs = paragraph.getRuns();
            		for (XWPFRun run : runs) {
            			//替换模板原来位置
            			run.setText(change2Value(run.toString(), textMap),0);
            		}
            		
            	}
            }
        }
        
    }
    
    
    /**
     * 替换表格对象方法
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合 
     */
    public static void changeTable(XWPFDocument document, Map<String, Object> textMap){
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格，且不循环表头
            XWPFTable table = tables.get(i);
            if(table.getRows().size()>1){
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if(checkIsPlaceholder(table.getText())){
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(document,rows, textMap);
                }else{
                    //insertTable(table, tableList);
                }
            }
        }
    }
    
    
    /**
     * 遍历表格
     * @param rows 表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(XWPFDocument document,List<XWPFTableRow> rows ,Map<String, Object> textMap){
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if(checkIsPlaceholder(cell.getText())){
                 
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) { 
                            run.setText(change2Value(run.toString(), textMap),0);
                        }
                    }
                }
            }
        }
    }
    
    
    /**
            *  判断文本中时候包含$
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkIsPlaceholder(String text){
        boolean check  =  false;
        if(null == text || text.indexOf("$") != -1){
            check = true;
        }
        return check;
        
    }
    
    
    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String change2Value(String value, Map<String, Object> textMap){
        Set<Entry<String, Object>> textSets = textMap.entrySet();
        for (Entry<String, Object> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${"+textSet.getKey()+"}";
            if(value.indexOf(key)!= -1){
            	

				if(textSet.getValue() instanceof Date) {
					DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
					value = dFormat.format(textSet.getValue());
				}
				else 
					value = (textSet.getValue()== null)? "" : String.valueOf(textSet.getValue());
                
                
                break;
            }
        }
        //模板未匹配到区域替换为空
        if(checkIsPlaceholder(value)){
            value = "";
        }
        return value;
    }
    
}
