package de.unisb.cs.esee.ui.views;

import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import de.unisb.cs.esee.core.annotate.EseeAnnotations;
import de.unisb.cs.esee.core.annotate.Annotator.Location;
import de.unisb.cs.esee.core.data.SingleRevisionInfo;
import de.unisb.cs.esee.core.exception.BrokenConnectionException;
import de.unisb.cs.esee.core.exception.NotVersionedException;
import de.unisb.cs.esee.core.exception.UnsupportedSCMException;
import de.unisb.cs.esee.ui.util.StdRevisionHighlighter;

public class ModifiedResourceViewSorter extends ViewerSorter{
	private int propertyIndex;
	private static final int DESCENDING = 1;

	private int direction = DESCENDING;

	public ModifiedResourceViewSorter() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int result = 0;
		IFile f1 = (IFile) e1;
		IFile f2 = (IFile) e2;
		if (propertyIndex == 0) {
			result = f1.getFullPath().toString().compareToIgnoreCase(f2.getFullPath().toString());
		} else if (propertyIndex == 3) {
			try {
				String lcdStr1 = f1.getPersistentProperty(StdRevisionHighlighter.lastCheckedDateProp);
				String lcdStr2 = f2.getPersistentProperty(StdRevisionHighlighter.lastCheckedDateProp);
				
				if (lcdStr1 == lcdStr2)
					result =  0;
				else if (lcdStr1 == null)
					result = -1;
				else if (lcdStr2 == null)
					result = 1;
				else {
					Date d1 = new Date(Long.parseLong(lcdStr1));
					Date d2 = new Date(Long.parseLong(lcdStr2));
					result = d1.compareTo(d2);
				}
			} catch (CoreException e) {
				e.printStackTrace();
				result = 0;
			}
			
		} else {
			SingleRevisionInfo revInfo1;
			SingleRevisionInfo revInfo2;
			try {
				revInfo1 = EseeAnnotations.getResourceRevisionInfo(f1, Location.Local, null);
				revInfo2 = EseeAnnotations.getResourceRevisionInfo(f2, Location.Local, null);
					
				switch (propertyIndex) {
				case 1:
					result = revInfo1.author.compareToIgnoreCase(revInfo2.author);
					break;
				case 2:
					try {
						Long l1 = Long.parseLong(revInfo1.revision);
						Long l2 = Long.parseLong(revInfo2.revision);
						result = l1.compareTo(l2);
					} catch (Exception e) {
						result = revInfo1.revision.compareTo(revInfo2.revision);
					}
					break;
				case 4:
					result = (new Date(revInfo1.stamp)).compareTo(new Date (revInfo2.stamp));
					break;
				default:
					result = 0;
					break;
				} 
			} catch (UnsupportedSCMException e) {
				e.printStackTrace();
			} catch (BrokenConnectionException e) {
				e.printStackTrace();
			} catch (NotVersionedException e) {
				e.printStackTrace();
			}
		}
		return (direction == DESCENDING) ? result : -result;
	}
}
