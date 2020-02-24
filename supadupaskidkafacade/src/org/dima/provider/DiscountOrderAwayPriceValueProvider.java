package org.dima.provider;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.DefaultFieldNameProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.dima.service.OrderAwayDiscountService;
import org.springframework.beans.factory.annotation.Required;


public class DiscountOrderAwayPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

	private FieldNameProvider fieldNameProvider;
	private OrderAwayDiscountService defaultOrderAwayDiscountService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException {

		final Collection<FieldValue> fieldValues = new ArrayList();
		final ProductModel product;

		if (model instanceof ProductModel) {
			product = (ProductModel) model;
		}
		else {
			throw new FieldValueProviderException("Cannot evaluate price of non-product item");
		}

		Collection<CurrencyModel> aviableCurrencyes = indexConfig.getCurrencies();

		if (indexedProperty.isCurrency() && CollectionUtils.isNotEmpty(aviableCurrencyes)) {
			aviableCurrencyes.forEach(currency -> addFieldValues(fieldValues, product, indexedProperty, currency));
		}

		return fieldValues;
	}

	public void addFieldValues(final Collection<FieldValue> fieldValues, final ProductModel product,
			final IndexedProperty indexedProperty, final CurrencyModel currency) {
		String currencyIso = currency.getIsocode();
		Collection<String> fieldNames = this.fieldNameProvider
				.getFieldNames(indexedProperty, currencyIso.toLowerCase(Locale.ROOT));
		final double discountedPrice = defaultOrderAwayDiscountService.discountForIndexin(product, currency);

		fieldNames.forEach(fieldName -> fieldValues.add(new FieldValue(fieldName, discountedPrice)));
	}

	public void setFieldNameProvider(final DefaultFieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}

	@Required
	public void setOrderAwayDiscountService(final OrderAwayDiscountService defaultOrderAwayDiscountService) {
		this.defaultOrderAwayDiscountService = defaultOrderAwayDiscountService;
	}

}
