/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.errorreporter.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ErrorNotifyDialog extends Dialog {
	Double value;

	/**
	 * @param parent
	 */
	public ErrorNotifyDialog(Shell parent) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ErrorNotifyDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Makes the dialog visible.
	 *
	 * @return
	 */
	public Double open() {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		shell.setText("A problem was detected");
		shell.setLayout(new GridLayout(1, true));

		Label label = new Label(shell, SWT.NULL);
		label.setText("An unexpected error occured. Please press send to report the error to the development team");

		final Button buttonSend = new Button(shell, SWT.PUSH);
		buttonSend.setText("Send");

		Button buttonView = new Button(shell, SWT.PUSH);
		buttonView.setText("View Details");

		final Button buttonDontSend = new Button(shell, SWT.PUSH);
		buttonDontSend.setText("Don't Send");

		buttonSend.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});

		buttonView.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				value = null;

			}
		});

		buttonDontSend.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				value = null;
				shell.dispose();
			}
		});

		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE) {
					event.doit = false;
				}
			}
		});

		shell.pack();
		shell.open();

		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return value;
	}
}