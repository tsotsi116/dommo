Dommo
=====

## What is this?

> IMPORTANT NOTE: Dommo is an experiment, not production ready etc..

Mobile money is popular in Malawi for paying for things like utilities, 
TV subscriptions, even Insurance. Why can't we use these platforms to pay for 
things online so that more Malawian startups can offer their products online?

Dommo tries to address this problem by verifying Payments via a simple form.
 
Essentially, once a user pays via Mobile Money they will fill the Dommo form to
send the transaction reference information to be verifed by the merchant/service provider.
Once the merchant/service provider verifies the Payment via the Dommo Dashboard or API (coming soon) the user is notified via E-mail or SMS that their transaction was successful.

We know it's not as sexy as the flow for paying via Stripe, PayPal or VISA 
but it's a start. The ultimate goal is to inspire Mobile Money vendors to 
provide online payment APIs themselves to make the process more fluid.

**Prior art?**

Dommo is not the first platform that handles such a flow, but it is probably the 
first to offer an API so that anyone can implement this flow (and user interface)
in their own systems.

## Tech Stack


The following make this thing work:

* Java 14
* Sparkjava
* Gson
* Postgres
* VueJS for UI (with Vue CLI)
* golang-migrate/migrate
* [*Yavi](https://github.com/making/yavi) for validation
* [*Active Persistence](https://github.com/lazaronixon/active-persistence/) (evaluating it)

<small>An innovative solution needs a bleeding edge stack, right? ;)</small>


## Building

```shell
$ git clone https://github.com/nndi-oss/dommo.git

$ cd dommo

# Assuming you have downloaded and placed golang-migrate/migrate on your PATH
$ migrate -source file://src/resources/migrations -database postgres://localhost:5432/dommo?sslmode=disable up

$ yarn 

$ mvn clean package

$ export DATABASE_URL="jdbc:postgresql://localhost:5432/dommo"
$ export DATABASE_USERNAME="dommo"
$ export DATABASE_PASSWORD="5up3r5ecret!"

$ java --enable-preview -jar target/dommo-0.1.0-SNAPSHOT.jar
```


---

Copyright (c) 2020, NNDI