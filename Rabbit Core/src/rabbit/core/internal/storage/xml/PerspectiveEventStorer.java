package rabbit.core.internal.storage.xml;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import rabbit.core.events.PerspectiveEvent;
import rabbit.core.internal.storage.xml.schema.events.EventListType;
import rabbit.core.internal.storage.xml.schema.events.PerspectiveEventListType;
import rabbit.core.internal.storage.xml.schema.events.PerspectiveEventType;

public class PerspectiveEventStorer
		extends AbstractStorer<PerspectiveEvent, PerspectiveEventType, PerspectiveEventListType> {

	@Override
	protected List<PerspectiveEventListType> getXmlTypeCategories(EventListType events) {
		return events.getPerspectiveEvents();
	}

	@Override
	protected boolean hasSameId(PerspectiveEventType x, PerspectiveEvent e) {
		return x.getPerspectiveId().equals(e.getPerspective().getId());
	}

	@Override
	protected boolean hasSameId(PerspectiveEventType x1, PerspectiveEventType x2) {
		return x1.getPerspectiveId().equals(x2.getPerspectiveId());
	}

	@Override
	protected void merge(PerspectiveEventType main, PerspectiveEvent e) {
		main.setDuration(main.getDuration() + e.getDuration());
	}

	@Override
	protected void merge(PerspectiveEventType main, PerspectiveEventType x) {
		main.setDuration(main.getDuration() + x.getDuration());
	}

	@Override
	protected void merge(PerspectiveEventListType main, PerspectiveEvent e) {
		merge(main.getPerspectiveEvent(), e);
	}

	@Override
	protected void merge(PerspectiveEventListType main, PerspectiveEventListType data) {
		merge(main.getPerspectiveEvent(), data.getPerspectiveEvent());
	}

	@Override
	protected PerspectiveEventType newXmlType(PerspectiveEvent e) {
		PerspectiveEventType type = OBJECT_FACTORY.createPerspectiveEventType();
		type.setDuration(e.getDuration());
		type.setPerspectiveId(e.getPerspective().getId());
		return type;
	}

	@Override
	protected PerspectiveEventListType newXmlTypeHolder(XMLGregorianCalendar date) {
		PerspectiveEventListType type = OBJECT_FACTORY.createPerspectiveEventListType();
		type.setDate(date);
		return type;
	}

	@Override
	protected IDataStore getDataStore() {
		return DataStore.PERSPECTIVE_STORE;
	}

}