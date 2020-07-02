package w2020;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FUtil {

    public static Workbook getWB(String fpath){
        Workbook wb = null;
        try {
            File file = new File( fpath );
            InputStream input = new FileInputStream(file);
            String fileExt = file.getName().substring( file.getName().lastIndexOf(".") + 1);
            if ("xls".equalsIgnoreCase(fileExt)) {
                wb = (HSSFWorkbook) WorkbookFactory.create(input);
            } else {
                wb = new XSSFWorkbook(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Workbook getWB(File fff){
        Workbook wb = null;
        try {
            InputStream input = new FileInputStream(fff);
            String fileExt = fff.getName().substring( fff.getName().lastIndexOf(".") + 1);
            if ("xls".equalsIgnoreCase(fileExt)) {
                wb = (HSSFWorkbook) WorkbookFactory.create(input);
            } else {
                wb = new XSSFWorkbook(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }


    public static String getCellValueByCell(Cell cell) {
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        short format = cell.getCellStyle().getDataFormat();
        String cellValue = "";
        int cellType=cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    //System.out.println("cell.getCellStyle().getDataFormat()="+cell.getCellStyle().getDataFormat());
                    if (format == 20 || format == 32) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else if (format == 14 || format == 31 || format == 57 || format == 58  ) {
                        // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil
                                .getJavaDate(value);
                        cellValue = sdf.format(date);
                    }else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                    try {
                        cellValue = sdf.format(cell.getDateCellValue());// 日期
                    } catch (Exception e) {
                        try {
                            throw new Exception("exception on get date data !".concat(e.toString()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }finally{
                        sdf = null;
                    }
                }  else {
                    BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                    cellValue = bd.toPlainString();// 数值 这种用BigDecimal包装再获取plainString，可以防止获取到科学计数值
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue()+"";;
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "ERROR VALUE";
                break;
            default:
                cellValue = "UNKNOW VALUE";
                break;
        }
        return cellValue;
    }


    public static <T> T deepClone(T srcObj) throws IOException, ClassNotFoundException  {
        if (srcObj == null) {
            return null;
        }

        T newObj = null;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;

        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(srcObj);
            bi = new ByteArrayInputStream(bo.toByteArray());
            oi = new ObjectInputStream(bi);
            newObj = (T) oi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        }finally{
            try {
                oo.close();
                bo.close();
                bi.close();
                oi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newObj ;
    }


    public static void setStrValue(T bean , String tValue , String proName){

        try {
            Field tmpField = getFieldByAliase(bean.getClass() , proName);
            String fieldname = tmpField.getName();
            String readMname =  "get" + capitalize(fieldname);
            String writeMname =   "set" + capitalize(fieldname);
            PropertyDescriptor pd = new PropertyDescriptor(fieldname, bean.getClass() , readMname , writeMname );
            Method setter = pd.getWriteMethod();
            setter.invoke(bean, tValue ) ;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getFieldByAliase(Class tmpClass , String alias) {

        String proName = alias.toLowerCase();

        for(  ; !tmpClass.equals(Object.class) ; tmpClass = tmpClass.getSuperclass() ){
            Field[] fields = tmpClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().toLowerCase().equals(proName.toLowerCase())) {
                    return field;
                }
            }
        }
        return null;
    }

    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }



    private static void copyFile(File srcFile, String dstFullPath ) {
        try {
            FileUtils.copyFile( srcFile , new File(dstFullPath));
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> loadTxt(String txtpath ) {
        List<String> ret = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(txtpath) ) );
            String s = null;
            while ((s = br.readLine()) != null) {
                if( StringUtils.isEmpty(s) ){
                    continue;
                }
                ret.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static void cleanDir(String dirName ) {
        File dirFile = new File(dirName);
        try {
            if( dirFile.exists() ){
                FileUtils.deleteDirectory(dirFile);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
        dirFile.mkdirs();
    }



    public static void loadExcelDemo(){
        Workbook workbook = getWB(""  );
        Sheet rsheet = workbook.getSheetAt(1);
        int lastRowNum = rsheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            Row row =  rsheet.getRow(i);
            if(row ==null){
                continue;
            }
            Cell orderCell = row.getCell(1);
            String order = getCellValueByCell( orderCell );
        }
    }
    private static void writeExcelDemo( List list ) {

        XSSFWorkbook wb = (XSSFWorkbook) getWB( "tmpPath" );
        Sheet wsheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        for(   int i=0;  i<list.size()  ; i++ ){

            Object tmpP = list.get(i);
            String sep = (i+1) + "";

            Row wrow = wsheet.createRow( (1+i) );

            Cell wcell = wrow.createCell(0);
            wcell.setCellValue( sep );

            wcell = wrow.createCell(1);
            wcell.setCellValue( "" );

        }

        writeExcel(wb  , "toFileName");
    }

    public static void writeExcel(XSSFWorkbook wb , String fileName) {
        FileOutputStream fout = null;
        try {
            File file = new File(fileName);
            if( file.exists()){
                file.delete();
            }
            file.createNewFile();
            fout = new FileOutputStream(file);
            wb.write(fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
