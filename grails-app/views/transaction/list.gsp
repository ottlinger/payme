<%@ page import="com.payme.db.Transaction" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-transaction" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                  default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" controller="account" action="index">Transaction Screen</g:link></li>
        <li><g:link class="list" action="pay" target="_blank">Pay Some Person</g:link></li>
    </ul>
</div>

<div id="chooseAccount" class="content scaffold-list" role="main">
    <g:form controller="transaction" action="list">
        Choose account to view:
        <g:select id="id" name="id"
                  from="${com.payme.db.Account.list()}" onchange="submit()"
                  optionKey="id" optionValue="name" required="" value="${accountId}"/>
    </g:form>
</div>


<div id="list-transaction" class="content scaffold-list" role="main">
    <h1>See transactions of "${accountName}"</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <th><g:message code="transaction.partner.label" default="Transaction partner"/></th>

            <g:sortableColumn property="amount"
                              title="${message(code: 'transaction.amount.label', default: 'Amount')}"/>

            <g:sortableColumn property="dateCreated"
                              title="${message(code: 'transaction.dateCreated.label', default: 'Date Created')}"/>
        </tr>
        </thead>
        <tbody>
        <g:each in="${transactionInstanceList}" status="i" var="transactionInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>${fieldValue(bean: transactionInstance, field: "partner")}</td>

                <td>${fieldValue(bean: transactionInstance, field: "amount")} £</td>

                <td><g:formatDate date="${transactionInstance.dateCreated}"/></td>

            </tr>
        </g:each>
        <tr class="${(transactionInstanceCount + 1 % 2) == 0 ? 'even' : 'odd'}">
            <td>Balance:</td>

            <td>${currentBalance} £</td>

            <td>&nbsp;</td>

        </tr>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${transactionInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
