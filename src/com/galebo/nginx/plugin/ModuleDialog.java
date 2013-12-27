package com.galebo.nginx.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.galebo.nginx.Create;
import com.galebo.nginx.Module;
import com.galebo.nginx.Module.Parameter;

public class ModuleDialog extends BaseDialog {
	protected Object result;

	Module module = null;
	Text text1;
	TableViewer createTable;
	public ModuleDialog(Shell parent, int style) {
		super(parent);
	}

	public ModuleDialog(Shell parent) {
		this(parent, 0);
	}
	protected Shell createContents() {
		final Shell shell = new Shell(getParent(), 67680);
		
		shell.setLocation(200, 200);
		shell.setText("Add One Module");
		Label info1 = new Label(shell, SWT.NONE);info1.setText("Module Name");

		text1 = new Text(shell, SWT.BORDER | SWT.SINGLE);


		final Button addParameter = createAddParameterButton(shell);
		final Button create = createCreateButton(shell);
		createTable= createTable(shell);

		int height = 25;
		info1.setBounds(10, 14, 100, height);
		text1.setBounds(120, 10, 100, height);
		addParameter.setBounds(250, 10, 120, height);
		create      .setBounds(390, 10, 120, height);
		createTable.getTable().setBounds(10, 44, 500, 200);
		shell.setSize(530, 280);
		
		return shell;
	}

	private Button createCreateButton(final Shell shell) {
		final Button create = new Button(shell, 0);
		create.setText("Create Nginx File");
		create.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {

				if(text1.getText()==null||text1.getText().length()==0){
					MessageDialog.openInformation(shell, "", "name is null");
					return ;
				}
				Module module1=new Module(text1.getText(),false);
				for (Parameter parameter : module.getParameters()) {
					module1.addParameter(parameter);
				}
				String msg="create ok";
		        DirectoryDialog folderdlg=new DirectoryDialog(shell);
		        folderdlg.setText("choose directory");
		        folderdlg.setFilterPath("SystemDrive");
		        folderdlg.setMessage("please choose save directory");
		        String selecteddir=folderdlg.open();
		        if(selecteddir==null){
		        	msg="directory must select";
		        }else{
		        	Create create =new Create();
		        	try {
						String pathname = selecteddir+"/ngx_http_"+module1.getName()+"_module.c";
						System.out.println(pathname);
						FileUtils.writeStringToFile(new File(pathname),create.genFtlResult(module1));
					} catch (IOException e1) {
			        	msg="saveing has error："+e1.getMessage();
					}
		        }   
				MessageDialog.openInformation(shell, "",msg);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		return create;
	}

	private Button createAddParameterButton(final Shell shell) {
		final Button addParameter = new Button(shell, 0);
		addParameter.setText("Add Parameter");
		addParameter.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				ParameterDialog pd=new ParameterDialog(shell);
				pd.open();
				Parameter result2 = pd.getResult();
				if(result2!=null){
					if(module==null)
						module=new Module("",false);
					module.addParameter(result2);;
					createTable.setInput(module.getParameters());
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		return addParameter;
	}

	private TableViewer createTable(Shell shell) {
		TableViewer tv = new TableViewer(shell, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		Table table = tv.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableLayout tLayout = new TableLayout();
		table.setLayout(tLayout);
		tLayout.addColumnData(new ColumnWeightData(40));
		tLayout.addColumnData(new ColumnWeightData(20));
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(table, SWT.NONE).setText("Name");
		new TableColumn(table, SWT.NONE).setText("Type");
		new TableColumn(table, SWT.NONE).setText("Default_value");

		tv.setContentProvider(new MyContentProvider());
		tv.setLabelProvider(new MyLabelProvider());
		if(module!=null)
			tv.setInput(module.getParameters());
		return tv;
	}


	private static final class MyContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object element) {
			if (element instanceof List)
				return ((List<?>) element).toArray();
			else
				return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	private static final class MyLabelProvider implements ITableLabelProvider {
		public String getColumnText(Object element, int col) {
			Parameter parameter = (Parameter) element;
			if (col == 0)
				return parameter.getName();
			else if (col == 1)
				return parameter.getTypeName();
			else
				return parameter.getDefaultValue();
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}

	public void setData(Module module) {
		this.module=module;
	}

}
