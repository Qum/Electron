package com.dima.event;

import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.impl.DefaultBusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;


public class CartNotificationEmailEventListener extends AbstractEventListener<CartNotificationEvent> {

	private BusinessProcessService businessProcessService;

	@Override
	protected void onEvent(final CartNotificationEvent cartNotificationEvent) {

		getBusinessProcessService().startProcess(cartNotificationEvent.getCartNotificationEmailProcessModel());
	}

	protected BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(final DefaultBusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}
}
