package dev.scriptor.ui

fun test1() = Bundle().document("html") {
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

fun test2() = Bundle().html {
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

            button {
                +"Click me!"

                on("click") {
                    console.log("Hello world from click listener!")
                }
            }
        }
    }
}

fun main() {
    val doc1 = test1()
    val xml1 = doc1.toXmlString()

    val doc2 = test2()
    val xml2 = doc2.toXmlString()
}
