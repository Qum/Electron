package com.dima.service.impl;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dima.service.OrderAwayDiscountService;

import org.springframework.beans.factory.annotation.Required;


public class DefaultOrderAwayDiscountService implements OrderAwayDiscountService {

	private static final String PRODUCT_NPE_MESSAGE = "ProductModel cannot be null";
	private static final String CURRENCY_NPE_MESSAGE = "CurrencyModel cannot be null";

	private CommonI18NService com18service;

	@Override
	public PriceInformation getDiscountedPrice(final ProductModel product) {
		final CurrencyModel currentCurrency = com18service.getCurrentCurrency();
		return getDiscountedPrice(product, currentCurrency);
	}

	@Override
	public PriceInformation getDiscountedPrice(final ProductModel product, final CurrencyModel currentCurrency) {
		validateParameterNotNull(product, PRODUCT_NPE_MESSAGE);
		validateParameterNotNull(currentCurrency, CURRENCY_NPE_MESSAGE);

		final List<PriceRowModel> pricesForActualCurrency = getPricesForActualCurrency(product, currentCurrency);
		final List<DiscountModel> actualDiscountsForCurrency = getActualDiscountsForCurrency(product, currentCurrency);
		Double result = 0d;

		if (isNotEmpty(pricesForActualCurrency) && isNotEmpty(actualDiscountsForCurrency)) {
			final Double basePrice = pricesForActualCurrency.get(0).getPrice();

			result = actualDiscountsForCurrency.stream()
					.map(discount -> discount.getAbsolute() ? discount.getValue() : basePrice * discount.getValue() / 100)
					.reduce(basePrice, (left, right) -> left - right);
		}
		final PriceValue discountedPV = new PriceValue(currentCurrency.getIsocode(), result, false);
		return new PriceInformation(discountedPV);
	}

	@Override
	public double discountForIndexin(final ProductModel product, final CurrencyModel currency) {
		return getDiscountedPrice(product, currency).getValue().getValue();
	}

	private List<DiscountModel> getActualDiscountsForCurrency(final ProductModel product, final CurrencyModel currentCurrency) {
		validateParameterNotNull(product, PRODUCT_NPE_MESSAGE);
		validateParameterNotNull(currentCurrency, CURRENCY_NPE_MESSAGE);

		final List<DiscountModel> actualDiscountsForCurrency = new ArrayList<>();
		final Collection<DiscountRowModel> discountRowModels = product.getEurope1Discounts();

		discountRowModels.forEach(discoRow -> {
			final DiscountModel discoMod = discoRow.getDiscount();
			final boolean isAbsolute = discoMod.getAbsolute();
			if (!isAbsolute) {
				actualDiscountsForCurrency.add(discoMod);
			}
			else {
				final boolean isActualDiscountForCurrency = discoMod.getCurrency().getIsocode()
						.equals(currentCurrency.getIsocode());
				if (isActualDiscountForCurrency) {
					actualDiscountsForCurrency.add(discoMod);
				}
			}
		});
		return actualDiscountsForCurrency;
	}

	private List<PriceRowModel> getPricesForActualCurrency(final ProductModel product, final CurrencyModel currentCurrency) {
		validateParameterNotNull(product, PRODUCT_NPE_MESSAGE);
		validateParameterNotNull(currentCurrency, CURRENCY_NPE_MESSAGE);

		final List<PriceRowModel> pricesForActualUserCurrency = new ArrayList<>();
		final List<PriceRowModel> priceRowModels = new ArrayList<>(product.getEurope1Prices());

		priceRowModels.forEach(priceRow -> {
			if (priceRow.getCurrency().getIsocode().equals(currentCurrency.getIsocode())) {
				pricesForActualUserCurrency.add(priceRow);
			}
		});
		return pricesForActualUserCurrency;
	}

	@Required
	public void setCommonI18NService(final DefaultCommonI18NService commonI18NService) {
		this.com18service = commonI18NService;
	}
}
