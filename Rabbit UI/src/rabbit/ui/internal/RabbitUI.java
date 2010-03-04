package rabbit.ui.internal;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import rabbit.ui.IPage;
import rabbit.ui.internal.util.PageDescriptor;

/**
 * The activator class controls the plug-in life cycle
 */
public class RabbitUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "rabbit.ui";

	public static final String UI_PAGE_EXTENSION_ID = "rabbit.ui.pages";

	public static final String DEFAULT_DISPLAY_DATE_PERIOD = "defaultDisplayDatePeriod";

	// The shared instance
	private static RabbitUI plugin;

	private SortedSet<PageDescriptor> pages;

	/**
	 * The constructor
	 */
	public RabbitUI() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		pages = new TreeSet<PageDescriptor>(new Comparator<PageDescriptor>() {
			@Override
			public int compare(PageDescriptor o1, PageDescriptor o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (IConfigurationElement e : Platform.getExtensionRegistry()
				.getConfigurationElementsFor(UI_PAGE_EXTENSION_ID)) {
			PageDescriptor p = recursiveGet(e);
			if (p != null) {
				pages.add(p);
			}
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static RabbitUI getDefault() {
		return plugin;
	}

	/**
	 * Gets the pages.
	 * 
	 * @return The pages.
	 */
	public Set<PageDescriptor> getPages() {
		return pages;
	}

	/**
	 * Recursively builds a tree out of the given element.
	 * 
	 * @param e
	 *            The element.
	 * @return A tree, or null if one cannot be created.
	 */
	private PageDescriptor recursiveGet(IConfigurationElement e) {

		String name = e.getAttribute("name");
		String desc = e.getAttribute("description");
		String imagePath = e.getAttribute("icon");

		Object o = null;
		try {
			o = e.createExecutableExtension("class");

		} catch (CoreException ex) {
			System.err.println(ex.getMessage());
			return null;
		}

		if (!(o instanceof IPage)) {
			return null;
		}

		ImageDescriptor image = null;
		if (imagePath != null) {
			image = imageDescriptorFromPlugin(e.getContributor().getName(), imagePath);
		}
		IPage page = (IPage) o;
		PageDescriptor extension = new PageDescriptor(name, page, desc, image);

		for (IConfigurationElement child : e.getChildren()) {
			PageDescriptor p = recursiveGet(child);
			if (p != null) {
				extension.addChild(p);
			}
		}
		return extension;
	}

	/**
	 * Gets the default number of days to display the data in the main view.
	 * 
	 * @return The default number of days.
	 */
	public int getDefaultDisplayDatePeriod() {
		return getPreferenceStore().getInt(DEFAULT_DISPLAY_DATE_PERIOD);
	}

	/**
	 * Sets the default number of days to display the data in the main view.
	 * 
	 * @param numDays
	 *            The number of days.
	 */
	public void setDefaultDisplayDatePeriod(int numDays) {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(DEFAULT_DISPLAY_DATE_PERIOD, numDays);
	}
}
