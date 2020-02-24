package org.dima.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;

import org.dima.service.OrderAwayDiscountService;


public class SearchResultProductHasDiscountPopulator implements Populator<SearchResultValueData, ProductData> {

	private OrderAwayDiscountService defaultOrderAwayDiscountService;
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target) throws ConversionException {
		final double discountedValue = (double) source.getValues().get("discountValue");

		target.setDiscountedPrice(
				defaultOrderAwayDiscountService.createPriceData(discountedValue, commonI18NService.getCurrentCurrency()));
	}

	public void setCommonI18NService(final DefaultCommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public void setOrderAwayDiscountService(final OrderAwayDiscountService defaultOrderAwayDiscountService) {
		this.defaultOrderAwayDiscountService = defaultOrderAwayDiscountService;
	}
}