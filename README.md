pingbot
=======

Automatically monitor your HTTP resources and visualize their health over the last past hours. Multiple resources can be grouped together, which again be put into an environment, allowing you to manage a larger amount of servers you would like to monitor. 

pingbot is a simple monitoring app which checks regularly (default: every minute) web pages you configured and saves the result (including response times) to a database and displayed as near-realtime plots. Beside the measure result, there is a database table holding the availability changes, which allows you to hook in and customize the way you want to be notified about resource outages.


Requires
--------

* Java Development Kit (JDK >= 6)
* [Play! Framework](http://www.playframework.org/documentation/1.2.5/install) (1.2.x)
* MySQL Database (by default uses `pingbot` schema on localhost access by 'root' user)


Setup
-----

Clone project by running 

    git clone https://github.com/nikos/pingbot

Create database schema (also for tests)

    mysqladmin create pingbot
    mysqladmin create pingbot_test

Ensure your mysql root user is able to access those new schemes, otherwise configure the database and user you would like to use by editing `conf/application.conf`

To setup the initial HTTP resources you might want to copy `conf/initial-data-sample.yml` over to `conf/initial-data.yml` containing your individual server setup to monitor. This configuration is read in at the very first time you start pingbot.

Before you can start the Play! application, you need to resolve the dependent modules by executing:

    play deps

Now you are ready to start pingbot by running:

    play run

You should now see the first web resources to be requested on the console and might want to give the graphical dashboard (which makes use of a great JavaScript plotting library: [Flot](http://www.flotcharts.org/)) by visting

    open http://localhost:9000/dashboard

To modify the resources being watched you can access a very simple (CRUD-based) UI at:

    open http://localhost:9000/admin


Screenshots
-----------

Example Dashboard View:

![Pingbot Dashboard](https://raw.github.com/nikos/pingbot/master/play1app/doc/pingbot-screenshot-dashboard.png)

Example Detail View:

![Pingbot Detail View](https://raw.github.com/nikos/pingbot/master/play1app/doc/pingbot-screenshot-detail.png)


Have fun, and please let me know if you have improvements or want your git pull requests merged in.

Niko (@niko_nava)
