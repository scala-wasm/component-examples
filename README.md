# Scala Wasm Component Examples

Examples for compiling Scala to WebAssembly components with [scala-wasm](https://github.com/scala-wasm/scala-wasm).

## Prerequisites

- [Wasmtime](https://wasmtime.dev/)
- [wasm-tools](https://github.com/bytecodealliance/wasm-tools)
- [wit-bindgen-scala](https://github.com/scala-wasm/wit-bindgen-scala)
  - `cargo install wit-bindgen-scala --version 0.1.0`
- [cargo-component](https://github.com/bytecodealliance/cargo-component)
- [wac](https://github.com/bytecodealliance/wac)
- [wkg](https://github.com/bytecodealliance/wasm-pkg-tools)
- [Spin canary](https://developer.fermyon.com/spin/v4/install)

## Examples

- [helloworld](helloworld): minimal command component with a JUnit smoke test.
- [spin-todo](spin-todo): Spin HTTP + SQLite TODO API.
- [wasi-http-client](wasi-http-client): command component that sends an HTTP GET request.
- [rust-compose](rust-compose): cross-language component composition with Scala and Rust.

See each directory README for build and run steps.
