package de.unisb.cs.esee.ui.views;

import java.util.Date;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.unisb.cs.esee.core.annotate.EseeAnnotations;
import de.unisb.cs.esee.core.annotate.Annotator.Location;
import de.unisb.cs.esee.core.data.SingleRevisionInfo;
import de.unisb.cs.esee.core.exception.BrokenConnectionException;
import de.unisb.cs.esee.core.exception.NotVersionedException;
import de.unisb.cs.esee.core.exception.UnsupportedSCMException;
import de.unisb.cs.esee.ui.ApplicationManager;
import de.unisb.cs.esee.ui.util.IRevisionHighlighter;
import de.unisb.cs.esee.ui.util.StdRevisionHighlighter;

public class ModifiedResourceViewerFilter extends ViewerFilter {
    private IRevisionHighlighter highlighter = new StdRevisionHighlighter();

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (ApplicationManager.getDefault().isHighlightingActive()) {
			if (element instanceof IResource) {
				try {
					IResource resource = (IResource) element;
					SingleRevisionInfo revInfo = EseeAnnotations.getResourceRevisionInfo(resource, Location.Local, null);
					Date curRevDate = new Date(revInfo.stamp);
					
					if (highlighter.isChangeOfInterest(resource, curRevDate, revInfo.author)) 
						return true;
					else
						return false;
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
