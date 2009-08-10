package de.unisb.cs.esee.ui.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class ModifiedResourceDoubleClickListener implements
		IDoubleClickListener {

	public void doubleClick(DoubleClickEvent event) {
		if (event.getSelection() instanceof IStructuredSelection){
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			final Object obj = selection.getFirstElement();
			if (obj instanceof IFile) {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IFile file = (IFile) obj;
				IEditorDescriptor desc = PlatformUI.getWorkbench().
		        getEditorRegistry().getDefaultEditor(file.getName());
				try {
					page.openEditor(new FileEditorInput(file), desc.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
