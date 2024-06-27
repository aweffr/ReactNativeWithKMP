# React Native with Kotlin Multiplatform (KMP): A Practical Guide

This demo project demonstrates how to seamlessly 
integrate Kotlin Multiplatform (KMP) into an existing 
React Native (RN) project.

The repository begins with the standard React Native 
template version 0.73.

By making a few modifications to the Gradle configurations, 
we can incorporate the standard KMP starter project (shared module) 
into our `android/` directory. This allows the `KMP shared module`'s 
exported classes and functions to be accessible to both the Android 
and iOS parts of the project.

Following the initial setup, there are several 
commits that illustrate three classic use cases 
where native modules are required in React Native:
- Offloading computation logic to the native side.
- Handling asynchronous resources.
- Broadcasting updates from the native side using EventEmitter to 
  update the UI.

Additionally, the app features a showcase that highlights 
a common blocking issue in RN projects: the JavaScript engine 
becomes blocked due to heavy computation on a single thread. 
This problem becomes more pronounced as RN projects grow in size.
