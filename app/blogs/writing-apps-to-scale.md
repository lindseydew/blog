Scaling with confidence
scaling-with-confidence
20/08/2013
---
Writing software that works on 'my machine' versus writing software that works can be worlds apart. Hack days are proof that
devs can get a fairly decent application working in a day or even a couple of hours. But taking that piece of software, and
getting that to run smoothly for thousands or millions of users, on many variations of browsers, and suddenly it's a whole
different challenge.

Having worked on a couple of projects that have been required to work at scale, here are some of the few techniques and skills
my team have employed to deliver scalable software that go beyond just writing the code:

**Cross browser test early:** Make sure your whole team understands which browsers are officially supported, and get everyone
rotating browser.

**Invisible Load Testing**: Accurately simulating your PROD load is difficult. There are so many variables to keep
consistent before you can have any confidence in your tests results; same hard wear, same level of traffic, same intervals
being hit. Chances are you'll run a test, get a set of results, then spend your time wondering whether it's telling you something,
or it's just the way the tests were ran. Better, if possible, to add a call to all existing prod traffic that hits your
application.

**Monitoring:** Make visible how many failed requests you're serving. If you're using Google App Engine, you'll have monitoring build in.
Otherwise it can be a little harder to get set up. But even something crude that just alerts failed requests can help give
some visibility of how your app is performing at scale.

**Know your logs:** Making sure your app logs useful messages at error points, and being confident about how to search through
your logs can make a big difference to resolving unexpected/peculiar issues quickly.

**Attaching Unique Header:** If your client's request needs to go through multiple services to respond, then even ascertaining
which app has a failure point can be difficult. Attaching a unique header to these requests at the first point of contact service,
and then logging this via all the services the request calls, can help trace these and spot the origin of the error.


