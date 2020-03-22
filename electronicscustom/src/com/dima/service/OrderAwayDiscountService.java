package com.dima.service;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

public interface OrderAwayDiscountService {

	PriceInformation getDiscountedPrice(ProductModel product);

	PriceInformation getDiscountedPrice(ProductModel product, CurrencyModel currentCurrency);

	double discountForIndexin(ProductModel product, CurrencyModel currency);
}
