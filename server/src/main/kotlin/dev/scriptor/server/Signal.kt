package dev.scriptor.server

import dev.scriptor.server.http.ParameterList
import dev.scriptor.server.http.result.*
import java.io.InputStream
import java.nio.channels.ReadableByteChannel

open class Signal(
    val code: Int,
    val text: String,
    val headers: ParameterList = ParameterList(),
    val content: Any? = null,
) : Throwable("$code - $text") {

    fun generate(): HTTPResult<*> {
        if (content == null) {
            val headers = ParameterList(headers)

            if ("content-type" !in headers) {
                headers["content-type"] = "text/plain"
            }

            return HTTPResultString(
                code,
                text,
                headers,
                message,
            )
        }

        val headers = ParameterList(headers)

        if ("content-type" !in headers) {
            headers["content-type"] = "*/*"
        }

        return when (content) {
            is Unit -> HTTPResultUnit(
                code,
                text,
                headers,
            )

            is String -> HTTPResultString(
                code,
                text,
                headers,
                content,
            )

            is InputStream -> HTTPResultStream(
                code,
                text,
                headers,
                content,
            )

            is ReadableByteChannel -> HTTPResultChannel(
                code,
                text,
                headers,
                content,
            )

            else -> throw Error("invalid signal content $content")
        }
    }
}

/**
 * 100 - Continue
 */
class ContinueSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(100, "Continue", headers, content)

/**
 * 101 - Switching Protocols
 */
class SwitchingProtocolsSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(101, "Switching Protocols", headers, content)

/**
 * 102 - Processing
 */
class ProcessingSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(102, "Processing", headers, content)

/**
 * 103 - Early Hints
 */
class EarlyHintsSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(103, "Early Hints", headers, content)

/**
 * 200 - OK
 */
class OKSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(200, "OK", headers, content)

/**
 * 201 - Created
 */
class CreatedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(201, "Created", headers, content)

/**
 * 202 - Accepted
 */
class AcceptedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(202, "Accepted", headers, content)

/**
 * 203 - Non-Authoritative Information
 */
class NonAuthoritativeInformationSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(203, "Non-Authoritative Information", headers, content)

/**
 * 204 - No Content
 */
class NoContentSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(204, "No Content", headers, content)

/**
 * 205 - Reset Content
 */
class ResetContentSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(205, "Reset Content", headers, content)

/**
 * 206 - Partial Content
 */
class PartialContentSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(206, "Partial Content", headers, content)

/**
 * 207 - Multi-Status
 */
class MultiStatusSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(207, "Multi-Status", headers, content)

/**
 * 208 - Already Reported
 */
class AlreadyReportedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(208, "Already Reported", headers, content)

/**
 * 226 - IM Used
 */
class IMUsedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(226, "IM Used", headers, content)

/**
 * 300 - Multiple Choices
 */
class MultipleChoicesSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(300, "Multiple Choices", headers, content)

/**
 * 301 - Moved Permanently
 */
class MovedPermanentlySignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(301, "Moved Permanently", headers, content)

/**
 * 302 - Found
 */
class FoundSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(302, "Found", headers, content)

/**
 * 303 - See Other
 */
class SeeOtherSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(303, "See Other", headers, content)

/**
 * 304 - Not Modified
 */
class NotModifiedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(304, "Not Modified", headers, content)

/**
 * 307 - Temporary Redirect
 */
class TemporaryRedirectSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(307, "Temporary Redirect", headers, content)

/**
 * 308 - Permanent Redirect
 */
class PermanentRedirectSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(308, "Permanent Redirect", headers, content)

/**
 * 400 - Bad Request
 */
class BadRequestSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(400, "Bad Request", headers, content)

/**
 * 401 - Unauthorized
 */
class UnauthorizedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(401, "Unauthorized", headers, content)

/**
 * 402 - Payment Required
 */
class PaymentRequiredSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(402, "Payment Required", headers, content)

/**
 * 403 - Forbidden
 */
class ForbiddenSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(403, "Forbidden", headers, content)

/**
 * 404 - Not Found
 */
class NotFoundSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(404, "Not Found", headers, content)

/**
 * 405 - Method Not Allowed
 */
class MethodNotAllowedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(405, "Method Not Allowed", headers, content)

/**
 * 406 - Not Acceptable
 */
class NotAcceptableSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(406, "Not Acceptable", headers, content)

/**
 * 407 - Proxy Authentication Required
 */
class ProxyAuthenticationRequiredSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(407, "Proxy Authentication Required", headers, content)

/**
 * 408 - Request Timeout
 */
class RequestTimeoutSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(408, "Request Timeout", headers, content)

/**
 * 409 - Conflict
 */
class ConflictSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(409, "Conflict", headers, content)

/**
 * 410 - Gone
 */
class GoneSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(410, "Gone", headers, content)

/**
 * 411 - Length Required
 */
class LengthRequiredSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(411, "Length Required", headers, content)

/**
 * 412 - Precondition Failed
 */
class PreconditionFailedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(412, "Precondition Failed", headers, content)

/**
 * 413 - Content Too Large
 */
class ContentTooLargeSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(413, "Content Too Large", headers, content)

/**
 * 414 - URI Too Long
 */
class URITooLongSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(414, "URI Too Long", headers, content)

/**
 * 415 - Unsupported Media Type
 */
class UnsupportedMediaTypeSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(415, "Unsupported Media Type", headers, content)

/**
 * 416 - Range Not Satisfiable
 */
class RangeNotSatisfiableSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(416, "Range Not Satisfiable", headers, content)

/**
 * 417 - Expectation Failed
 */
class ExpectationFailedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(417, "Expectation Failed", headers, content)

/**
 * 418 - I'm A Teapot
 */
class ImATeapotSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(418, "I'm A Teapot", headers, content)

/**
 * 421 - Misdirected Request
 */
class MisdirectedRequestSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(421, "Misdirected Request", headers, content)

/**
 * 422 - Unprocessable Content
 */
class UnprocessableContentSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(422, "Unprocessable Content", headers, content)

/**
 * 423 - Locked
 */
class LockedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(423, "Locked", headers, content)

/**
 * 424 - Failed Dependency
 */
class FailedDependencySignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(424, "Failed Dependency", headers, content)

/**
 * 425 - Too Early
 */
class TooEarlySignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(425, "Too Early", headers, content)

/**
 * 426 - Upgrade Required
 */
class UpgradeRequiredSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(426, "Upgrade Required", headers, content)

/**
 * 428 - Precondition Required
 */
class PreconditionRequiredSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(428, "Precondition Required", headers, content)

/**
 * 429 - Too Many Requests
 */
class TooManyRequestsSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(429, "Too Many Requests", headers, content)

/**
 * 431 - Request Header Fields Too Large
 */
class RequestHeaderFieldsTooLargeSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(431, "Request Header Fields Too Large", headers, content)

/**
 * 451 - Unavailable For Legal Reasons
 */
class UnavailableForLegalReasonsSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(451, "Unavailable For Legal Reasons", headers, content)

/**
 * 500 - Internal Server Error
 */
class InternalServerErrorSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(500, "Internal Server Error", headers, content)

/**
 * 501 - Not Implemented
 */
class NotImplementedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(501, "Not Implemented", headers, content)

/**
 * 502 - Bad Gateway
 */
class BadGatewaySignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(502, "Bad Gateway", headers, content)

/**
 * 503 - Service Unavailable
 */
class ServiceUnavailableSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(503, "Service Unavailable", headers, content)

/**
 * 504 - Gateway Timeout
 */
class GatewayTimeoutSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(504, "Gateway Timeout", headers, content)

/**
 * 505 - HTTP Version Not Supported
 */
class HTTPVersionNotSupportedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(505, "HTTP Version Not Supported", headers, content)

/**
 * 506 - Variant Also Negotiates
 */
class VariantAlsoNegotiatesSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(506, "Variant Also Negotiates", headers, content)

/**
 * 507 - Insufficient Storage
 */
class InsufficientStorageSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(507, "Insufficient Storage", headers, content)

/**
 * 508 - Loop Detected
 */
class LoopDetectedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(508, "Loop Detected", headers, content)

/**
 * 510 - Not Extended
 */
class NotExtendedSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(510, "Not Extended", headers, content)

/**
 * 511 - Network Authentication Required
 */
class NetworkAuthenticationRequiredSignal(headers: ParameterList = ParameterList(), content: Any? = null) :
    Signal(511, "Network Authentication Required", headers, content)
