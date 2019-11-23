package com.github.princesslana.karmaleon

import com.github.princesslana.smalld.SmallD
import org.slf4j.impl.SimpleLogger

fun main(args: Array<String>) {
    if (System.getenv("KRML_DEBUG")?.toBoolean() ?: false) {
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    }

    SmallD.run(System.getenv("KRML_TOKEN")) { }
}
