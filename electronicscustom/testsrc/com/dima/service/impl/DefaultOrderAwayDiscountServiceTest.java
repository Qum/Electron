package com.dima.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderAwayDiscountServiceTest {

	private static final String JPY_ISOCODE = "JPY";
	private static final String USD_ISOCODE = "USD";
	private static final Double BASE_PRICE_USD = 100.0D;
	private static final Double BASE_PRICE_JPY = 300.0D;
	private static final Double EXPECTED_PRICE_AFTER_PERCENTAGE_DISCOUNT = 80.0D;
	private static final Double EXPECTED_PRICE_AFTER_ABSOLUTE_DISCOUNT = 85.0D;
	private static final Double ABSOLUTE_DISCOUNT_VALUE = 15.0D;
	private static final Double PERCENTAGE_DISCOUNT_VALUE = 20.0D;
	private static final Double ZERO = 0D;
	private static final int DIGITS_AFTER_POINT = 2;

	@InjectMocks
	private DefaultOrderAwayDiscountService DefaultOrderAwayDiscountService;

	@Mock
	private ProductModel productModel;

	@Mock
	private DefaultCommonI18NService com18service;

	@Mock
	private DiscountModel discountModelForUSD;

	@Mock
	private DiscountModel discountModelForJPY;

	@Mock
	private DiscountModel discountModelForNotAbsolute;

	@Mock
	private CurrencyModel currencyModelForUSDPrice;

	@Mock
	private CurrencyModel currencyModelForJPYPrice;

	@Mock
	private CurrencyModel currencyModelForUSDDiscountModel;

	@Mock
	private CurrencyModel currencyModelForJPYDiscountModel;

	@Mock
	private CurrencyModel targetCurrencyModel;

	private List<PriceRowModel> priceRowModels = new ArrayList<>();
	private List<DiscountRowModel> discountRowModels = new ArrayList<>();
	private PriceRowModel priceRowModelForUSD = new PriceRowModel();
	private PriceRowModel priceRowModelForJPY = new PriceRowModel();
	private DiscountRowModel discountRowModelForUSD = new DiscountRowModel();
	private DiscountRowModel discountRowModelForJPY = new DiscountRowModel();
	private DiscountRowModel discountRowModelForNotAbsolute = new DiscountRowModel();

	@Before
	public void setUp() {
		when(currencyModelForUSDDiscountModel.getDigits()).thenReturn(DIGITS_AFTER_POINT);
		when(currencyModelForUSDPrice.getDigits()).thenReturn(DIGITS_AFTER_POINT);
		when(currencyModelForJPYPrice.getDigits()).thenReturn(DIGITS_AFTER_POINT);
		when(targetCurrencyModel.getDigits()).thenReturn(DIGITS_AFTER_POINT);
		when(targetCurrencyModel.getSymbol()).thenReturn("$");

		priceRowModelForUSD.setCurrency(currencyModelForUSDPrice);
		priceRowModelForJPY.setCurrency(currencyModelForJPYPrice);

		priceRowModelForUSD.setPrice(BASE_PRICE_USD);
		priceRowModelForJPY.setPrice(BASE_PRICE_JPY);

		priceRowModels.add(priceRowModelForUSD);
		priceRowModels.add(priceRowModelForJPY);

		when(discountModelForUSD.getValue()).thenReturn(ABSOLUTE_DISCOUNT_VALUE);
		when(discountModelForJPY.getValue()).thenReturn(ABSOLUTE_DISCOUNT_VALUE);
		when(discountModelForNotAbsolute.getValue()).thenReturn(PERCENTAGE_DISCOUNT_VALUE);

		discountRowModelForUSD.setDiscount(discountModelForUSD);
		discountRowModelForJPY.setDiscount(discountModelForJPY);
		discountRowModelForNotAbsolute.setDiscount(discountModelForNotAbsolute);

		when(discountModelForUSD.getAbsolute()).thenReturn(Boolean.TRUE);
		when(discountModelForUSD.getCurrency()).thenReturn(currencyModelForUSDDiscountModel);
		when(discountModelForJPY.getAbsolute()).thenReturn(Boolean.TRUE);
		when(discountModelForJPY.getCurrency()).thenReturn(currencyModelForJPYDiscountModel);
		when(discountModelForNotAbsolute.getAbsolute()).thenReturn(Boolean.FALSE);

		when(currencyModelForUSDPrice.getIsocode()).thenReturn(USD_ISOCODE);
		when(currencyModelForJPYPrice.getIsocode()).thenReturn(JPY_ISOCODE);
		when(currencyModelForUSDDiscountModel.getIsocode()).thenReturn(USD_ISOCODE);
		when(currencyModelForJPYDiscountModel.getIsocode()).thenReturn(JPY_ISOCODE);

		//setting actual currency in test - USD
		when(targetCurrencyModel.getIsocode()).thenReturn(USD_ISOCODE);

		when(com18service.getCurrentCurrency()).thenReturn(targetCurrencyModel);

		when(productModel.getEurope1Prices()).thenReturn(priceRowModels);
		when(productModel.getEurope1Discounts()).thenReturn(discountRowModels);
	}

	@Test
	public void shouldReturnDiscountedPriceValueWhenAbsoluteDiscountAviable() {
		discountRowModels.add(discountRowModelForUSD);
		discountRowModels.add(discountRowModelForJPY);

		final Double discountedPrice = DefaultOrderAwayDiscountService.getDiscountedPrice(productModel).getValue().getValue();
		assertEquals(EXPECTED_PRICE_AFTER_ABSOLUTE_DISCOUNT, discountedPrice);
	}

	@Test
	public void shouldReturnDiscountedPriceValueWhenPercentageDiscountAviable() {
		discountRowModels.add(discountRowModelForNotAbsolute);
		discountRowModels.add(discountRowModelForJPY);

		final PriceInformation result = DefaultOrderAwayDiscountService.getDiscountedPrice(productModel);
		final Double discountedPrice = result.getValue().getValue();
		assertEquals(EXPECTED_PRICE_AFTER_PERCENTAGE_DISCOUNT, discountedPrice);
	}

	@Test
	public void shouldReturnZeroValueWhenNoDiscountAviableForProductOrRequiredCurrency() {
		discountRowModels.add(discountRowModelForJPY);

		final PriceInformation result = DefaultOrderAwayDiscountService.getDiscountedPrice(productModel);
		final Double discountedPrice = result.getValue().getValue();
		assertEquals(ZERO, discountedPrice);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnThrowExceptionWhenArgumentIsNull() {
		DefaultOrderAwayDiscountService.getDiscountedPrice(productModel, null);
	}
}