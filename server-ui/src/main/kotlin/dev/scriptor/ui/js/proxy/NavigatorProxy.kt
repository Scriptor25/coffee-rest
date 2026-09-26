package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class NavigatorProxy(value: JsExpression) : JsProxy(value) {

    val appCodeName by proxy("appCodeName")
    val appName by proxy("appName")
    val appVersion by proxy("appVersion")
    val audioSession by proxy("audioSession", ::AudioSessionProxy)
    val bluetooth by proxy("bluetooth", ::BluetoothProxy)
    val clipboard by proxy("clipboard", ::ClipboardProxy)
    val connection by proxy("connection", ::NetworkInformationProxy)
    val contacts by proxy("contacts", ::ContactsManagerProxy)
    val cookieEnabled by proxy("cookieEnabled")
    val credentials by proxy("credentials", ::CredentialsContainerProxy)
    val deviceMemory by proxy("deviceMemory")
    val devicePosture by proxy("devicePosture", ::DevicePostureProxy)
    val geolocation by proxy("geolocation", ::GeolocationProxy)
    val globalPrivacyControl by proxy("globalPrivacyControl")
    val gpu by proxy("gpu", ::GPUProxy)
    val hardwareConcurrency by proxy("hardwareConcurrency")
    val hid by proxy("hid", ::HIDProxy)
    val ink by proxy("ink", ::InkProxy)
    val keyboard by proxy("keyboard", ::KeyboardProxy)
    val language by proxy("language")
    val languages by proxy("languages")
    val locks by proxy("locks", ::LockManagerProxy)
    val login by proxy("login", ::NavigatorLoginProxy)
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
