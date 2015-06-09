<%@ page import="com.payme.db.Transaction" %>



<div class="fieldcontain ${hasErrors(bean: transactionInstance, field: 'owner', 'error')} required">
	<label for="owner">
		<g:message code="transaction.owner.label" default="From:" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="owner" name="owner.id" from="${com.payme.db.Account.list()}" optionKey="id" required="" value="${transactionInstance?.owner?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: transactionInstance, field: 'partner', 'error')} required">
	<label for="partner">
		<g:message code="transaction.partner.label" default="To:" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="partner" name="partner.id" from="${com.payme.db.Account.list()}" optionKey="id" required="" value="${transactionInstance?.partner?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: transactionInstance, field: 'amount', 'error')} required">
	<label for="amount">
		<g:message code="transaction.amount.label" default="Amount:" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="amount" value="${fieldValue(bean: transactionInstance, field: 'amount')}" required=""/> £

</div>

