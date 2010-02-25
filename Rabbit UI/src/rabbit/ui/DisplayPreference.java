package rabbit.ui;

import java.util.Calendar;

import rabbit.ui.internal.RabbitUI;

/**
 * Contains preferences for displaying data.
 */
public final class DisplayPreference {

	private Calendar startDate;
	private Calendar endDate;

	/** Constructor. */
	public DisplayPreference() {
		endDate = Calendar.getInstance();
		startDate = (Calendar) endDate.clone();
		startDate.add(Calendar.DAY_OF_MONTH, -RabbitUI.getDefault().getDefaultDisplayDatePeriod());
		// setStartDate(start);
		// setEndDate(Calendar.getInstance());
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	// /**
	// * Gets a copy of the start date.
	// *
	// * @return A copy of the start date.
	// */
	// public Calendar getStartDate() {
	// return (Calendar) startDate.clone();
	// }

	// /**
	// * Sets the start date.
	// *
	// * @param date
	// * The start date.
	// * @throws NullPointerException
	// * If the parameter is null.
	// */
	// public void setStartDate(Calendar date) {
	// if (date == null)
	// throw new NullPointerException("Date cannot be null.");
	// if (date.equals(startDate))
	// return;
	// startDate = date;
	// setChanged();
	// notifyObservers();
	// }

	// /**
	// * Gets a copy of the end date.
	// *
	// * @return A copy of the end date.
	// */
	// public Calendar getEndDate() {
	// return (Calendar) endDate.clone();
	// }

	// /**
	// * Sets the end date.
	// *
	// * @param date
	// * The end date.
	// * @throws NullPointerException
	// * If the parameter is null.
	// */
	// public void setEndDate(Calendar date) {
	// if (date == null)
	// throw new NullPointerException("Date cannot be null.");
	// if (date.equals(endDate))
	// return;
	// endDate = date;
	// setChanged();
	// notifyObservers();
	// }
}
