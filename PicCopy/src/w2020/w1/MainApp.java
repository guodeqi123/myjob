package w2020.w1;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;

import w2020.FUtil;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainApp {


    public static final String basePath = "D:/GG/t4";
    public static String tempPath = basePath + "/template2.xls";

    public static void main( String[] args ){

        loadOneResume( tempPath );

    }



    private static Resume3 loadOneResume( String path ) {

        Resume3 resume = new Resume3();


        Workbook workbook = FUtil.getWB(path  );
        //��ȡָ��sheet
        Sheet rsheet = workbook.getSheetAt(0);

//        Row row = rsheet.getRow(1);
//        Cell nameCell = row.getCell(2);
//        String tttt = EAComparator.getCellValueByCell(nameCell);

        Set<Map.Entry<String, String>> entries = Resume3.proNameToPos.entrySet();
        for(  Map.Entry<String, String> en :  entries ){
            String proName = en.getKey();
            String value = en.getValue();
            String[] split = value.split(",");
            int trow = Integer.parseInt(split[0]) -1 ;
            int tcol = Integer.parseInt(split[1]) -1 ;

            setProerties( resume , proName , rsheet , trow  , tcol   );
        }


        System.out.print( resume  );

        return resume;
    }

    private static void setProerties(Resume3 bean, String proName, Sheet sheet, int rolNum, int colNum ) {

        try {
            Row row = sheet.getRow(rolNum);
            Cell tCell = row.getCell(colNum);

            String tValue =  getCellValueByCell(tCell);

            if( isDatePro(proName) && !StringUtils.isEmpty(tValue) ){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                double value = tCell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                tValue = sdf.format(date);
            }

            Field tmpField = getFieldByAliase(Resume3.class , proName);
            String fieldname = tmpField.getName();
            String readMname =  "get" + capitalize(fieldname);
            String writeMname =   "set" + capitalize(fieldname);
            PropertyDescriptor pd = new PropertyDescriptor(fieldname, bean.getClass() , readMname , writeMname );
            Method setter = pd.getWriteMethod();
            setter.invoke(bean, tValue ) ;
        }catch (Exception e) {
            System.out.print( "error cell :  " + rolNum  + " , " +  colNum  );
            e.printStackTrace();
        }
    }

    private static boolean isDatePro(String proName) {

        if(    "birthDay".equals( proName ) || "graTime".equals( proName ) || "hsInTime".equals( proName ) ||
                "hsOutTime".equals( proName ) || "rccInTime".equals( proName ) || "rccOutTime".equals( proName )
                || "msInTime".equals( proName ) || "msOutTime".equals( proName )
        ){
            return false;
        }
        return false;

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

    public static String getCellValueByCell(Cell cell) {
        //�ж��Ƿ�Ϊnull��մ�
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        short format = cell.getCellStyle().getDataFormat();
        String cellValue = "";
        int cellType=cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC: // ����
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    //System.out.println("cell.getCellStyle().getDataFormat()="+cell.getCellStyle().getDataFormat());
                    if (format == 20 || format == 32) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else if (format == 14 || format == 31 || format == 57 || format == 58  ) {
                        // �����Զ������ڸ�ʽ��m��d��(ͨ���жϵ�Ԫ��ĸ�ʽid�����id��ֵ��58)
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil
                                .getJavaDate(value);
                        cellValue = sdf.format(date);
                    }else {// ����
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                    try {
                        cellValue = sdf.format(cell.getDateCellValue());// ����
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
                    cellValue = bd.toPlainString();// ��ֵ ������BigDecimal��װ�ٻ�ȡplainString�����Է�ֹ��ȡ����ѧ����ֵ
                }
                break;
            case Cell.CELL_TYPE_STRING: // �ַ���
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue()+"";;
                break;
            case Cell.CELL_TYPE_FORMULA: // ��ʽ
                cellValue = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_BLANK: // ��ֵ
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: // ����
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
}



