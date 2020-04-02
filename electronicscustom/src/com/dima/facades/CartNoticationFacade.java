package com.dima.facades;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;


public interface CartNoticationFacade {

	void sendMailToCustomer(CartData cartData);

}
