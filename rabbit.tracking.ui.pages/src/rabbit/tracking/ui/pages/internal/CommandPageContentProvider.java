package rabbit.tracking.ui.pages.internal;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for a command page.
 */
public class CommandPageContentProvider implements IStructuredContentProvider {

	/** Constructor. */
	public CommandPageContentProvider() {}

	@Override public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Collection<?>) {
			return ((Collection<?>) inputElement).toArray();
		}
		return new Object[0];
	}

	@Override public void dispose() {}

	@Override public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
}