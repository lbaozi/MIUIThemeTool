package view;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import controle.DealTheme;
import controle.SelectFileAction;

public class MainView {
    
    public void start() {
        initGlobalFontSetting(new Font("SimHei", Font.PLAIN, 12));
        JFrame frame = new JFrame("MIUI主题工具");
        JPanel panel = new JPanel();
        
        //设置窗口大小
        frame.setSize(400, 300);
        //设置退出按钮动作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置初始位置
        frame.setLocationRelativeTo(null);
        
        //主题包路径
        JLabel sourcePathLabel = new JLabel("主题包路径:");
        JTextField sourcePathField = new JTextField(40);
        sourcePathField.setEditable(false);
        JButton selectFileBtn = new JButton("打开");
        //选择文件按钮
        SelectFileAction selectFile = new SelectFileAction(sourcePathField,panel);
        selectFileBtn.addActionListener(selectFile);
        //处理主题按钮
        JButton dealThemeBtn = new JButton("开始");
        //显示结果输入框
        JTextArea resultArea = new JTextArea(8,50);
        //设置输入框自动换行
        resultArea.setLineWrap(true);
        //设置换行不断字
        resultArea.setWrapStyleWord(true);
        JScrollPane resultAreaPanel = new JScrollPane(resultArea);
        
        //创建文件处理类
        DealTheme dealTheme = new DealTheme(sourcePathField,resultArea);
        dealThemeBtn.addActionListener(dealTheme);
        
        panel.add(sourcePathLabel);
        panel.add(sourcePathField);
        panel.add(selectFileBtn);
        panel.add(resultAreaPanel);
        panel.add(dealThemeBtn);
        
        frame.add(panel);
        frame.setVisible(true);
    }
    
    public void initGlobalFontSetting(Font fnt){
        FontUIResource fontRes = new FontUIResource(fnt);
        for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }
}
