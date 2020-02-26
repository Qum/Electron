package org.dima.service;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;

public interface OrderAwayDiscountService {

	PriceData getDiscountedPrice(ProductModel product);

	PriceData getDiscountedPrice(ProductModel product, CurrencyModel currentCurrency);

	double discountForIndexin(ProductModel product, CurrencyModel currency);

	PriceData createPriceData(double discountedValue, CurrencyModel currencyModel);
}
