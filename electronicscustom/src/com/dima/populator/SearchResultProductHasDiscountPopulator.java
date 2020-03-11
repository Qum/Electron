package com.dima.populator;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Required;


public class SearchResultProductHasDiscountPopulator implements Populator<SearchResultValueData, ProductData> {

	private static final String DISCOUNTED_PRICE_PARAM_NAME_FROM_CACHE = "discountValue";
	private PriceDataFactory priceDataFactory;
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target) throws ConversionException {
		final double discountedValue = (double) source.getValues().get(DISCOUNTED_PRICE_PARAM_NAME_FROM_CACHE);

		final PriceData priceData = priceDataFactory
				.create(PriceDataType.BUY, BigDecimal.valueOf(discountedValue), commonI18NService.getCurrentCurrency());

		target.setDiscountedPrice(priceData);
	}

	public void setCommonI18NService(final DefaultCommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory) {
		this.priceDataFactory = priceDataFactory;
	}
}