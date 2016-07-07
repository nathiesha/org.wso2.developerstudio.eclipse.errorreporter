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

/**
 * @author Nathie
 *
 */
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TestDialog extends Dialog
{
  public TestDialog(final Shell parentShell)
  {
    super(parentShell);
  }


  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite body = (Composite)super.createDialogArea(parent);

    final TableViewer viewer = new TableViewer(body, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

    viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    // TODO: Set TableViewer content and label providers
    // TODO: Set TableViewer input
    
    viewer.add("hello");

    return body;
  }
}