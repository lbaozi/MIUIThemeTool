package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectFileAction implements ActionListener {
    private JTextField sourcePathField = null;
    private JPanel panel = null;
    
    public SelectFileAction(JTextField sourcePathField,JPanel panel){
        this.sourcePathField = sourcePathField;
        this.panel = panel;
    }

    /**
     * 点击按钮显示 选择文件弹出框
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser sourceFileChooser = new JFileChooser();
        //创建文件格式过滤
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MIUI主题", "mtz");
        //设置文件格式过滤
        sourceFileChooser.setFileFilter(filter);
        //显示选择文件对话框
        int returnVal = sourceFileChooser.showOpenDialog(panel);
        //点击确定后 获取文件路径 得到文件路径
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            sourcePathField.setText(sourceFileChooser.getSelectedFile().getAbsolutePath());
        }

    }

}
