package com.mlq.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.mlq.sm.SM2Utils;

public class Window extends JFrame{
	StringBuffer str=new StringBuffer();
	// 显示面板
	JTextArea textArea; 
	// 文件URL输入栏
	JTextField urlField=new JTextField();
	public Window() {
		super("SM2加密"); 

		/*// 新建显示HTML的面板，并设置它不可编辑
		textPane = new JEditorPane(); */
		//textPane.setEditable(false);
		
		// 创建状态栏标签，并放在主窗口底部


		// 初始化菜单和工具栏
		this.initMenu();
		this.initToolbar();
		textArea=new JTextArea();
		textArea.setTabSize(4);
		textArea.setFont(new Font("标楷体", Font.BOLD, 16));
		textArea.setLineWrap(true);// 激活自动换行功能
		textArea.setWrapStyleWord(true);// 激活断行不断字功能
		textArea.setBackground(Color.white);
		
		
		// 将HTML显示面板放入主窗口，居中显示
		this.add(new JScrollPane(textArea),BorderLayout.CENTER);
	}
	private void initToolbar() {
		// 输入网址的文本框
		urlField = new JTextField();
		JToolBar toolbar = new JToolBar();

		// 地址标签
		toolbar.add(new JLabel("         地址："));
		toolbar.add(urlField);
		// 将工具栏放在主窗口的北部
		this.getContentPane().add(toolbar, BorderLayout.NORTH);
	}
	private void initMenu() {
		// 文件菜单，下面有两个菜单项：打开、退出
		JMenu fileMenu = new JMenu("文件");
		JMenuItem openMenuItem = new JMenuItem("打开");

		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();  
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
		        jfc.showDialog(new JLabel(), "选择");  
		        File file=jfc.getSelectedFile();  
		        if(file.isDirectory()){  
		        	urlField.setText("文件夹:"+file.getAbsolutePath());  
		        }else if(file.isFile()){  
		        	urlField.setText("文件:"+file.getAbsolutePath());  
		        }  
		        //建立数据的输入通道
				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(file);
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//建立缓冲数组配合循环读取文件的数据。
				int length = 0; //保存每次读取到的字节个数。
				byte[] buf = new byte[1024]; //存储读取到的数据    缓冲数组 的长度一般是1024的倍数，因为与计算机的处理单位。  理论上缓冲数组越大，效率越高
				try {
					while((length = fileInputStream.read(buf))!=-1){ // read方法如果读取到了文件的末尾，那么会返回-1表示。
						//SM2Utils.plainText=(new String(buf,0,length));
						try {
							str.append(new String(buf,0,length));	
							textArea.append(new String(buf,0,length));
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem exitMenuItem = new JMenuItem("退出");

		// 当“退出”时退出应用程序
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});

		fileMenu.add(openMenuItem);
		fileMenu.add(exitMenuItem);

		//帮助菜单，就一个菜单项：关于
		JMenu helpMenu = new JMenu("帮助");
		JMenu doMenu = new JMenu("操作");
		
		JMenuItem pItem = new JMenuItem("加密");
		pItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				SM2Utils.plainText=str.toString();
				
				try {
					textArea.append(SM2Utils.encrypte());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		JMenuItem cItem = new JMenuItem("解密");
		cItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				int  sb_length =str.length();// 取得字符串的长度
				str.delete(0,sb_length);    //删除字符串从0~sb_length-1处的内容
				String str1="";
				try {
					str1 = SM2Utils.decrypte();
					str.append(str1);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					textArea.append(str1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		doMenu.add(pItem);
		doMenu.add(cItem);
		JMenuItem aboutMenuItem = new JMenuItem("关于");
		helpMenu.add(aboutMenuItem);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(doMenu);
		menuBar.add(helpMenu);
		
		
		// 将菜单栏添加到主窗口
		this.setJMenuBar(menuBar);

	}
	public void exit() {
		// 弹出对话框，请求确认，如果确认退出，则退出应用程序
		if ((JOptionPane.showConfirmDialog(this, "你确定退出SM2加密器？", "退出",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)){
			System.exit(0);
		}
	}
	public static void main(String[] args) {
		// 设置浏览器，当所有浏览器窗口都被关闭时，退出应用程序

		Window window = new Window(); 
		window.setSize(800, 600);
		// 显示窗口
		window.setVisible(true); 

	}

}
