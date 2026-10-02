SCALA_MODULE := ../target/out/sjs1/scala-2.13.18/rust-compose-scala/rust-compose-scala-fastopt/main.wasm
RUST_MODULE := rust-greeter/target/wasm32-wasip2/release/rust_greeter.wasm
OUT := out.wasm

build-scala:
	cd .. && sbt rustComposeScala/fastLinkJS

build-rust:
	cd rust-greeter && cargo build --target wasm32-wasip2 --release

compose:
	wac plug --plug $(RUST_MODULE) $(SCALA_MODULE) -o $(OUT)

run: compose
	wasmtime -W gc,function-references,exceptions $(OUT)

all: build-scala build-rust run

clean:
	rm -f $(OUT)

.PHONY: build-scala build-rust compose run all clean
