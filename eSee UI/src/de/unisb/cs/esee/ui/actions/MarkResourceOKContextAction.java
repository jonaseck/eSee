package de.unisb.cs.esee.ui.actions;

import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.unisb.cs.esee.core.annotate.EseeAnnotations;
import de.unisb.cs.esee.core.annotate.Annotator.Location;
import de.unisb.cs.esee.ui.decorators.NewestResourcesDecorator;

public class MarkResourceOKContextAction implements IObjectActionDelegate {
    private IStructuredSelection selection = null;

    public MarkResourceOKContextAction() {
	super();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	
    }

    @SuppressWarnings("unchecked")
	public void run(IAction action) {
    	if (this.selection != null) {
    		Iterator iter = this.selection.iterator();
    		while (iter.hasNext()) {
    			IResource selectedResource = null;
    		    Object obj = iter.next();

    		    if (obj instanceof IResource)
    		    	selectedResource = (IResource) obj;
    		    else if (obj instanceof IJavaElement) {
    		    	try {
    		    		selectedResource = ((IJavaElement) obj).getCorrespondingResource();
    		    	} catch (JavaModelException e) {
    		    		selectedResource = null;
    		    	}
    		    }
    		    markResourceAndChildren(selectedResource);
    		}
    	}
    }

    private void markResourceAndChildren(IResource res) {
	if (res != null) {
	    try {
		res.accept(new IResourceVisitor() {
		    public boolean visit(IResource resource) throws CoreException {
			try {
			    Date curRevDate = EseeAnnotations.getResourceDateAttribute(resource, Location.Local, null);

			    resource.setPersistentProperty(
				NewestResourcesDecorator.lastCheckedDateProp,
				Long.toString(curRevDate.getTime())
			    );

			    return true;
			} catch (Exception e) {
			    return false;
			}
		    }
		});
	    } catch (CoreException e) {
		e.printStackTrace();
	    }
	}
	
	PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	    public void run() {
		PlatformUI.getWorkbench().getDecoratorManager().update(NewestResourcesDecorator.ID);
	    }
	});
    }

    public void selectionChanged(IAction action, ISelection selection) {
    	if (selection instanceof IStructuredSelection) {
    		this.selection = (IStructuredSelection) selection; 
    	} else
    		this.selection = null;
    }
}
