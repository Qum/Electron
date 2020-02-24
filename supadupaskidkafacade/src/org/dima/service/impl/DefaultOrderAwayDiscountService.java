package org.dima.service.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.dima.service.OrderAwayDiscountService;
import org.springframework.beans.factory.annotation.Required;


public class DefaultOrderAwayDiscountService implements OrderAwayDiscountService {

	private CommonI18NService com18service;

	@Override
	public PriceData getDiscountedPrice(final ProductModel product) {
		CurrencyModel currentCurrency = com18service.getCurrentCurrency();
		return getDiscountedPrice(product, currentCurrency);
	}

	@Override
	public PriceData getDiscountedPrice(final ProductModel product, final CurrencyModel currentCurrency) {

		final PriceData discountedPrice = new PriceData();
		discountedPrice.setValue(new BigDecimal(0));
		PriceRowModel priceForActualUserCurrency = getPricesForActualUserCurrency(product, currentCurrency);
		List<DiscountModel> actualDiscountsForCurrency = getActualDiscountsForCurrency(product, currentCurrency);

		if (priceForActualUserCurrency != null) {
			actualDiscountsForCurrency.forEach(discount -> {
				double basePrice = priceForActualUserCurrency.getPrice();

				double minusValue = discount.getAbsolute() ? discount.getValue() : basePrice * discount.getValue() / 100;
				double totalPrice  = basePrice - minusValue;
				totalPrice = com18service.roundCurrency(totalPrice, currentCurrency.getDigits());

				createPriceData(totalPrice, currentCurrency, discountedPrice);
			});
		}

		return discountedPrice;
	}

	@Override
	public List<DiscountModel> getActualDiscountsForCurrency(final ProductModel product, final CurrencyModel currentCurrency) {
		final List<DiscountModel> actualDiscountsForCurrency = new ArrayList<>();

		final List<DiscountRowModel> discountRowModels = new ArrayList<>(product.getEurope1Discounts());

		discountRowModels.forEach(discoRow -> {
			final DiscountModel discoMod = discoRow.getDiscount();
			boolean isAbsolute = discoMod.getAbsolute();
			if (!isAbsolute || discoMod.getCurrency().getIsocode().equals(currentCurrency.getIsocode())) {
				actualDiscountsForCurrency.add(discoMod);
			}
		});

		return actualDiscountsForCurrency;
	}

	@Override
	public PriceRowModel getPricesForActualUserCurrency(final ProductModel product, final CurrencyModel currentCurrency) {
		final List<PriceRowModel> pricesForActualUserCurrency = new ArrayList<>();

		final List<PriceRowModel> priceRowModels = new ArrayList<>(product.getEurope1Prices());

		priceRowModels.forEach(priceRow -> {
			if (priceRow.getCurrency().getIsocode().equals(currentCurrency.getIsocode())) {
				pricesForActualUserCurrency.add(priceRow);
			}
		});
		return CollectionUtils.isEmpty(pricesForActualUserCurrency) ? null : pricesForActualUserCurrency.get(0);
	}

	@Override
	public double discountForIndexin(final ProductModel product, final CurrencyModel currency) {

		return getDiscountedPrice(product, currency).getValue().doubleValue();
	}

	@Override
	public PriceData createPriceData(final double discountedValue, final CurrencyModel currencyModel) {
		PriceData discountedPrice = new PriceData();
		return createPriceData(discountedValue, currencyModel, discountedPrice);
	}

	@Override
	public PriceData createPriceData(final double discountedValue, final CurrencyModel currencyModel,
			final PriceData discountedPrice) {
		discountedPrice.setValue(BigDecimal.valueOf(discountedValue));
		discountedPrice.setCurrencyIso(currencyModel.getIsocode());
		discountedPrice.setFormattedValue(currencyModel.getSymbol() + BigDecimal.valueOf(discountedValue));

		return discountedPrice;
	}

	@Required
	public void setCommonI18NService(final DefaultCommonI18NService commonI18NService) {
		this.com18service = commonI18NService;
	}
}
