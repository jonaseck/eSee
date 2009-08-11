package de.unisb.cs.esee.ui.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class ModifiedResourceContentProvider implements IStructuredContentProvider {
	
    public Object[] getChildren(Object element) {
		try {
			if (element instanceof IContainer) {
				return ((IContainer) element).members();
			} else 
				return null;
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
			
    }	
	
	private ArrayList<Object> getFlatResources(Object parent) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (parent instanceof IFile)
			list.add(parent);
		
		Object[] obs = getChildren(parent);
		if (obs != null) {
			for (Object o : obs) {
				list.addAll(getFlatResources(o));
			}
		}
		return list;
	}

	public Object[] getElements(Object inputElement) {
		return getFlatResources(inputElement).toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
