package com.galebo.nginx.plugin;
import org.eclipse.swt.widgets.Shell;

import com.galebo.nginx.Module;
import com.galebo.nginx.Module.Parameter;


public class TestModuleDialog {
	static private Module getModule() {
		Module module = new Module("concat", false);
		module.addParameter(new Parameter("enable", false));
		module.addParameter(new Parameter("max_files", 10));
		module.addParameter(new Parameter("unique", true));
		module.addParameter(new Parameter("delimiter", ""));
		module.addParameter(new Parameter("ignore_file_error", false));
		module.addParameter(new Parameter("types", new String[] { "application/x-javascript", "text/css" }));
		return module;

	}

	public static void main(String[] args) {
		Shell shell = new Shell();
		ModuleDialog dialog = new ModuleDialog(shell);
		dialog.setData(getModule());
		dialog.open();
	}
}
