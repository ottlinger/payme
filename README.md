PayMe
=====

Example prototype that models a payment application, based on Grails 2.5.0.

## Application structure

### View layout

The naming and UI layout does not *strictly* follow the supporting information given in the task.
I assume this is okay because I wanted to reuse as much as possible from auto-generated views/controllers.

### Domain concepts
Each account has a starting balance of 200, that is created during initial account creation.
An account's balance is calculated dynamically as each booking results in transactions being written into the database.
Thus balance is a transient field of each account.
Each balance is represented as a BigDecimal. You may transfer '210' or 210,13' from the pay screen.

### Separation of concerns: controllers and their services
The app has controllers that rely on services to perform their duties.
The transaction service is invoked when a bank transfer between accounts is requested. It performs the necessary operations on the domain objects to have a valid balance.

A notification service is used to send mail notifications upon bank transfers. These notifications are NOT send out since greenmail is used as local SMTP-mock. If run in production environments you can configure you correct mail provider to really send mails.

Example configuration to really send out mails:

<code>
    //        grails {
    //            mail {
    //                host = "smtp"
    //                port = 465
    //                username = "user@mailbox"
    //                password = "realpwgoes here"
    //                props = ["mail.smtp.auth"                  : "true",
    //                         "mail.smtp.socketFactory.port"    : "465",
    //                         "mail.smtp.socketFactory.class"   : "javax.net.ssl.SSLSocketFactory",
    //                         "mail.smtp.socketFactory.fallback": "false"]
    //            }
    //        }
</code>

## Relevant links

* http://localhost:8080/payme/ - application starting page
* http://localhost:8080/payme/greenmail/index - to see list of fake mails

How to run this application
---------------------------

Switch to correct grails version:
```gvm use grails 2.5.0
```

Start the application
```grails run-app
```

The application is only configured to be run in development/test mode. For a production deployment you need to configure a mail server
in order to fully take use of the notification feature when transfering money between accounts.

How to run tests
----------------
Switch to correct grails version:
```gvm use grails 2.5.0
```

Start the integration and unit tests
```grails test-app --stacktrace
```

You may want to have a look on the test reports under
```target/test-reports/html/all.html
```

The application contains some basic unit tests that make sure that the domain modelling works ok.
Furthermore service calls are checked - with the exception of the mailsending service (I had to @Ignore the test).

Basic integration tests ensure that logical constraints hold and payments can be made correctly.

The app lacks frontend tests!

How to import this app into IntelliJ
------------------------------------
- import existing project and select the folder you've checked out to
- you can base the import on Eclipse since grails creates .project/.classpath files, 
but will need to manually set the correct grails environment (path to gvm)
- Grails >3 has gradle support builtin, which will ease the IDE integration
- You may need to manually specificy your grails location as of:
```
$ which grails / example: /Users/username/.gvm/grails/current/
```

Areas of improvement / known issues
-----------------------------------

- balance calculation is slow with many transactions - possible solution: precompute results to reduce number of rows to consider when scanning the transaction table, Transaction.findAllWithDateCreatedGreaterThan(DateOfLastPrecomputation))
- more testing (UI/functional tests are missing), only some parts of the code are covered by integration tests
- UX in the frontend can be improved :-) I just wanted to reuse many of the auto-generated views/components.
- Frontend-/Browser-based testing is completely left out.
* Since my Grails knowdledge is very basic I did not want to spend too much time debugging and trying out things. I was happy to work with Spock for the first time. LLAP - Very nice ;-)
* The decoupling of notifications from actual bookings is very important (I did not do this for matters of simplicity), which might result in rollbacks in case a mail cannot be sent.
* The code is not 100%-groovy since I'm a Java developer. This can easily be changed :-)

Version: 2015-06-10
