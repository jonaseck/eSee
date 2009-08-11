
package de.unisb.cs.esee.ui.views;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class ModifiedResourceView extends ViewPart {
	private TableViewer viewer;
	private ModifiedResourceViewSorter sorter;
	
	public ModifiedResourceView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		sorter = new ModifiedResourceViewSorter();
        createColumns(viewer);
		viewer.setContentProvider(new ModifiedResourceContentProvider());
        viewer.setLabelProvider(new ModifiedResourceLabelProvider());
        viewer.addFilter(new ModifiedResourceViewerFilter());
        viewer.setSorter(sorter);
        viewer.addDoubleClickListener(new ModifiedResourceDoubleClickListener());
      
        if (root != null) {
			viewer.setInput(root);
        }
        
        WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider().addListener(new ILabelProviderListener() {
			public void labelProviderChanged(LabelProviderChangedEvent event) {
				viewer.refresh();
			}
		});
	}
	
	private void createColumns(final TableViewer viewer) {
		Table table = viewer.getTable();
		String[] titles = { "Path", "Author", "Revision", "Last checked date", "Revision date" };
		int[] bounds = { 300, 100, 100, 170 , 170};

		for (int i = 0; i < titles.length; i++) {
			final int index = i;
			final TableViewerColumn viewerColumn = new TableViewerColumn(
					viewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();
			column.setText(titles[i]);
			column.setWidth(bounds[i]);
			column.setResizable(true);
			column.setMoveable(true);
			
			column.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					sorter.setColumn(index);
					int dir = viewer.getTable().getSortDirection();
					if (viewer.getTable().getSortColumn() == column) {
						dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
					} else {
						dir = SWT.DOWN;
					}
					viewer.getTable().setSortDirection(dir);
					viewer.getTable().setSortColumn(column);
					viewer.refresh();
				}
			});
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}	
		
	@Override
	public void setFocus() {
	}
}
