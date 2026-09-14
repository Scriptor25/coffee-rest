package dev.scriptor.ui

import dev.scriptor.ui.dom.document
import dev.scriptor.ui.html.html

fun test() = document("html") {
    element("head") {
        element("title") {
            +"Hello world!"
        }
    }
    element("body") {
        element("main") {
            element("h1", "id" to "headline") {
                +"Hello world!"
            }
            element("p") {
                +"Lorem ipsum dolor sit amet"
            }
        }
    }
}

fun test2() = html {
    head {
        title("Hello world!")
    }
    body {
        main {
            h1({ id = "headline" }) {
                +"Hello world!"
            }
            p {
                +"Lorem ipsum dolor sit amet"
            }
        }
    }
}
