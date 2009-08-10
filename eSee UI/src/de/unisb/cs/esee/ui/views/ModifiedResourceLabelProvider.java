package de.unisb.cs.esee.ui.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.unisb.cs.esee.ui.ApplicationManager;

public class ModifiedResourceLabelProvider extends LabelProvider {
	private static Image eSeeImage = null;
	
	private static Image geteSeeImage() {
		if (eSeeImage == null) {
			ImageDescriptor descriptor = ApplicationManager.getImageDescriptor("resources/images/activateNewsIcon.png");
			if (descriptor != null)
				eSeeImage = descriptor.createImage();
		}
		return eSeeImage;
	}
	
	@Override
	public Image getImage(Object element) {
		return geteSeeImage();
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof IFile) 
			return ((IFile) element).getFullPath().toOSString();
		else
			return super.getText(element);
	}

}
