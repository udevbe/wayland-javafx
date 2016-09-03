# wayland-javafx
A Wayland backend for JavaFX.

This is a work a in progress. Currently nothing works. Hopefully this will soon change.

basic TODO (in order):
 - output using sw rendering (via wayland shared memory buffers)
 - input handling through wayland's input protocols
 - output using hw rendering (via wayland drm buffers).

This library *will* make use of libraries not present in a standard jdk/jfx install as to get things up and running as quickly as possible.
 - [wayland-java-bindings](https://github.com/udevbe/wayland-java-bindings)
 - [jaccall](https://github.com/udevbe/jaccall)
 - [dagger2](https://github.com/google/dagger)

 The primary goal is to be able to use JavaFX as a pure client side widget toolkit capable to run on any Wayland compositor.
 
 Initial effort will focus on creating a Wayland implementation for the JavaFX Monocle back-end. This back-end is meant for the embedded, fullscreen, single application use case.
 
 Secondary effort is to create JavaFX Wayland back-end for general desktop usage.
