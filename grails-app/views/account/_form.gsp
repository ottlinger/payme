<%@ page import="com.payme.db.Account" %>



<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="account.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${accountInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'email', 'error')} required">
	<label for="email">
		<g:message code="account.email.label" default="Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="email" name="email" required="" value="${accountInstance?.email}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: accountInstance, field: 'transactions', 'error')} ">
	<label for="transactions">
		<g:message code="account.transactions.label" default="Transactions" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${accountInstance?.transactions?}" var="t">
    <li><g:link controller="transaction" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="transaction" action="create" params="['account.id': accountInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'transaction.label', default: 'Transaction')])}</g:link>
</li>
</ul>


</div>

