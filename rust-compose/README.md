# rust-compose

Cross-language composition example.

Scala imports a custom `greeter` interface, Rust exports it as a `wasm32-wasip2` component, and `wac plug` links them into one runnable component.

## Run

From this directory:

```sh
make all
```

`make all` builds the Scala component, builds the Rust component, composes them with `wac plug`, and runs the result with Wasmtime.
