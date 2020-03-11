package com.dima.populator;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;

import com.dima.service.OrderAwayDiscountService;

import org.springframework.beans.factory.annotation.Required;


public class DefaultProductDiscountPopulator implements Populator<ProductModel, ProductData> {

	private OrderAwayDiscountService defaultorderAwayDiscountService;

	private PriceDataFactory priceDataFactory;

	@Override
	public void populate(final ProductModel productModel, final ProductData productData) throws ConversionException {

		final PriceInformation info = defaultorderAwayDiscountService.getDiscountedPrice(productModel);

		final PriceData priceData = priceDataFactory
				.create(PriceDataType.BUY, BigDecimal.valueOf(info.getPriceValue().getValue()),
						info.getPriceValue().getCurrencyIso());

		productData.setDiscountedPrice(priceData);
	}

	@Required
	public void setOrderAwayDiscountService(final OrderAwayDiscountService defaultOrderAwayDiscountService) {
		this.defaultorderAwayDiscountService = defaultOrderAwayDiscountService;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory) {
		this.priceDataFactory = priceDataFactory;
	}
}
