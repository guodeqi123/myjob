package org.tool.doc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


/**
 * 
 * @author Guodeqi
 *
 */
public class MainAppDoc extends JFrame {
	
	public MainAppDoc thisFrame = this;
	
	private JTextField excelPath = null;
	private JFileChooser excelChooser = null;
	private JButton selectExcelBtn;
	
	private JButton beginParseBtn;
	
	
	public MainAppDoc(){
		
		initUI();
		initAction();
		this.setTitle("Excel");
		this.setSize(800	,  200 );
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
	}
	
	
	public void initUI(){
		
		Container contentPane = this.getContentPane();
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.setBorder(BorderFactory.createTitledBorder(" "));
		excelPath = new JTextField("E:\\ceshi22222222222\\照片.xlsx");
		excelPath.setPreferredSize(new Dimension( 400 , 30 ));
		excelChooser = new JFileChooser();
		excelChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		excelChooser.setDialogTitle("选择Word文件路径");
		selectExcelBtn = new JButton("选择Word");
		beginParseBtn = new JButton("转换");
		
		topPanel.add(excelPath);
		topPanel.add(selectExcelBtn);
		topPanel.add(beginParseBtn);
		topPanel.setPreferredSize(new Dimension(0, 70));
		contentPane.add(topPanel , BorderLayout.NORTH);
		
		
		
	}
	
	public void initAction(){
		selectExcelBtn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int showOpenDialog = excelChooser.showOpenDialog(thisFrame);
				if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
					String path = excelChooser.getSelectedFile().getParent();
					String filename=excelChooser.getSelectedFile().getName();
					excelPath.setText( excelChooser.getSelectedFile().getAbsolutePath() );
				}
			}
		});
		
		beginParseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				final String excelFile = excelPath.getText().trim();
				if( excelFile==null || excelFile.length()==0 ){
					return ;
				}
				
				Thread thread = new Thread(){
					@Override
					public void run() {
						WordToExcel.wordToExcel(excelFile , "");
						JOptionPane.showMessageDialog(null, "完成");
					}
				};
				thread.start();
				
			}
		});
		
	}
	
	
	
	public String getExcelPath(){
		return excelPath.getText().trim();
	}
	
	
	public void setFinished(  ){
		JOptionPane.showMessageDialog(thisFrame, "拷贝完成!");
	}
	
	public void appendLog( String log ){}
	
	public void appendErrorLog( String log ){}
	
	public static Properties pro = new Properties();
	
	public static void main(String[] args) {
		
		try {
			FileInputStream fis = new FileInputStream(new File("conf/conf.properties"));
			pro.load( fis );
			fis.close();
			
			String property = pro.getProperty("titleSEP");
			if( property != null ){
				WordToExcel.titleSEP = property;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		MainAppDoc mainApp = new MainAppDoc();
		
		mainApp.setVisible(true);
		
	}
	
	
}
