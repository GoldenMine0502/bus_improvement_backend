package kr.goldenmine.bus_improvement_backend.util

import java.io.File

val File.nameNoExtension
    get() = this.name.substring(0, this.name.lastIndexOf('.'))

val File.nameExtension
    get() = this.name.substring(this.name.lastIndexOf('.') + 1)