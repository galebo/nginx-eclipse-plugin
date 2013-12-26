package com.galebo.nginx.plugin;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.galebo.nginx.Module.Parameter;

public class ParameterDialog extends BaseDialog {
	protected Parameter result;

	protected Integer selectIndex=null;
	public ParameterDialog(Shell parent) {
		super(parent, SWT.NORMAL);
	}

	Text text1;
	Text text2;
	protected Shell createContents() {
		final Shell shell = new Shell( getParent());
		shell.setText("Add One Parameter");

		Label info1 = new Label(shell, SWT.NONE);info1.setText("Name");
		Label info2 = new Label(shell, SWT.NONE);info2.setText("Type");
		Label info3 = new Label(shell, SWT.NONE);info3.setText("Default Value");

		text1 = new Text(shell, SWT.BORDER | SWT.SINGLE);
		text2 = new Text(shell, SWT.BORDER | SWT.SINGLE);


		int height = 25;
		int lineHeight = 30;
		
		info1                 .setBounds(10, 14+lineHeight*0, 80, height);
		info2                 .setBounds(10, 14+lineHeight*1, 80, height);
		info3                 .setBounds(10, 14+lineHeight*2, 80, height);
		text1                 .setBounds(100, 10+lineHeight*0, 100, height);
		getCombo(shell)       .setBounds(100, 10+lineHeight*1, 100, height);
		text2                 .setBounds(100, 10+lineHeight*2, 100, height);
		getAddButton(shell)   .setBounds(220, 10+lineHeight*0, 50, height);
		getCancelButton(shell).setBounds(220, 10+lineHeight*1, 50, height);

		shell.setSize(300, 150);
		return shell;
	}



	private Button getAddButton(final Shell shell) {
		Button add    = new Button(shell, 0);
		add   .setText("Add");
		add   .addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if(text1.getText()==null){
					MessageDialog.openInformation(shell, "", "name is null");
					return ;
				}
				if(selectIndex==null){
					MessageDialog.openInformation(shell, "", "type is null");
					return ;
				}
				Object object=null;
				if(selectIndex==0){
					try {
						object=Integer.valueOf(text2.getText()==null?"0":text2.getText());
					} catch (Exception e2) {
						MessageDialog.openInformation(shell, "", "value '"+text2.getText()+"' is not number");
						return ;
					}
				}
				if(selectIndex==1){
					try {
						object=Boolean.valueOf(text2.getText()==null?"false":text2.getText());
					} catch (Exception e2) {
						MessageDialog.openInformation(shell, "", "value '"+text2.getText()+"' is not boolean");
						return ;
					}
				}
				if(selectIndex==2)
					object=text2.getText()==null?"":text2.getText();
				if(selectIndex==3)
					object=(text2.getText()==null?"":text2.getText()).split(",");
				result=new Parameter(text1.getText(), object);
				shell.close();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		return add;
	}



	private Button getCancelButton(final Shell shell) {
		final Button cancel=new Button(shell, 0);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		return cancel;
	}

	private Combo getCombo(Shell shell){
        final Combo combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setItems(new String[]{"integer","boolean","string","string[]"});
        combo.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectIndex=combo.getSelectionIndex();
            }
        });
        return combo;
	}

	public Parameter getResult() {
		return result;
	}
}
