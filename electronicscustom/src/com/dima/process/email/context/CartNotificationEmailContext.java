package com.dima.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.yacceleratorfacades.process.email.context.CustomerEmailContext;

import java.util.List;

import com.dima.core.model.process.CartNotificationEmailProcessModel;


public class CartNotificationEmailContext extends CustomerEmailContext {

	private String totalCartPrice;

	private List<String> itemsInCart;

	@Override
	public void init(final StoreFrontCustomerProcessModel processModel, final EmailPageModel emailPageModel) {
		super.init(processModel, emailPageModel);
		if (processModel instanceof CartNotificationEmailProcessModel) {
			setTotalCartPrice(((CartNotificationEmailProcessModel) processModel).getTotalCartPrice());
			setItemsInCart(((CartNotificationEmailProcessModel) processModel).getItemsInCart());
		}
	}

	public String getTotalCartPrice() {
		return totalCartPrice;
	}

	public void setTotalCartPrice(final String totalCartPrice) {
		this.totalCartPrice = totalCartPrice;
	}

	public List<String> getItemsInCart() {
		return itemsInCart;
	}

	public void setItemsInCart(final List<String> itemsInCart) {
		this.itemsInCart = itemsInCart;
	}
}