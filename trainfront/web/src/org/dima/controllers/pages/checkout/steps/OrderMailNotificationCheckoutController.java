package org.dima.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateQuoteCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dima.controllers.ControllerConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/checkout/multi/order-Mail-Notification")
public class OrderMailNotificationCheckoutController extends AbstractCheckoutStepController {

	private static final String ORDER_MAIL_NOTIFICATION = "order-Mail-Notification";

	private static final Logger LOGGER = Logger.getLogger(OrderMailNotificationCheckoutController.class);

	@Override
	@RequestMapping(value = "/choose", method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateQuoteCheckoutStep
	@PreValidateCheckoutStep(checkoutStep = ORDER_MAIL_NOTIFICATION)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException {
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		this.prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryMethod.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());

		return ControllerConstants.Views.Pages.MultiStepCheckout.OrderMailNotificationPage;
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectDeliveryMode(
			@RequestParam(value = "order_mail_notificationew", defaultValue = "") String needMailNotification) {
		if (StringUtils.isNotEmpty(needMailNotification)) {
			LOGGER.info("need mail notif");
		}
		return getCheckoutStep().nextStep();
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes) {
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes) {
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep() {
		return getCheckoutStep(ORDER_MAIL_NOTIFICATION);
	}

}
