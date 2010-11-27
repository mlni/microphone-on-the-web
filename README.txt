What's this?
------------

Have you ever needed to record audio directly on a web page, without installing anything extra? Well, neither did I. But there's a guitar school that did. 

This project contains a java applet that records a bit of audio directly into memory and let's you replay it and upload to the location specified in the parameters. To the server the upload looks exactly like a file upload from a form. In fact, the applet will use the browser's user agent and any cookies set by the server in order to look quite exactly like the browser. That's mainly because the Typo CMS kills any sessions where user agent changes during the session :)

Why?
----

There's basically three ways you can record audio on a webpage right now: Flash + streaming media server, MS Silverlight and a Java applet. Flash probably has the largest installed base, but unfortunately the streaming media server part didn't quite fit the bill. Silverlight is probably quite nice, but I don't have much experience with it. So that's why the recording is done with and applet.

Who?
----

Me. Matti Jagula <matti.jagula@gmail.com>

