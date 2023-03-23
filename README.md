
## Session 1

So one of the things that I'm curious about as we pick up on ChatGPT so how this will influence TDD.
Much/all of the discussion I've seen on it assumes that you're doing testing for quality
assurance. But I practice TDD as much as a design practice as a testing practice.

How will ChatGPT change the way I do tests? Do I guide it through both tests and implementation?
Do I start testing the entire deployable system from the outside only and ignore unit tests?

For my first attempt playing with this, I'm going to look at writing the unit tests myself and
asking ChatGPT to fix them. I'll try to do fairly standard red-green-refactor, at least to start
with.

I'm going to use the To-do-Backend project for this. I have a shell application in Kotlin+
Spring Boot stubbed out.

We'll see what happens!

I like to stop sessions with broken tests to remind me where to pick up, and I think this is long
enough for one video!

## Session 2

In case it's not clear, I've never really used Kotlin, and it's been a day or two since I've used
Spring Boot for more than 3 seconds.

So I've cleaned some things up and renamed them and set up the JPA dependencies and gotten ready
to test all that stuff.

Back to it.

First, I'll probably need to remind ChatGPT where we were.

### whenRootDelete_thenReturnsOk

Technically, the implementation does more than necessary to make the test pass: it doesn't
need to clear the items right now. But I'm also following the tests in the TodoBackend
project pretty closely, and I'd probably do them differently. Maybe in the next session
I'll do more driving and less cookbooking it. To mix metaphors.

### data objects

This is going to be slightly icky. I'll fix it later.

Cool. Gotta run, but solid progress.
