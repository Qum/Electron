package org.dima.service;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public interface OrderAwayDiscountService {

	PriceData getDiscountedPrice(ProductModel product);

	PriceData getDiscountedPrice(ProductModel product, CurrencyModel currentCurrency);

	List<DiscountModel> getActualDiscountsForCurrency(ProductModel product, CurrencyModel currentCurrency);

	PriceRowModel getPricesForActualUserCurrency(ProductModel product, CurrencyModel currentCurrency);

	double discountForIndexin(ProductModel product, CurrencyModel currency);

	PriceData createPriceData(double discountedValue, CurrencyModel currencyModel);

	PriceData createPriceData(double discountedValue, CurrencyModel currencyModel, PriceData discountedPrice);

	@Required
	void setCommonI18NService(DefaultCommonI18NService commonI18NService);
}
