<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/checkout/multi/summary/placeOrder" var="placeOrderUrl" htmlEscape="false"/>
<spring:url value="/checkout/multi/termsAndConditions" var="getTermsAndConditionsUrl" htmlEscape="false"/>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

    <div class="row">
        <div class="col-sm-6">
            <div class="checkout-headline">
                <span class="glyphicon glyphicon-lock"></span>
                <spring:theme code="checkout.multi.secure.checkout" />
            </div>
            <multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
                <jsp:body>
                <ycommerce:testId code="checkoutStepThree">
                    <div class="checkout-review hidden-xs">
                        <div class="checkout-order-summary">

                            <form action="select" method="GET">
<%--                                <input type="checkbox" name="needSend" value="true">A you want to get Mail description for your cart?<Br>--%>
                                <input type="checkbox" name="order_mail_notificationew" value="true">A you want to get Mail description for your cart?<Br>
                                <button type="submit" class="btn btn-primary btn-block"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.useThesePaymentDetails"/></button>
<%--                                <button id="deliveryMethodSubmit" type="button" class="btn btn-primary btn-block checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue"/></button>--%>
                            </form>
                        </div>
                    </div>
                    <div class="place-order-form hidden-xs">
<%--                                        <form:form action="choose" id="notificationCheck1" commandName="notificationCheck">--%>
<%--                                            <div class="checkbox">--%>
<%--                                                <label> <form:checkbox id="Terms1" path="termsCheck" />--%>
<%--                                                </label>--%>
<%--                                            </div>--%>
<%--                                        </form:form>--%>

                    </div>
                </ycommerce:testId>
<%--                    <button id="deliveryMethodSubmit" type="button" class="btn btn-primary btn-block checkout-next"><spring:theme code="checkout.multi.deliveryMethod.continue"/></button>--%>
                </jsp:body>
            </multi-checkout:checkoutSteps>
        </div>

        <div class="col-sm-6">
            <multi-checkout:checkoutOrderSummary cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="true" showTaxEstimate="true" showTax="true" />
        </div>

        <div class="col-sm-12 col-lg-12">
            <br class="hidden-lg">
            <cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
                <cms:component component="${feature}"/>
            </cms:pageSlot>
        </div>
    </div>
</template:page>

<%--                    <form:form action="choose" id="notificationCheck1" commandName="notificationCheck">--%>
<%--                        <div class="checkbox">--%>
<%--                            <label> <form:checkbox id="Terms1" path="termsCheck" />--%>
<%--                            </label>--%>
<%--                            <button id="placeOrder" type="submit" class="btn btn-primary btn-place-order btn-block">--%>
<%--                                <spring:theme code="checkout.summary.placeOrder" text="Place Order"/>--%>
<%--                            </button>--%>
<%--                        </div>--%>
<%--                    </form:form>--%>
