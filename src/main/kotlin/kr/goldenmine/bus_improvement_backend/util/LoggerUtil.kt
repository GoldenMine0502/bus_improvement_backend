package kr.goldenmine.bus_improvement_backend.util

import org.slf4j.Logger

fun Logger.infoToString(text: Any?) {
    this.info(text.toString())
}