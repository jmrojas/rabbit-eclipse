package rabbit.ui.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import rabbit.ui.DisplayPreference;
import rabbit.ui.IPage;
import rabbit.ui.internal.util.PageDescriptor;

public class RabbitViewTest {

	private static Shell shell;

	@BeforeClass
	public static void setUpBeforeClass() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				shell = new Shell(PlatformUI.getWorkbench().getDisplay());
			}
		});
	}

	@AfterClass
	public static void tearDownAfterClass() {
		shell.dispose();
	}

	@Test
	public void testUpdateDate() {
		Calendar date = Calendar.getInstance();
		DateTime widget = new DateTime(shell, SWT.NONE);
		widget.setYear(1901);
		widget.setMonth(3);
		widget.setDay(9);
		RabbitView.updateDate(date, widget);
		assertEquals(widget.getYear(), date.get(Calendar.YEAR));
		assertEquals(widget.getMonth(), date.get(Calendar.MONTH));
		assertEquals(widget.getDay(), date.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testUpdateDateTime() {
		Calendar date = Calendar.getInstance();
		date.set(1999, 2, 3);
		DateTime widget = new DateTime(shell, SWT.NONE);
		RabbitView.updateDateTime(widget, date);
		assertEquals(date.get(Calendar.YEAR), widget.getYear());
		assertEquals(date.get(Calendar.MONTH), widget.getMonth());
		assertEquals(date.get(Calendar.DAY_OF_MONTH), widget.getDay());
	}

	@Test
	public void testIsSameDate() {
		Calendar cal = Calendar.getInstance();
		DateTime widget = new DateTime(shell, SWT.NONE);
		RabbitView.updateDateTime(widget, cal);
		assertTrue(RabbitView.isSameDate(cal, widget));

		cal.add(Calendar.DAY_OF_MONTH, 1);
		assertFalse(RabbitView.isSameDate(cal, widget));
	}

	@Test
	public void testDispose() throws Exception {
		RabbitView view = new RabbitView();
		view.createPartControl(shell);
		view.dispose();

		Field metricsImg = RabbitView.class.getDeclaredField("metricsImg");
		metricsImg.setAccessible(true);
		assertTrue(((Image) metricsImg.get(view)).isDisposed());

		Field statImg = RabbitView.class.getDeclaredField("statImg");
		statImg.setAccessible(true);
		assertTrue(((Image) statImg.get(view)).isDisposed());

		Field toolkit = RabbitView.class.getDeclaredField("toolkit");
		toolkit.setAccessible(true);
		FormToolkit theKit = (FormToolkit) toolkit.get(view);
		Field isDisposed = FormToolkit.class.getDeclaredField("isDisposed");
		isDisposed.setAccessible(true);
		assertTrue((Boolean) isDisposed.get(theKit));
	}

	@Test
	public void testUpdate_checkDates() throws Exception {
		RabbitView view = new RabbitView();
		view.createPartControl(shell);

		Calendar fromDate = new GregorianCalendar(1999, 1, 1);
		DateTime fromDateTime = getFromDateTime(view);
		fromDateTime.setDate(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));

		Calendar toDate = new GregorianCalendar(2010, 1, 1);
		DateTime toDateTime = getToDateTime(view);
		toDateTime.setDate(toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH), toDate.get(Calendar.DAY_OF_MONTH));

		update(view);
		DisplayPreference pref = getPreference(view);

		assertEquals(fromDate.get(Calendar.YEAR), pref.getStartDate().get(Calendar.YEAR));
		assertEquals(fromDate.get(Calendar.MONTH), pref.getStartDate().get(Calendar.MONTH));
		assertEquals(fromDate.get(Calendar.DAY_OF_MONTH), pref.getStartDate().get(Calendar.DAY_OF_MONTH));

		assertEquals(toDate.get(Calendar.YEAR), pref.getEndDate().get(Calendar.YEAR));
		assertEquals(toDate.get(Calendar.MONTH), pref.getEndDate().get(Calendar.MONTH));
		assertEquals(toDate.get(Calendar.DAY_OF_MONTH), pref.getEndDate().get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testUpdate_checkPageStatus() throws Exception {
		RabbitView view = new RabbitView();
		view.createPartControl(shell);

		IPage visiblePage = null;
		for (PageDescriptor des : RabbitUI.getDefault().getPages()) {
			visiblePage = des.getPage();
			display(view, des.getPage());
		}
		// All pages have been displayed before, so they should all be updated:
		Map<IPage, Boolean> status = getPageStatus(view);
		for (boolean isPageUpdated : status.values()) {
			assertTrue(isPageUpdated);
		}

		update(view);
		// Now only the current visible page is updated:
		for (Map.Entry<IPage, Boolean> entry : status.entrySet()) {
			if (entry.getKey() == visiblePage)
				assertTrue(entry.getValue());
			else
				assertFalse(entry.getValue());
		}

	}

	private void display(RabbitView view, IPage page) throws Exception {
		Method display = RabbitView.class.getDeclaredMethod("display", IPage.class);
		display.setAccessible(true);
		display.invoke(view, page);
	}

	@SuppressWarnings("unchecked")
	private Map<IPage, Boolean> getPageStatus(RabbitView view) throws Exception {
		Field pageStatus = RabbitView.class.getDeclaredField("pageStatus");
		pageStatus.setAccessible(true);
		return (Map<IPage, Boolean>) pageStatus.get(view);
	}

	private void update(RabbitView view) throws Exception {
		Method update = RabbitView.class.getDeclaredMethod("update");
		update.setAccessible(true);
		update.invoke(view);
	}

	private DateTime getFromDateTime(RabbitView view) throws Exception {
		Field fromDateTimeField = RabbitView.class.getDeclaredField("fromDateTime");
		fromDateTimeField.setAccessible(true);
		return (DateTime) fromDateTimeField.get(view);
	}

	private DateTime getToDateTime(RabbitView view) throws Exception {
		Field toDateTimeField = RabbitView.class.getDeclaredField("toDateTime");
		toDateTimeField.setAccessible(true);
		return (DateTime) toDateTimeField.get(view);
	}

	private DisplayPreference getPreference(RabbitView view) throws Exception {
		Field pref = RabbitView.class.getDeclaredField("displayPref");
		pref.setAccessible(true);
		return (DisplayPreference) pref.get(view);
	}
}
