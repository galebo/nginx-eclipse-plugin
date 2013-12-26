package com.galebo.nginx.plugin;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class BaseDialog extends Dialog {

	public BaseDialog(Shell parent) {
		super(parent);
	}

	public BaseDialog(Shell parent, int style) {
		super(parent, style);
	}
	public void open() {
		Shell shell =createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!(shell.isDisposed()))
			if (!(display.readAndDispatch()))
				display.sleep();
	}

	protected abstract Shell createContents();
}