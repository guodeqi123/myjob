package org.tool.piccopy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class MainApp extends JFrame {
	
	public MainApp thisFrame = this;
	
	private JTextField excelPath = null;
	private JFileChooser excelChooser = null;
	private JButton selectExcelBtn;
	
	private JTextField picDirPath = null;
	private JFileChooser picDirChooser = null;
	private JButton selectPicDirBtn;
	private JButton addPicDirBtn;
	private JButton removePicDirBtn;
	
	private JTable picDirTable = null;
	String[] headers = { "图片目录"}; 
	Object[][] a = new Object[][]{ { "E:\\ceshi22222222222\\picDir1" } , {  "E:\\ceshi22222222222\\picDir2" } };
	DefaultTableModel tableModel = new DefaultTableModel( a , headers);
	
	private JButton beginParseBtn;
	private JProgressBar process = null;
	
	JScrollPane logScroll = new JScrollPane( );
	private JTextArea logArea = null;
	
	public MainApp(){
		
		
		initUI();
		initAction();
		this.setTitle("图片自动分类");
		this.setSize(800	,  500 );
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
		excelChooser.setDialogTitle("选择Excel文件路径");
		selectExcelBtn = new JButton("选择Excel");
		topPanel.add(excelPath);
		topPanel.add(selectExcelBtn);
		topPanel.setPreferredSize(new Dimension(0, 70));
		contentPane.add(topPanel , BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder(" "));
		picDirChooser = new JFileChooser();
		picDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		picDirChooser.setDialogTitle("选择图片路径");
		JPanel centerPanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		picDirPath = new JTextField();
		picDirPath.setPreferredSize(new Dimension( 400 , 30 ));
		selectPicDirBtn = new JButton("选择图片目录");
		addPicDirBtn = new JButton("添加图片目录");
		removePicDirBtn = new JButton("移除图片目录");
		centerPanelTop.add(picDirPath);
		centerPanelTop.add(selectPicDirBtn);
		centerPanelTop.add(addPicDirBtn);
		centerPanelTop.add(removePicDirBtn);
		centerPanel.add(centerPanelTop , BorderLayout.NORTH );
		picDirTable = new JTable();
		picDirTable = new JTable(tableModel);
		JScrollPane tableScroll = new JScrollPane(picDirTable);
		centerPanel.add(tableScroll , BorderLayout.CENTER);
		//日志
		JPanel logPanel = new JPanel( new BorderLayout() );
		logPanel.setBorder(BorderFactory.createTitledBorder("Logs"));
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		logScroll.setViewportView(logArea);
		logPanel.add(logScroll);
		logPanel.setPreferredSize(new Dimension( 380 ,0  ));
		centerPanel.add(logPanel , BorderLayout.EAST);
		contentPane.add(centerPanel);
		
		JPanel bottomPanel = new JPanel( new FlowLayout(FlowLayout.LEFT) );
		process = new JProgressBar();
		process.setPreferredSize(new Dimension( 400 , 30 ));
		beginParseBtn = new JButton("开始");
		bottomPanel.add(process);
		bottomPanel.add(beginParseBtn);
		bottomPanel.setPreferredSize(new Dimension( 0 , 40 ));
		contentPane.add(bottomPanel , BorderLayout.SOUTH);
		
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
		
		selectPicDirBtn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int showOpenDialog = picDirChooser.showOpenDialog(thisFrame);
				if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
					String path = picDirChooser.getSelectedFile().getParent();
					String filename=picDirChooser.getSelectedFile().getName();
					picDirPath.setText( picDirChooser.getSelectedFile().getAbsolutePath() );
				}
			}
		});
		
		addPicDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = picDirPath.getText();
				tableModel.addRow(new Object[]{ text });
			}
		});
		
		removePicDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRow = picDirTable.getSelectedRow();
				tableModel.removeRow(selectedRow);
				
			}
		});
		
		beginParseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String excelFile = excelPath.getText().trim();
				if( excelFile==null || excelFile.length()==0 ){
					return ;
				}
				
				logArea.setText("");
				process.setValue(0);
				MainCopyThread run = new MainCopyThread(thisFrame);
				Thread thread = new Thread(run);
				thread.start();
				
			}
		});
		
	}
	
	
	public List<String> getImgDirs(){
		List<String> rets = new ArrayList<String>();
		int rowCount = tableModel.getRowCount();
		for( int i=0 ;  i<rowCount  ; i++ ){
			Object valueAt = tableModel.getValueAt(i, 0);
			rets.add(valueAt.toString()       );
		}
		return rets;
	}
	
	public String getExcelPath(){
		return excelPath.getText().trim();
	}
	
	public void setProgress( int a ){
		process.setValue(a);
	}
	
	public void setFinished(  ){
		JOptionPane.showMessageDialog(thisFrame, "拷贝完成!");
	}
	
	public void appendLog( String log ){
		String text = logArea.getText();
		if( text.length() > 15000 ){
			text = text.substring(5000);
			logArea.setText(text);
		}
		logArea.append(log+"\r\n");
	}
	
	public void appendErrorLog( String log ){
		String text = logArea.getText();
		if( text.length() > 15000 ){
			text = text.substring(5000);
			logArea.setText(text);
		}
		logArea.append("Error XXXXXXXXXXXX:::"+log+"\r\n");
	}
	
	public static void main(String[] args) {
		
		
		MainApp mainApp = new MainApp();
		
		mainApp.setVisible(true);
		
	}
	
	
}
