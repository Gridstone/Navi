Navi
====

![Navi](http://img09.deviantart.net/b2a1/i/2006/310/8/1/_navi__by_link3kokiri.gif) Hey! Listen! Easy Android navigation with [Flow](https://github.com/square/flow) and MVP.

What is this library?
---------------------

Navi is an experimental library that aims to ease the development of `Fragment`less Android applications using the [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) pattern.

This library takes care of managing a stack of `Presenters` that have a lifecycle decoupled from `Views`. Your `Presenter` can take model data and pump it into a `View`, and also take events from a `View` and deliver them to your model layer. (This lends itself quite well to [RxJava](https://github.com/ReactiveX/RxJava)).

For now, Navi will remain very much in flux as we test how much we like this approach to development. We might find that we hate it and drop support all together; or maybe we'll love it. Who knows?

###Why not [Mortar](https://github.com/square/mortar/)?

Because we didn't like working with it that much, and found it a bit clunky. We don't mean to say that Navi is any better, but we're going to try something different. If you want something that works with Flow that's a little more battle tested, definitely check out Mortar.

We do find Flow to be pretty awesome though, and Navi delegates its back-stack management to Flow.

How do I use it?
----------------

Navi requires you to think about your application as a set of distinct `Screens`. You might have a Home screen that navigates to an Item List screen that navigates to a Details screen. In traditional Android development, these might have been `Activities`, or `Fragments` that pushed and popped.

When using Navi, each section of your app will have three defined components:
 - Screen
 - Presenter
 - View

###Screen

Your `Screen` should be a really dumb class that defines that `View`, layout, `Presenter`, and label to be used by this distinct section of your app.

```Kotlin
class HomeScreen : Screen<HomeView>() {
  override val label: String = "Home"
  override fun getLayoutRes(): Int = R.layout.home
  override fun createPresenter(context: Context): Presenter<HomeView> = HomePresenter(id)
}
```

###Presenter

Your `Presenter` mediates the delivery of data to a `View`, and capturing events from a `View`. A `Presenter` may or may not have a `View` at any point in time. Navi's `Presenters` survive orientation changes, which makes them much easier to conceptualise. When your user has navigated to a particular `Screen`, this `Presenter` will always exist. They also have their own lifecycle:

 - `onCreate(savedState: Bundle?)`
 - `onViewReady(view: V)`
 - `onViewDropped()`
 - `onSaveState(outState: Bundle)`
 - `onDestroy()`

`onCreate()` will be called before anything else. It provides an optional `Bundle` that can contain any data you may have persisted in `onSaveState()`. (Your app may have been terminated in the background).

`onViewReady()` will be called when a `View` has been handed to the `Presenter`. This gives you a clean way to interface with a non-null `View`. If you need to work with a `View` outside of this method, all `Presenters` have a property `view: View?`.

`onViewDropped()` will be called when a `View` has been taken away from the `Presenter`. Here you potentially stop any work that no longer needs to occur if a UI is not visible to the user. Navigation and`Activity` pause/resume will cause `onViewDropped()` and `onViewReady()` to be called.

`onSaveState()` will be called for all `Presenters` currently in the stack. The timing of this call matches `Activity's` `onSaveInstanceState()`, and gives your `Presenter` a change to persist anything it might need before it dies.

`onDestroy()` will be called when the `Presenter` has been removed from the stack. This is your final opportunity to free up resources and stop all work.

###View

Your `View` should simply display data to the screen and produce user input events. This can be a custom Android `View` with various methods exposed for the `Presenter` to interact with. It might also be an interface, allowing you to easily give your `Presenter` a mock `View` for testing purposes.

I need more info on how to use it!
----------------------------------

Check out the sample app in navi-example.

