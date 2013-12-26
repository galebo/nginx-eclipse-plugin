package com.galebo.nginx.plugin;

import org.eclipse.swt.widgets.Shell;

public class TestParameterDialog {
	public static void main(String[] args) {
		Shell shell = new Shell();
		ParameterDialog dialog = new ParameterDialog(shell);
		dialog.open();
		System.out.println(dialog.getResult().toString());;
	}
}
