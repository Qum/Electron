package com.dima.facades.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductVariantFacade;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.impl.DefaultBusinessProcessService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.impl.DefaultEventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.site.impl.DefaultBaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.store.services.impl.DefaultBaseStoreService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.dima.core.model.process.CartNotificationEmailProcessModel;
import com.dima.event.CartNotificationEvent;
import com.dima.facades.CartNoticationFacade;


public class DefaultCartNoticationFacade implements CartNoticationFacade {

	private CustomerFacade customerFacade;
	private ProductFacade productFacade;
	private BaseStoreService baseStoreService;
	private CommonI18NService commonI18NService;
	private BaseSiteService baseSiteService;
	private EventService eventService;
	private UserService userService;
	private BusinessProcessService businessProcessService;
	private ModelService modelService;

	private static final String CART_DATA_NPE_MESSAGE = "CartData cannot be null";

	@Override
	public void sendMailToCustomer(final CartData cartData) {
		validateParameterNotNull(cartData, CART_DATA_NPE_MESSAGE);

		if (CollectionUtils.isNotEmpty(cartData.getEntries())) {
			for (final OrderEntryData entry : cartData.getEntries()) {
				final String productCode = entry.getProduct().getCode();
				final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.VARIANT_MATRIX_BASE,
								ProductOption.PRICE_RANGE));
				entry.setProduct(product);
			}
		}

		eventService.publishEvent(new CartNotificationEvent(createCartNotificationEmailProcessModel(cartData)));
	}

	private CartNotificationEmailProcessModel createCartNotificationEmailProcessModel(final CartData cartData) {
		validateParameterNotNull(cartData, CART_DATA_NPE_MESSAGE);

		final List<String> itemsInCart = Objects.requireNonNull(cartData.getEntries()).stream().map(f -> f.getProduct().getName())
				.collect(Collectors.toList());

		final CartNotificationEmailProcessModel cartNotificationEmailProcessModel = getBusinessProcessService()
				.createProcess("CartNotificationEmail-" + System.currentTimeMillis(), "CartNotificationEmailProcess");

		cartNotificationEmailProcessModel.setCurrency(getCommonI18NService().getCurrentCurrency());
		cartNotificationEmailProcessModel.setLanguage(getCommonI18NService().getCurrentLanguage());
		cartNotificationEmailProcessModel.setCustomer((CustomerModel) getUserService().getCurrentUser());
		cartNotificationEmailProcessModel.setStore(getBaseStoreService().getCurrentBaseStore());
		cartNotificationEmailProcessModel.setSite(getBaseSiteService().getCurrentBaseSite());
		cartNotificationEmailProcessModel.setItemsInCart(itemsInCart);
		cartNotificationEmailProcessModel.setTotalCartPrice(cartData.getTotalPrice().getFormattedValue());

		getModelService().save(cartNotificationEmailProcessModel);
		return cartNotificationEmailProcessModel;
	}

	protected ProductFacade getProductFacade() {
		return productFacade;
	}

	@Required
	public void setProductFacade(final DefaultProductVariantFacade productFacade) {
		this.productFacade = productFacade;
	}

	protected CustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	@Required
	public void setCustomerFacade(final DefaultCustomerFacade customerFacade) {
		this.customerFacade = customerFacade;
	}

	protected CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final DefaultCommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	protected BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final DefaultBaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	protected BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final DefaultBaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	@Required
	public void setEventService(final DefaultEventService eventService) {
		this.eventService = eventService;
	}

	protected UserService getUserService() {
		return userService;
	}

	@Required
	public void setUserService(final DefaultUserService userService) {
		this.userService = userService;
	}

	protected BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final DefaultBusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	public void setModelService(final DefaultModelService modelService) {
		this.modelService = modelService;
	}

	protected ModelService getModelService() {
		return modelService;
	}

}
