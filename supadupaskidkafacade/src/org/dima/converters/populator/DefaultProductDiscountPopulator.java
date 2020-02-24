package org.dima.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.dima.service.OrderAwayDiscountService;
import org.springframework.beans.factory.annotation.Required;


public class DefaultProductDiscountPopulator implements Populator<ProductModel, ProductData> {

	private OrderAwayDiscountService defaultorderAwayDiscountService;

	@Override
	public void populate(final ProductModel productModel, final ProductData productData) throws ConversionException {
		productData.setDiscountedPrice(defaultorderAwayDiscountService.getDiscountedPrice(productModel));
	}

	@Required
	public void setOrderAwayDiscountService(final OrderAwayDiscountService defaultOrderAwayDiscountService) {
		this.defaultorderAwayDiscountService = defaultOrderAwayDiscountService;
	}
}
