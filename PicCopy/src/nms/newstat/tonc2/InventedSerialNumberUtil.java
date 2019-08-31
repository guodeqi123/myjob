package nms.newstat.tonc2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Declan
 * @date 2019/08/31 17:36
 */
public class InventedSerialNumberUtil {


    private static Map<String, String> kuCunZuZhi2KuDaiMa = new HashMap<String, String>();


    private static List<String> randomStr = new ArrayList<String>();
    private static int index = 0;

    static {
        kuCunZuZhi2KuDaiMa.put("01", "A");
        kuCunZuZhi2KuDaiMa.put("0101", "B");
        kuCunZuZhi2KuDaiMa.put("010101", "C");
        kuCunZuZhi2KuDaiMa.put("010102", "D");
        kuCunZuZhi2KuDaiMa.put("0102", "E");

        for (int i = 1; i < 100000; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            String str = String.valueOf(i);
            for (int j = 0; j < 5-str.length(); j++) {
                stringBuilder.append("0");
            }
            stringBuilder.append(str);
            randomStr.add(stringBuilder.toString());
        }
    }


    /**
     * ��ȡ��ٺ�
     *  ����Ź����ⶨ�����£�Ϊ�����غ������������� ����13λ����Ŀǰ��˾������Ʒ�õ������11λ��
     *  ����Ź��򣺿����(3λ���Ź̶���+ �꣨������������λ����ʾ��+ �£�1~9ABC��һλ��+ �գ�01~31��λ��ʶ��+	��ˮ�ţ�5λ�������ֱ�ʾ��
     *  �ô���һĿ��Ȼ����ʶ�����ա��׶˾��ǣ��������ĺų��ˡ�

     * @param kuWeiZuZhi ��λ��֯
     * @param kuBie ���
     * @param kuWei ��λ
     * @return
     */
    public static String getInventedSerialNumber(String kuWeiZuZhi, String kuBie, String kuWei){

        StringBuilder retSerialNumberBuilder = new StringBuilder();
        retSerialNumberBuilder.append(getKuDaiMa(kuWeiZuZhi, kuBie, kuWei));
        retSerialNumberBuilder.append(getDateStr());
        retSerialNumberBuilder.append(get5RandomNumber());
        return retSerialNumberBuilder.toString();
    }
    /**
     * ��ȡ��ٺ�
     *  ����Ź����ⶨ�����£�Ϊ�����غ������������� ����13λ����Ŀǰ��˾������Ʒ�õ������11λ��
     *  ����Ź��򣺿����(3λ���Ź̶���+ �꣨������������λ����ʾ��+ �£�1~9ABC��һλ��+ �գ�01~31��λ��ʶ��+	��ˮ�ţ�5λ�������ֱ�ʾ��
     *  �ô���һĿ��Ȼ����ʶ�����ա��׶˾��ǣ��������ĺų��ˡ�

     * @param kuWeiZuZhi ��λ��֯
     * @param kuBie ���
     * @param kuWei ��λ
     * @param startIndex 5λ��ˮ��(00001~99999)����ʼ����
     * @return
     */
    public static String getInventedSerialNumber(String kuWeiZuZhi, String kuBie, String kuWei, int startIndex){
        if(startIndex>0){
            index = startIndex;
        }
        StringBuilder retSerialNumberBuilder = new StringBuilder();
        retSerialNumberBuilder.append(getKuDaiMa(kuWeiZuZhi, kuBie, kuWei));
        retSerialNumberBuilder.append(getDateStr());
        retSerialNumberBuilder.append(get5RandomNumber());
        return retSerialNumberBuilder.toString();
    }

    private static String getKuDaiMa(String kuWeiZuZhi, String kuBie, String kuWei){
        StringBuilder kuBieStrBuilder = new StringBuilder();
        if(!kuCunZuZhi2KuDaiMa.containsKey(kuWeiZuZhi)){
            throw new RuntimeException("�����֯�Ҳ���");
        }
        kuBieStrBuilder.append(kuCunZuZhi2KuDaiMa.get(kuWeiZuZhi));
        if(kuBie==null || kuBie.equals("") || kuBie.length()>3 || kuBie.length()<2){
            throw new RuntimeException("����Ŀ�����"+kuBie);
        }
        if(kuBie.length()<3){
            kuBie = "0" + kuBie;
        }
        kuBieStrBuilder.append(kuBie);
        return kuBieStrBuilder.toString();
    }


    private static String getDateStr(){
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);

        stringBuilder.append(String.valueOf(year).substring(2));
        if(month>9){
            String strMonth = month==10 ? "A" : month==11 ? "B" : "C";
            stringBuilder.append(strMonth);
        }else{
            stringBuilder.append(String.valueOf(month));
        }

        String strDay = day>9 ? String.valueOf(day) : "0"+day;
        stringBuilder.append(strDay);

        return stringBuilder.toString();
    }


    private static String get5RandomNumber(){
        String random = randomStr.get(index);
        index++;
        return random;
    }


    public static void main(String[] args) {
        String kuCunZuZhi = "010102";
        String kuBie = "66";
        String kuWei = "01";
        String inventedSerialNumber = getInventedSerialNumber(kuCunZuZhi, kuBie, kuWei, 2);
        String inventedSerialNumber1 = getInventedSerialNumber(kuCunZuZhi, kuBie, kuWei);
        System.out.println(inventedSerialNumber);
        System.out.println(inventedSerialNumber1);

    }




}
