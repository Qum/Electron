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
import java.util.Collection;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import org.dima.service.OrderAwayDiscountService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderAwayDiscountService implements OrderAwayDiscountService {

	private CommonI18NService com18service;

	@Override
	public PriceData getDiscountedPrice(final ProductModel product) {
		final CurrencyModel currentCurrency = com18service.getCurrentCurrency();
		return getDiscountedPrice(product, currentCurrency);
	}

	@Override
	public PriceData getDiscountedPrice(final ProductModel product, final CurrencyModel currentCurrency) {

		if (product == null || currentCurrency == null) {
			throw new IllegalArgumentException("ProductModel and CurrencyModel most be nonNull");
		}

		final PriceData discountedPrice = new PriceData();
		discountedPrice.setValue(BigDecimal.valueOf(0));
		final PriceRowModel priceForActualCurrency = getPricesForActualCurrency(product, currentCurrency);
		final List<DiscountModel> actualDiscountsForCurrency = getActualDiscountsForCurrency(product, currentCurrency);

		if (priceForActualCurrency != null && isNotEmpty(actualDiscountsForCurrency)) {
			final double basePrice = priceForActualCurrency.getPrice();
			discountedPrice.setValue(BigDecimal.valueOf(basePrice));

			actualDiscountsForCurrency.forEach(discount -> {
				double totalPrice = discountedPrice.getValue().doubleValue();
				double minusValue = discount.getAbsolute() ? discount.getValue() : basePrice * discount.getValue() / 100;
				totalPrice = totalPrice - minusValue;
				totalPrice = com18service.roundCurrency(totalPrice, currentCurrency.getDigits());
				discountedPrice.setValue(BigDecimal.valueOf(totalPrice));
			});
			formatPriceData(discountedPrice, currentCurrency);
		}
		return discountedPrice;
	}

	@Override
	public double discountForIndexin(final ProductModel product, final CurrencyModel currency) {
		return getDiscountedPrice(product, currency).getValue().doubleValue();
	}

	@Override
	public PriceData createPriceData(final double discountedValue, final CurrencyModel currencyModel) {
		final PriceData discountedPrice = new PriceData();
		discountedPrice.setValue(BigDecimal.valueOf(discountedValue));
		discountedPrice.setCurrencyIso(currencyModel.getIsocode());
		discountedPrice.setFormattedValue(currencyModel.getSymbol() + BigDecimal.valueOf(discountedValue));

		return discountedPrice;
	}

	private List<DiscountModel> getActualDiscountsForCurrency(final ProductModel product, final CurrencyModel currentCurrency) {
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

	private PriceRowModel getPricesForActualCurrency(final ProductModel product, final CurrencyModel currentCurrency) {
		final List<PriceRowModel> pricesForActualUserCurrency = new ArrayList<>();
		final List<PriceRowModel> priceRowModels = new ArrayList<>(product.getEurope1Prices());

		priceRowModels.forEach(priceRow -> {
			if (priceRow.getCurrency().getIsocode().equals(currentCurrency.getIsocode())) {
				pricesForActualUserCurrency.add(priceRow);
			}
		});
		return isEmpty(pricesForActualUserCurrency) ? null : pricesForActualUserCurrency.get(0);
	}

	private void formatPriceData(final PriceData discountedPrice, final CurrencyModel currencyModel) {
		discountedPrice.setCurrencyIso(currencyModel.getIsocode());
		discountedPrice.setFormattedValue(currencyModel.getSymbol() + discountedPrice.getValue());
	}

	@Required
	public void setCommonI18NService(final DefaultCommonI18NService commonI18NService) {
		this.com18service = commonI18NService;
	}
}
