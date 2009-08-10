package de.unisb.cs.esee.ui.views;

import java.util.Date;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.unisb.cs.esee.core.annotate.EseeAnnotations;
import de.unisb.cs.esee.core.annotate.Annotator.Location;
import de.unisb.cs.esee.core.exception.BrokenConnectionException;
import de.unisb.cs.esee.core.exception.NotVersionedException;
import de.unisb.cs.esee.core.exception.UnsupportedSCMException;
import de.unisb.cs.esee.ui.ApplicationManager;
import de.unisb.cs.esee.ui.decorators.NewestResourcesDecorator;

public class ModifiedResourceViewerFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (ApplicationManager.getDefault().isHighlightingActive()) {
			if (element instanceof IResource) {
				try {
					IResource resource = (IResource) element;
					String lcdStr = resource.getPersistentProperty(NewestResourcesDecorator.lastCheckedDateProp);
					Date curRevDate = EseeAnnotations.getResourceDateAttribute(resource, Location.Local, null);

				    if (lcdStr == null) 
				    	return true;
				    else {
				    	long lcdStamp = Long.parseLong(lcdStr);
				    	Date lcd = new Date(lcdStamp);
					
				    	if (curRevDate.after(lcd)) 
				    		return true;
			    		else
			    			return false;
				    			
				    }
				} catch (CoreException e) {
					e.printStackTrace();
				} catch (UnsupportedSCMException e) {
					e.printStackTrace();
				} catch (BrokenConnectionException e) {
					e.printStackTrace();
				} catch (NotVersionedException e) {
					return false;
				}
			}
		}
		return false;
	}

}
