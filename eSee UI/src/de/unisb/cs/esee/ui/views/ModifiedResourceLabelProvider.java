package de.unisb.cs.esee.ui.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.unisb.cs.esee.core.annotate.EseeAnnotations;
import de.unisb.cs.esee.core.annotate.Annotator.Location;
import de.unisb.cs.esee.core.data.SingleRevisionInfo;
import de.unisb.cs.esee.core.exception.BrokenConnectionException;
import de.unisb.cs.esee.core.exception.NotVersionedException;
import de.unisb.cs.esee.core.exception.UnsupportedSCMException;
import de.unisb.cs.esee.ui.ApplicationManager;
import de.unisb.cs.esee.ui.util.StdRevisionHighlighter;

public class ModifiedResourceLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static Image eSeeImage = null;
	private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");

	private static Image geteSeeImage() {
		if (eSeeImage == null) {
			ImageDescriptor descriptor = ApplicationManager.getImageDescriptor("resources/images/activateNewsIcon.png");
			if (descriptor != null)
				eSeeImage = descriptor.createImage();
		}
		return eSeeImage;
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return geteSeeImage();
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof IFile) {
			IFile file = (IFile) element;
			SingleRevisionInfo revInfo = null;
			try {
				revInfo = EseeAnnotations.getResourceRevisionInfo(file, Location.Local, null);
			} catch (UnsupportedSCMException e) {
				e.printStackTrace();
				return "Could not get revision infomation";
			} catch (BrokenConnectionException e) {
				e.printStackTrace();
				return "Could not get revision infomation";
			} catch (NotVersionedException e) {
				e.printStackTrace();
				return "Could not get revision infomation";
			}
			
			switch (columnIndex) {
			case 0:
				return file.getFullPath().toOSString();
			case 1:
				return revInfo.author;
			case 2:
				return revInfo.revision;
			case 3:
				try {
					String lcdStr = file.getPersistentProperty(StdRevisionHighlighter.lastCheckedDateProp);
					if (lcdStr == null)
						return "not set yet";
					Long l = Long.parseLong(lcdStr);
					Date d = new Date(l);
					return format.format(d);
				} catch (CoreException e) {
					e.printStackTrace();
					return "Could not read Properties.";
				}
			case 4:
				Date d = new Date(revInfo.stamp);
				return format.format(d);
			default:
				return "default";
			}
			
		}
		return "wrong resource";
	}
}
