package com.dima.provider;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import com.dima.service.impl.DefaultOrderAwayDiscountService;

import org.springframework.beans.factory.annotation.Required;


public class DiscountOrderAwayPriceValueResolver extends AbstractValueResolver {

	private DefaultOrderAwayDiscountService defaultOrderAwayDiscountService;
	private CommonI18NService commonI18NService;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final ItemModel product, final ValueResolverContext valueResolverContext)
			throws FieldValueProviderException {

		final ProductModel productModel;
		if (product instanceof ProductModel) {
			productModel = (ProductModel) product;
		}
		else {
			throw new FieldValueProviderException("Cannot evaluate price of non-product item");
		}

		addFieldValue(document, productModel, indexedProperty);
	}

	private void addFieldValue(final InputDocument document, final ProductModel product, final IndexedProperty indexedProperty)
			throws FieldValueProviderException {

		final String currencyIso = getQualifierProvider().getCurrentQualifier().toFieldQualifier();
		final CurrencyModel currency = commonI18NService.getCurrency(currencyIso);
		final double discountedPrice = defaultOrderAwayDiscountService.discountForIndexin(product, currency);

		document.addField(indexedProperty, discountedPrice, currencyIso);
	}

	@Required
	public void setOrderAwayDiscountService(final DefaultOrderAwayDiscountService defaultOrderAwayDiscountService) {
		this.defaultOrderAwayDiscountService = defaultOrderAwayDiscountService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}
}
