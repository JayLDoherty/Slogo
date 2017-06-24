CompSci 308: VOOGASalad Analysis
===================

> This is the link to the assignment: [VOOGASalad](http://www.cs.duke.edu/courses/compsci308/current/assign/04_voogasalad/)

SLogo Addition
======

### Estimation

I'm planning to do the front end extension and I actually think it should be fairly straightforward. I know I'll have to add a GUI component, but I'll have some old code that I can follow along with because we have a variety of different GUI components already. I'm hoping this will take between 1 and 2 hours total.

I know I will need to add a new file for the GUI component to add to the side panel, and I will have to actually modify the Panel class to include the new component. I'll also have to modify some resource files to give it a title, etc. That might be it? I know we already have a Command that does this feature so I think the back-end functionality won't have to change at all.

### Review

It took me just under 2 hours to do this (like 1:50). 

I only had to add the one new file for the GUI component I was adding, but I modified a lot of files. I took back many of my modifications so Git exaggerates how many files I actually modified just because the whitespace changed a little. I had to change the Turtle class though so that Turtle's image index was an observable value (SimpleIntegerProperty). This was because my GUI component needed to be able to detect changes in individual turtles, so I needed to be able to add a listener to see when their image index would change.

No I definitely struggled a bit making this happen and I did not get it right on the first try.

### Analysis

This made me question our design pretty significantly. Even just accessing certain classes in the project is difficult because there are so many dependencies in constructors. The entire Panel is made once on program start up, so I had to find a way to get a copy of the TurtleManager, which was tricky because the workspace object hadn't been made yet and it was still null. 

Also our whole model-view-controller set up was really confusing in that project. Doing this just makes me feel like that code is a total mess. There wasn't really a class for me to extend for my new class, so I just copied code from a similar class, which isn't a great feeling. And even then this feature was subtly different enough that it wasn't possible to do it in exactly the same way, which meant I needed to start scouring the code for a different place to set things up.

If I had been completely unfamiliar with the code this would have take hours more to do. I felt like I remembered a lot about this project and it still took me about 2 hours to implement this relatively straightforward extension.

