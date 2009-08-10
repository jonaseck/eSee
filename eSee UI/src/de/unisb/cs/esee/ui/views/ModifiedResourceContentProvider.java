package de.unisb.cs.esee.ui.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.model.WorkbenchContentProvider;


public class ModifiedResourceContentProvider extends WorkbenchContentProvider {
	public void dispose() {
	}
	
	@Override
	public boolean hasChildren(Object element) {
		return false;
	}
	
	@Override
	public Object[] getChildren(Object element) {
		return super.getChildren(element);
	}
	
	@Override
	public Object[] getElements(Object element) {
		return getFlatResources(element).toArray();
	}

	private ArrayList<Object> getFlatResources(Object parent) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (parent instanceof IFile)
			list.add(parent);
		if (super.hasChildren(parent)) {
			Object[] obs = super.getChildren(parent);
			for (Object o : obs) {
				list.addAll(getFlatResources(o));
			}
		}
		return list;
	}
}
