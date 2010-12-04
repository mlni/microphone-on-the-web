What's this?
------------

Have you ever needed to record audio directly on a web page, without installing anything extra? Well, neither did I. But there's a guitar school that did. 

This project contains a java applet that records a bit of audio directly into memory and let's you replay it and upload to the location specified in the parameters. To the server the upload looks exactly like a file upload from a form. In fact, the applet will use the browser's user agent and any cookies set by the server in order to look quite exactly like the browser. That's mainly because the Typo CMS kills any sessions where user agent changes during the session :)

Why?
----

There are basically three ways you can record audio on a webpage right now: Flash + streaming media server, MS Silverlight and a Java applet. Flash probably has the largest installed base, but unfortunately the streaming media server part didn't quite fit the bill. Silverlight is probably quite nice, but I don't have much experience with it. So that's why the recording is done with an applet.


What's it do?
----------

The project implements a java applet that can record, replay and upload a clip of sound. The sound is recorded from the default microphone and stored in an AU-formatted audio stream packed inside a zip file. The zipping is done mainly to reduce upload times on slow connections (about 2x deflate rate), but also to fit more audio into applet's memory. Though I actually never bothered to find out the maximal possible length.

The UI is implemented in beautiful engineer-designed Swing application using icons from Gnome project. During recording the record button displays approximate volume level on the background of the button. The upload button displays upload progress very similarly on the background of the upload button.

[About engineer-designed interfaces](http://dilbert.com/dyn/str_strip/000000000/00000000/0000000/000000/00000/2000/600/2652/2652.strip.gif)


You can tune the following configuration parameters of the applet:

* upload url
* upload field name
* additional post parameters to send with the upload request (eg, name of the submit button if needed)
* default filename displayed in the filename field
* background color of the applet
* maximal recording duration (in seconds)


Ideas for the future
---------------

Implement the mike as a faceless applet with javascript bindings, in order to better fit into modern web applications.

Who?
----

Me. Matti Jagula <matti.jagula@gmail.com>

