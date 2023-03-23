
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

## Session 3

I lied. I'm going to keep working through the tests given for the TodoBackend project.
I have gone ahead and implemented the next section of work, though, so you won't need
to watch me type as much as you have been. :)

My plan at this point is to continue with the next section of tests and features.

After that, I'll turn my attention to refactoring what we have to create some use-case
classes and a repository and database. That'll require me to write some tests.

When I come back from that, for the next section of TodoBackend tests, I'll quit coding
anything (insert ED joke here) and just have ChatGPT do everything.

K. Now back to coding.

Hmm. Why did I disable `whenPostThenGet_thenReturnsNewItem'? It looks like
it may have been working at some point.
Also, notice that I renamed the test methods and I refactored the tests.
Oh, so maybe this is the test I left hanging last time.
And the second failure is because the tests aren't properly isolated.

Let's fix that.

That mean's we're going to do the refactoring now.

The way I'd typically do that would be to
- introduce an interface in my tests,
- use the compiler warnings to push that change into the controller, and
- then mock out the repository.
- Then I'd have a separate set of tests to drive that implementation out.

But with my robot helper ...
I may actually do the same thing. We'll see.

Well, I said you wouldn't have to watch me, but I lied again. I'll be back
when there's more crunchy ChatGPT goodness. :)

