
package de.unisb.cs.esee.ui.views;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.decorators.DecoratorManager;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

@SuppressWarnings("restriction")
public class ModifiedResourceView extends ViewPart {
	private TreeViewer viewer;

	public ModifiedResourceView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
        viewer = new TreeViewer(parent, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
        viewer.setContentProvider(new ModifiedResourceContentProvider());
        viewer.setLabelProvider(new ModifiedResourceLabelProvider());
        viewer.addFilter(new ModifiedResourceViewerFilter());
        viewer.addDoubleClickListener(new ModifiedResourceDoubleClickListener());
        if (root != null) {
			viewer.setInput(root);
        }
        
        WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider().addListener(new ILabelProviderListener() {
			public void labelProviderChanged(LabelProviderChangedEvent event) {
				if (!event.getSource().getClass().equals(DecoratorManager.class)) 
					viewer.refresh();
			}
		});
	}

	@Override
	public void setFocus() {
	}
}
