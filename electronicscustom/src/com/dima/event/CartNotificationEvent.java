package com.dima.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import com.dima.core.model.process.CartNotificationEmailProcessModel;


public class CartNotificationEvent extends AbstractEvent {

	private CartNotificationEmailProcessModel cartNotificationProcessModel;

	public CartNotificationEvent(final CartNotificationEmailProcessModel cartNotificationProcessModel) {
		this.cartNotificationProcessModel = cartNotificationProcessModel;
	}

	public CartNotificationEmailProcessModel getCartNotificationEmailProcessModel() {
		return cartNotificationProcessModel;
	}

}
