# Scala


## Why Functional Programming ?

- To make program composition easy
- To eliminate side effects.

Side effect is a function that does more than returning return value. Programs that have side effect are more buggy and difficult to test.
```$xslt
int count = 0;

String method(String s) {
   count = count + 1; // this is side effect
   return "xxx" + s;
}
```

## Scala Basics
- [Side effects](https://alvinalexander.com/scala/scala-idiom-methods-functions-no-side-effects)
- [Monad](https://medium.com/beingprofessional/understanding-functor-and-monad-with-a-bag-of-peanuts-8fa702b3f69e) 
- Operations (tutorials: [AlvinAlexander](http://allaboutscala.com/#chapter-2-learning-scala-basics), [AllAboutScala](http://allaboutscala.com/#chapter-2-learning-scala-basics))



 
## Concurrency
In general concurrency task can be divided into four:

|              | Single        | Multiple  |
| ------------ |:-------------:| :-----:|
| Synchronous  | A                    | Iterable[A] |
| Asynchronous | Future[A] or Task[A] |   Observable[A] |

(*) A is `Any`, equivalent to Java `Object`

- Synchronous & single value: the basic programming style
- Synchronous & multiple values: 
    - Use Case : Iterating List or array
    - Tools: Java 8 Streams API
- Asynchronous & single value
    - Use Case: Passing requests in web server
    - Tools: JS Promise, Java and Scala Future, or Scala Task 
- Asynchronous & multiple values
    - Use Case: Iterating data from a very big file or an infinite list and sending them to database
    - Tools: Reactive Streams (RxJava) 
     
### Scala Future


Scala Future is simple
```$xslt
   Future{ ... }
```
To resolve it we can apply higher-order functions like map or foreach
```$xslt
   Future{ 1 }. map(p => ...) // 1 will arrive in p
```

Or we can resolve it like this
```$xslt

   Future{1/0}.onComplete {
      case Success(p) => 
      case Failure(e) => 
    }

``` 






### Multi-threading and thread pools
Read this [first](https://gist.github.com/djspiewak/46b543800958cf61af6efa8e072bfd5c). 

Threads are expensive and not infinite. They come from thread pool which requires allocation.
In most cases we define an ExecutorService but in cases where it is not explicitly needed Java will use a thread pool based on ForkJoinPool. (Read about [it and it's work stealing algorithm](https://www.baeldung.com/java-fork-join))  
Just note that thread pool should be ideal for the purpose of the task which can be classified as:
- CPU-bound  (computation, image processing, file read)
- Non-blocking IO (web socket communication)
- Blocking IO (database query using JDBC)

CPU-bound needs a fixed pool with number of threads equivalent to number of CPUs. 
Non-blocking IO can use a single thread. 
The problem  is non-blocking IO. The best way to handle it is replacing blocking library with a non-blocking one (Don't use JDBC but [this](https://github.com/jasync-sql/jasync-sql)).