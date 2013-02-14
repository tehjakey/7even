<%@ page import="com.seven.Rating" %>



<div class="fieldcontain ${hasErrors(bean: ratingInstance, field: 'order', 'error')} required">
	<label for="order">
		<g:message code="rating.order.label" default="Order" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="order" type="number" value="${ratingInstance.order}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: ratingInstance, field: 'label', 'error')} ">
	<label for="label">
		<g:message code="rating.label.label" default="Label" />
		
	</label>
	<g:textField name="label" value="${ratingInstance?.label}"/>
</div>

