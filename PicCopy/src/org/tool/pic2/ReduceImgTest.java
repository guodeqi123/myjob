package org.tool.pic2;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ReduceImgTest {
    /**
     * ָ��ͼƬ��Ⱥ͸߶Ⱥ�ѹ��������ͼƬ����ѹ��
     * 
     * @param imgsrc
     *            ԴͼƬ��ַ
     * @param imgdist
     *            Ŀ��ͼƬ��ַ
     * @param widthdist
     *            ѹ����ͼƬ�Ŀ��
     * @param heightdist
     *            ѹ����ͼƬ�ĸ߶�
     * @param rate
     *            ѹ���ı���
     */
    public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist, Float rate) {
        try {
            File srcfile = new File(imgsrc);
            // ���ͼƬ�ļ��Ƿ����
            if (!srcfile.exists()) {
                System.out.println("�ļ�������");
            }
            // ���������Ϊ����˵���ǰ�����ѹ��
            if (rate != null && rate > 0) {
                //���ԴͼƬ�Ŀ�ߴ���������
                int[] results = getImgWidthHeight(srcfile);
                if (results == null || results[0] == 0 || results[1] == 0) {
                    return;
                } else {
                    //���������Ż�����ͼƬ��С����������תΪ����
                    widthdist = (int) (results[0] * rate);
                    heightdist = (int) (results[1] * rate);
                }
            }
            // ��ʼ��ȡ�ļ�������ѹ��
            Image src = ImageIO.read(srcfile);

            // ����һ������ΪԤ����ͼ������֮һ�� BufferedImage
            BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

            //����ͼ��  getScaledInstance��ʾ������ͼ������Ű汾������һ���µ����Ű汾Image,��ָ����width,height����ͼ��
            //Image.SCALE_SMOOTH,ѡ��ͼ��ƽ���ȱ������ٶȾ��и������ȼ���ͼ�������㷨��
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);

            //�����ļ������
            FileOutputStream out = new FileOutputStream(imgdist);
            //��ͼƬ��JPEGѹ�������浽out��
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            //�ر��ļ������
            out.close();
        } catch (Exception ef) {
            ef.printStackTrace();
        }
    }

    /**
     * ��ȡͼƬ��Ⱥ͸߶�
     * 
     * @param ͼƬ·��
     * @return ����ͼƬ�Ŀ��
     */
    public static int[] getImgWidthHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = { 0, 0 };
        try {
            // ����ļ�������
            is = new FileInputStream(file);
            // �����ｫͼƬд�뻺��ͼƬ��
            src = ImageIO.read(is);
            result[0] =src.getWidth(null); // �õ�ԴͼƬ��
            result[1] =src.getHeight(null);// �õ�ԴͼƬ��
            is.close();  //�ر�������
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
//        String sPath = "D:/GG/2/prod/tmpDir/��Ҫ��2343.jpg";
//        String dPath = "D:/GG/2/prod/tmpDir/��Ҫ��2343_r.jpg";
//        File srcfile = new File(sPath); 
//        System.out.println("ѹ��ǰͼƬ��С��" + srcfile.length());
//        reduceImg(sPath, dPath, 260, 390, null);
//        
//        File distfile = new File(dPath);
//        System.out.println("ѹ����ͼƬ��С��" + distfile.length());
    	
    	System.out.println( "41052619898080033".length() );
    	System.out.println( "410403198312275526".length() );

    }
}