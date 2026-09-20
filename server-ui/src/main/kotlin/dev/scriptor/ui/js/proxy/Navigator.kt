package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class Navigator(value: JsExpression) : JsProxy(value) {

    val appCodeName by proxy("appCodeName")
    val appName by proxy("appName")
    val appVersion by proxy("appVersion")
    val audioSession by proxy("audioSession", ::AudioSession)
    val bluetooth by proxy("bluetooth", ::Bluetooth)
    val clipboard by proxy("clipboard", ::Clipboard)
    val connection by proxy("connection", ::NetworkInformation)
    val contacts by proxy("contacts", ::ContactsManager)
    val cookieEnabled by proxy("cookieEnabled")
    val credentials by proxy("credentials", ::CredentialsContainer)
    val deviceMemory by proxy("deviceMemory")
    val devicePosture by proxy("devicePosture", ::DevicePosture)
    val geolocation by proxy("geolocation", ::Geolocation)
    val globalPrivacyControl by proxy("globalPrivacyControl")
    val gpu by proxy("gpu", ::GPU)
    val hardwareConcurrency by proxy("hardwareConcurrency")
    val hid by proxy("hid", ::HID)
    val ink by proxy("ink", ::Ink)
    val keyboard by proxy("keyboard", ::Keyboard)
    val language by proxy("language")
    val languages by proxy("languages")
    val locks by proxy("locks", ::LockManager)
    val login by proxy("login", ::NavigatorLogin)
    val maxTouchPoints by proxy("maxTouchPoints")
    val mediaCapabilities by proxy("mediaCapabilities")
    val mediaDevices by proxy("mediaDevices")
    val mediaSession by proxy("mediaSession")
    val mimeTypes by proxy("mimeTypes")
    val onLine by proxy("onLine")
    val oscpu by proxy("oscpu")
    val pdfViewerEnabled by proxy("pdfViewerEnabled")
    val permissions by proxy("permissions")
    val platform by proxy("platform")
    val plugins by proxy("plugins")
    val preferences by proxy("preferences")
    val presentation by proxy("presentation")
    val product by proxy("product")
    val productSub by proxy("productSub")
    val serial by proxy("serial")
    val serviceWorker by proxy("serviceWorker")
    val storage by proxy("storage")
    val usb by proxy("usb")
    val userActivation by proxy("userActivation")
    val userAgent by proxy("userAgent")
    val userAgentData by proxy("userAgentData")
    val vendor by proxy("vendor")
    val vendorSub by proxy("vendorSub")
    val virtualKeyboard by proxy("virtualKeyboard")
    val wakeLock by proxy("wakeLock")
    val webdriver by proxy("webdriver")
    val windowControlsOverlay by proxy("windowControlsOverlay")
    val xr by proxy("xr")

    val canShare by proxy("canShare")
    fun canShare(data: JsExpression = JsUndefined): JsCall {
        return this["canShare"](data)
    }

    val clearAppBadge by proxy("clearAppBadge")
    fun clearAppBadge(): JsCall {
        return this["clearAppBadge"]()
    }

    val getAutoplayPolicy by proxy("getAutoplayPolicy")
    fun getAutoplayPolicy(data: JsExpression = JsUndefined): JsCall {
        return this["getAutoplayPolicy"](data)
    }

    val getBattery by proxy("getBattery")
    fun getBattery(): JsCall {
        return this["getBattery"]()
    }

    val getGamepads by proxy("getGamepads")
    fun getGamepads(): JsCall {
        return this["getGamepads"]()
    }

    val getInstalledRelatedApps by proxy("getInstalledRelatedApps")
    fun getInstalledRelatedApps(): JsCall {
        return this["getInstalledRelatedApps"]()
    }

    val javaEnabled by proxy("javaEnabled")
    fun javaEnabled(): JsCall {
        return this["javaEnabled"]()
    }

    val registerProtocolHandler by proxy("registerProtocolHandler")
    fun registerProtocolHandler(scheme: JsExpression, url: JsExpression): JsCall {
        return this["registerProtocolHandler"](scheme, url)
    }

    val requestMediaKeySystemAccess by proxy("requestMediaKeySystemAccess")
    fun requestMediaKeySystemAccess(keySystem: JsExpression, supportedConfigurations: JsExpression): JsCall {
        return this["requestMediaKeySystemAccess"](keySystem, supportedConfigurations)
    }

    val requestMIDIAccess by proxy("requestMIDIAccess")
    fun requestMIDIAccess(options: JsExpression = JsUndefined): JsCall {
        return this["requestMIDIAccess"](options)
    }

    val sendBeacon by proxy("sendBeacon")
    fun sendBeacon(url: JsExpression, data: JsExpression = JsUndefined): JsCall {
        return this["sendBeacon"](url, data)
    }

    val setAppBadge by proxy("setAppBadge")
    fun setAppBadge(contents: JsExpression = JsUndefined): JsCall {
        return this["setAppBadge"](contents)
    }

    val share by proxy("share")
    fun share(data: JsExpression = JsUndefined): JsCall {
        return this["share"](data)
    }

    val taintEnabled by proxy("taintEnabled")
    fun taintEnabled(): JsCall {
        return this["taintEnabled"]()
    }

    val unregisterProtocolHandler by proxy("unregisterProtocolHandler")
    fun unregisterProtocolHandler(scheme: JsExpression, url: JsExpression): JsCall {
        return this["unregisterProtocolHandler"](scheme, url)
    }

    val vibrate by proxy("vibrate")
    fun vibrate(pattern: JsExpression): JsCall {
        return this["vibrate"](pattern)
    }
}
