mod bindings {
    wit_bindgen::generate!({
        path: "../wit",
        world: "rust",
    });

    use super::Component;
    export!(Component);
}

use bindings::exports::scala_wasm::rust_compose::greeter::Guest;

struct Component;

impl Guest for Component {
    fn greet(name: String) -> String {
        let mut buffer = Vec::new();
        let message = format!("Hello from Rust, {name}!");
        ferris_says::say(&message, 80, &mut buffer).unwrap();
        String::from_utf8(buffer).unwrap()
    }
}
