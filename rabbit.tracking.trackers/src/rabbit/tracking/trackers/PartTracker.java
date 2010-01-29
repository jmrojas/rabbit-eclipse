package rabbit.tracking.trackers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import rabbit.tracking.event.WorkbenchEvent;
import rabbit.tracking.storage.xml.IStorer;
import rabbit.tracking.storage.xml.WorkbenchEventStorer;

public class PartTracker extends Tracker implements IPartListener,
		IWindowListener {

	private long start;

	private Set<WorkbenchEvent> data;

	public PartTracker() {
		data = new LinkedHashSet<WorkbenchEvent>();
	}

	@Override
	protected void doDisable() {

		PlatformUI.getWorkbench().removeWindowListener(this);
		for (IPartService s : getPartServices()) {
			s.removePartListener(this);
		}

		final IWorkbench wb = PlatformUI.getWorkbench();
		wb.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {

				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				if (win != null && win.getPartService().getActivePart() != null) {
					endSession(win);
				}
			}
		});

		if (data.isEmpty()) {
			return;
		}

		IStorer<WorkbenchEvent> s = new WorkbenchEventStorer<WorkbenchEvent>();
		s.insert(data);
		s.commit();
	}

	@Override
	protected void doEnable() {

		PlatformUI.getWorkbench().addWindowListener(this);
		for (IPartService s : getPartServices()) {
			s.addPartListener(this);
		}
		startSession();
	}

	/**
	 * Gets all the {@link IPartService} from the currently opened windows.
	 * 
	 * @return A Set of IPartService.
	 */
	private Set<IPartService> getPartServices() {
		Set<IPartService> result = new HashSet<IPartService>();
		IWorkbenchWindow[] ws = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (IWorkbenchWindow w : ws) {
			result.add(w.getPartService());
		}
		return result;
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		System.err.print("Part activated.\t");
		startSession();
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		System.err.println("Part deactivated.");
		endSession(part.getSite().getWorkbenchWindow());
	}

	protected void startSession() {
		start = System.nanoTime();
	}

	protected void endSession(IWorkbenchWindow win) {
		long duration = (System.nanoTime() - start) / 1000000;
		if (duration <= 0) {
			throw new IllegalStateException("Duration cannot be 0 or negative.");
		}
		start = Long.MAX_VALUE;
		data.add(new WorkbenchEvent(Calendar.getInstance(), duration, win));
	}

	@Override
	public void windowActivated(IWorkbenchWindow window) {
		System.err.println("Window activated.");
		if (window.getPartService().getActivePart() != null) {
			startSession();
		}
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
		System.err.println("Window closed.");
		window.getPartService().removePartListener(this);
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		System.err.println("Window deactivated.");
		if (window.getPartService().getActivePart() != null) {
			endSession(window);
		}
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		System.err.println("Window opened.");
		window.getPartService().addPartListener(this);
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		System.err.println("Part brought to top.");
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		System.err.println("Part closed.");
	}

	@Override
	public void partOpened(IWorkbenchPart p) {
		System.err.println("Part opened.");
		if (p == p.getSite().getWorkbenchWindow().getPartService().getActivePart()) {
			startSession();
		}
	}

}
