# Maximus Starter Base

------

## Introduction

Clone the project and start with your project. Skip the modules which you don't need

Tutorial:

- Application should be developed mainly in app module
- User 
- No dagger or any other DI lib

## Modules

```cmd
├───app
│   ├───app       Application
│   ├───model     models
│   ├───ui        activities & fragments
│   │   ├───main  MainActivity
│   └───viewmodel viewmodels
│
├───lib
│   ├───adapter   databinding adapter
│   ├───extension kotlin extensions
│   └───mvvm      MVVM framework
│
└───network
    ├───api
    ├───model
    └───repository
```

## MVVM
Usually, a viwemodel can only aware the destroy of its owner in onClear() method. But after making it implements LifecycleObserver and observing owner's lifecycle in ViewModelProvider.Factory. It can use onCreate() or other lifecycle event now.
Check these codes in MVVMViewModelFactory.kt

## Todo
many many things..