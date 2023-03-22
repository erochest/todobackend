
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