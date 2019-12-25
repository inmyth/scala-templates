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

Cheatsheet:
- Functor : anything with map
- Monad : functor with flatmap


 
## Concurrency
In general concurrency task can be divided into four regions:

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
    - Use Case: Iterating data from a very big file or an infinite list (streams) and sending them to database
    - Tools: Reactive Streams (RxJava) 
     
### Scala Future


Scala Future is simple
```$xslt
   Future{ ... }
```
To resolve it we use higher-order functions like map or foreach
```$xslt
   Future{ 1 }. map(p => ...) // 1 will arrive in p
```

Or we can resolve it like this
```$xslt

   Future{ 1/0 }.onComplete {
      case Success(p) => 
      case Failure(e) => 
    }

``` 
And we can compose it with for-comprehension
```$xslt
    val composition = for {
      f1 <- Future{ 3 }
      f2 <- Future{ f1 + 50 }
      f3 <- Future { f2 + f1 } // any Future can get the results from previous Futures, in this case f1 and f2
    } yield (f2, f3) // yield returns the value
    composition.map(p => ...) // what is p ?
```

### The problem with Scala's default Future

Short answer: It is eagerly evaluated.

Long answer: It breaks [referential transparency](https://nrinaudo.github.io/scala-best-practices/definitions/referential_transparency.html). 

Referential transparency means that any expression can be replaced by its value.
Consider this code:
```$xslt
  val f = Future {
    println("Hello")
    67
  }

  val composition = for {
    f1 <- f
    f2 <- f
  } yield (f1, f2)

  composition.foreach(println)
/*
    Hello
    (67,67)
*/
```
We have a Future that contains a print "Hello" and return value 67. We assign it to `f`. Then we use `f` twice in a future comprehension. 
By intuition we should see "Hello" twice. But it is only printed once. 
This happens because Future is resolved by the time it is declared in the variable and the result is memoized. 
So `f` is not equivalent to the Future but the result of the Future. That's why it breaks the referential transparency. 
For this reason we are going to use other libraries like Monix or ZIO which has good mapping between Future and Task (basically you can replace "Future" with "Task" in the code and it will still be okay).

### Observable

Observable evaluates an element in a collection to the end of the process before moving to the next element. 



### Multi-threading and thread pools
Read this [first](https://gist.github.com/djspiewak/46b543800958cf61af6efa8e072bfd5c). 

Threads are expensive and not infinite. They come from thread pool which requires allocation.
In most cases we define an ExecutorService but in cases where it is not explicitly needed Java will use a thread pool based on ForkJoinPool. (Read about [it and its work stealing algorithm](https://www.baeldung.com/java-fork-join))  
Thread pool should be configured depending on the purposes. In general there are only three types:
- CPU-bound  (i.e computation, image processing, file read)
- Non-blocking IO (i.e web socket communication)
- Blocking IO (i.e database query using JDBC)

CPU-bound needs a fixed pool with number of threads equivalent to number of CPUs. We can also use ForkJoinPool here. 
Non-blocking IO can use a single thread. 
The problem  is non-blocking IO. The best way to handle it is replacing blocking library with a non-blocking one (in case of JDBC, [this](https://github.com/jasync-sql/jasync-sql) is the non-blocking alternative).