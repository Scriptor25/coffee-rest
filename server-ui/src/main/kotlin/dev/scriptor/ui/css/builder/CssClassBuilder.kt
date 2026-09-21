package dev.scriptor.ui.css.builder

import dev.scriptor.ui.Delegate
import dev.scriptor.ui.css.CssClass
import dev.scriptor.ui.css.CssProperty
import kotlin.reflect.KProperty

enum class CssAlignItems(val value: String) {
    NORMAL("normal"),
    STRETCH("stretch"),

    BASELINE("baseline"),
    FIRST_BASELINE("first baseline"),
    LAST_BASELINE("last baseline"),

    CENTER("center"),
    START("start"),
    END("end"),
    SELF_START("self-start"),
    SELF_END("self-end"),
    FLEX_START("flex-start"),
    FLEX_END("flex-end"),

    UNSAFE_CENTER("unsafe center"),
    UNSAFE_START("unsafe start"),
    UNSAFE_END("unsafe end"),
    UNSAFE_SELF_START("unsafe self-start"),
    UNSAFE_SELF_END("unsafe self-end"),
    UNSAFE_FLEX_START("unsafe flex-start"),
    UNSAFE_FLEX_END("unsafe flex-end"),

    SAFE_CENTER("safe center"),
    SAFE_START("safe start"),
    SAFE_END("safe end"),
    SAFE_SELF_START("safe self-start"),
    SAFE_SELF_END("safe self-end"),
    SAFE_FLEX_START("safe flex-start"),
    SAFE_FLEX_END("safe flex-end"),
    ;

    companion object {
        fun parse(value: String): CssAlignItems? = when (value) {
            NORMAL.value -> NORMAL
            STRETCH.value -> STRETCH
            BASELINE.value -> BASELINE
            FIRST_BASELINE.value -> FIRST_BASELINE
            LAST_BASELINE.value -> LAST_BASELINE
            CENTER.value -> CENTER
            START.value -> START
            END.value -> END
            SELF_START.value -> SELF_START
            SELF_END.value -> SELF_END
            FLEX_START.value -> FLEX_START
            FLEX_END.value -> FLEX_END
            UNSAFE_CENTER.value -> UNSAFE_CENTER
            UNSAFE_START.value -> UNSAFE_START
            UNSAFE_END.value -> UNSAFE_END
            UNSAFE_SELF_START.value -> UNSAFE_SELF_START
            UNSAFE_SELF_END.value -> UNSAFE_SELF_END
            UNSAFE_FLEX_START.value -> UNSAFE_FLEX_START
            UNSAFE_FLEX_END.value -> UNSAFE_FLEX_END
            SAFE_CENTER.value -> SAFE_CENTER
            SAFE_START.value -> SAFE_START
            SAFE_END.value -> SAFE_END
            SAFE_SELF_START.value -> SAFE_SELF_START
            SAFE_SELF_END.value -> SAFE_SELF_END
            SAFE_FLEX_START.value -> SAFE_FLEX_START
            SAFE_FLEX_END.value -> SAFE_FLEX_END
            else -> null
        }
    }
}

enum class CssDisplay(val value: String) {

    BLOCK("block"),
    BLOCK_FLOW("block flow"),
    BLOCK_FLOW_ROOT("block flow-root"),
    BLOCK_TABLE("block table"),
    BLOCK_FLEX("block flex"),
    BLOCK_GRID("block grid"),
    BLOCK_RUBY("block ruby"),

    INLINE("inline"),
    INLINE_FLOW("inline flow"),
    INLINE_FLOW_ROOT("inline flow-root"),
    INLINE_TABLE("inline table"),
    INLINE_FLEX("inline flex"),
    INLINE_GRID("inline grid"),
    INLINE_RUBY("inline ruby"),

    RUN_IN("run-in"),
    RUN_IN_FLOW("run-in flow"),
    RUN_IN_FLOW_ROOT("run-in flow-root"),
    RUN_IN_TABLE("run-in table"),
    RUN_IN_FLEX("run-in flex"),
    RUN_IN_GRID("run-in grid"),
    RUN_IN_RUBY("run-in ruby"),

    FLOW("flow"),
    FLOW_ROOT("flow-root"),
    TABLE("table"),
    FLEX("flex"),
    GRID("grid"),
    RUBY("ruby"),

    LIST_ITEM("list-item"),

    BLOCK_LIST_ITEM("block list-item"),
    INLINE_LIST_ITEM("inline list-item"),
    RUN_IN_LIST_ITEM("run-in list-item"),

    FLOW_LIST_ITEM("flow list-item"),
    FLOW_ROOT_LIST_ITEM("flow-root list-item"),

    BLOCK_FLOW_LIST_ITEM("block flow list-item"),
    BLOCK_FLOW_ROOT_LIST_ITEM("block flow-root list-item"),

    INLINE_FLOW_LIST_ITEM("inline flow list-item"),
    INLINE_FLOW_ROOT_LIST_ITEM("inline flow-root list-item"),

    RUN_IN_FLOW_LIST_ITEM("run-in flow list-item"),
    RUN_IN_FLOW_ROOT_LIST_ITEM("run-in flow-root list-item"),

    TABLE_ROW_GROUP("table-row-group"),
    TABLE_HEADER_GROUP("table-header-group"),
    TABLE_FOOTER_GROUP("table-footer-group"),
    TABLE_ROW("table-row"),
    TABLE_CELL("table-cell"),
    TABLE_COLUMN_GROUP("table-column-group"),
    TABLE_COLUMN("table-column"),
    TABLE_CAPTION("table-caption"),
    RUBY_BASE("ruby-base"),
    RUBY_TEXT("ruby-text"),
    RUBY_BASE_CONTAINER("ruby-base-container"),
    RUBY_TEXT_CONTAINER("ruby-text-container"),

    CONTENTS("contents"),
    NONE("none"),

    GRID_LANES("grid-lanes"),
    INLINE_GRID_LANES("inline-grid-lanes"),
    ;

    companion object {
        fun parse(value: String): CssDisplay? = when (value) {
            BLOCK.value -> BLOCK
            BLOCK_FLOW.value -> BLOCK_FLOW
            BLOCK_FLOW_ROOT.value -> BLOCK_FLOW_ROOT
            BLOCK_TABLE.value -> BLOCK_TABLE
            BLOCK_FLEX.value -> BLOCK_FLEX
            BLOCK_GRID.value -> BLOCK_GRID
            BLOCK_RUBY.value -> BLOCK_RUBY
            INLINE.value -> INLINE
            INLINE_FLOW.value -> INLINE_FLOW
            INLINE_FLOW_ROOT.value -> INLINE_FLOW_ROOT
            INLINE_TABLE.value -> INLINE_TABLE
            INLINE_FLEX.value -> INLINE_FLEX
            INLINE_GRID.value -> INLINE_GRID
            INLINE_RUBY.value -> INLINE_RUBY
            RUN_IN.value -> RUN_IN
            RUN_IN_FLOW.value -> RUN_IN_FLOW
            RUN_IN_FLOW_ROOT.value -> RUN_IN_FLOW_ROOT
            RUN_IN_TABLE.value -> RUN_IN_TABLE
            RUN_IN_FLEX.value -> RUN_IN_FLEX
            RUN_IN_GRID.value -> RUN_IN_GRID
            RUN_IN_RUBY.value -> RUN_IN_RUBY
            FLOW.value -> FLOW
            FLOW_ROOT.value -> FLOW_ROOT
            TABLE.value -> TABLE
            FLEX.value -> FLEX
            GRID.value -> GRID
            RUBY.value -> RUBY
            LIST_ITEM.value -> LIST_ITEM
            BLOCK_LIST_ITEM.value -> BLOCK_LIST_ITEM
            INLINE_LIST_ITEM.value -> INLINE_LIST_ITEM
            RUN_IN_LIST_ITEM.value -> RUN_IN_LIST_ITEM
            FLOW_LIST_ITEM.value -> FLOW_LIST_ITEM
            FLOW_ROOT_LIST_ITEM.value -> FLOW_ROOT_LIST_ITEM
            BLOCK_FLOW_LIST_ITEM.value -> BLOCK_FLOW_LIST_ITEM
            BLOCK_FLOW_ROOT_LIST_ITEM.value -> BLOCK_FLOW_ROOT_LIST_ITEM
            INLINE_FLOW_LIST_ITEM.value -> INLINE_FLOW_LIST_ITEM
            INLINE_FLOW_ROOT_LIST_ITEM.value -> INLINE_FLOW_ROOT_LIST_ITEM
            RUN_IN_FLOW_LIST_ITEM.value -> RUN_IN_FLOW_LIST_ITEM
            RUN_IN_FLOW_ROOT_LIST_ITEM.value -> RUN_IN_FLOW_ROOT_LIST_ITEM
            TABLE_ROW_GROUP.value -> TABLE_ROW_GROUP
            TABLE_HEADER_GROUP.value -> TABLE_HEADER_GROUP
            TABLE_FOOTER_GROUP.value -> TABLE_FOOTER_GROUP
            TABLE_ROW.value -> TABLE_ROW
            TABLE_CELL.value -> TABLE_CELL
            TABLE_COLUMN_GROUP.value -> TABLE_COLUMN_GROUP
            TABLE_COLUMN.value -> TABLE_COLUMN
            TABLE_CAPTION.value -> TABLE_CAPTION
            RUBY_BASE.value -> RUBY_BASE
            RUBY_TEXT.value -> RUBY_TEXT
            RUBY_BASE_CONTAINER.value -> RUBY_BASE_CONTAINER
            RUBY_TEXT_CONTAINER.value -> RUBY_TEXT_CONTAINER
            CONTENTS.value -> CONTENTS
            NONE.value -> NONE
            GRID_LANES.value -> GRID_LANES
            INLINE_GRID_LANES.value -> INLINE_GRID_LANES
            else -> null
        }
    }
}

enum class CssFlexDirection(val value: String) {
    ROW("row"),
    ROW_REVERSE("row-reverse"),
    COLUMN("column"),
    COLUMN_REVERSE("column-reverse"),
    ;

    companion object {
        fun parse(value: String): CssFlexDirection? = when (value) {
            ROW.value -> ROW
            ROW_REVERSE.value -> ROW_REVERSE
            COLUMN.value -> COLUMN
            COLUMN_REVERSE.value -> COLUMN_REVERSE
            else -> null
        }
    }
}

enum class CssFlexWrap(val value: String) {
    NOWRAP("nowrap"),
    WRAP("wrap"),
    WRAP_REVERSE("wrap-reverse"),
    WRAP_BALANCE("wrap balance"),
    WRAP_REVERSE_BALANCE("wrap-reverse balance"),
    BALANCE("balance"),
    ;

    companion object {
        fun parse(value: String): CssFlexWrap? = when (value) {
            NOWRAP.value -> NOWRAP
            WRAP.value -> WRAP
            WRAP_REVERSE.value -> WRAP_REVERSE
            WRAP_BALANCE.value -> WRAP_BALANCE
            WRAP_REVERSE_BALANCE.value -> WRAP_REVERSE_BALANCE
            BALANCE.value -> BALANCE
            else -> null
        }
    }
}

enum class CssJustifyContent(val value: String) {
    NORMAL("normal"),

    SPACE_BETWEEN("space-between"),
    SPACE_AROUND("space-around"),
    SPACE_EVENLY("space-evenly"),
    STRETCH("stretch"),

    CENTER("center"),
    START("start"),
    END("end"),
    FLEX_START("flex-start"),
    FLEX_END("flex-end"),

    LEFT("left"),
    RIGHT("right"),

    UNSAFE_CENTER("unsafe center"),
    UNSAFE_START("unsafe start"),
    UNSAFE_END("unsafe end"),
    UNSAFE_FLEX_START("unsafe flex-start"),
    UNSAFE_FLEX_END("unsafe flex-end"),

    UNSAFE_LEFT("unsafe left"),
    UNSAFE_RIGHT("unsafe right"),

    SAFE_CENTER("safe center"),
    SAFE_START("safe start"),
    SAFE_END("safe end"),
    SAFE_FLEX_START("safe flex-start"),
    SAFE_FLEX_END("safe flex-end"),

    SAFE_LEFT("safe left"),
    SAFE_RIGHT("safe right"),
    ;

    companion object {
        fun parse(value: String): CssJustifyContent? = when (value) {
            NORMAL.value -> NORMAL
            SPACE_BETWEEN.value -> SPACE_BETWEEN
            SPACE_AROUND.value -> SPACE_AROUND
            SPACE_EVENLY.value -> SPACE_EVENLY
            STRETCH.value -> STRETCH
            CENTER.value -> CENTER
            START.value -> START
            END.value -> END
            FLEX_START.value -> FLEX_START
            FLEX_END.value -> FLEX_END
            LEFT.value -> LEFT
            RIGHT.value -> RIGHT
            UNSAFE_CENTER.value -> UNSAFE_CENTER
            UNSAFE_START.value -> UNSAFE_START
            UNSAFE_END.value -> UNSAFE_END
            UNSAFE_FLEX_START.value -> UNSAFE_FLEX_START
            UNSAFE_FLEX_END.value -> UNSAFE_FLEX_END
            UNSAFE_LEFT.value -> UNSAFE_LEFT
            UNSAFE_RIGHT.value -> UNSAFE_RIGHT
            SAFE_CENTER.value -> SAFE_CENTER
            SAFE_START.value -> SAFE_START
            SAFE_END.value -> SAFE_END
            SAFE_FLEX_START.value -> SAFE_FLEX_START
            SAFE_FLEX_END.value -> SAFE_FLEX_END
            SAFE_LEFT.value -> SAFE_LEFT
            SAFE_RIGHT.value -> SAFE_RIGHT
            else -> null
        }
    }
}

class CssClassBuilder(
    val selector: String,
) : CssBuilder<CssClass>() {

    val properties = mutableMapOf<String, String>()

    fun string(name: String): Delegate<String?> =
        object : Delegate<String?> {

            override fun getValue(self: Any?, property: KProperty<*>): String? {
                return properties[name]
            }

            override fun setValue(self: Any?, property: KProperty<*>, value: String?) {
                when (value) {
                    null -> properties.remove(name)
                    else -> properties[name] = value
                }
            }
        }

    fun <E : Enum<E>> enum(name: String, set: (String) -> E?, get: E.() -> String): Delegate<E?> =
        object : Delegate<E?> {

            override fun getValue(self: Any?, property: KProperty<*>): E? =
                when (val value = properties[name]) {
                    null -> null
                    else -> set(value)
                }

            override fun setValue(self: Any?, property: KProperty<*>, value: E?) {
                when (value) {
                    null -> properties.remove(name)
                    else -> properties[name] = value.get()
                }
            }
        }

    override fun build(): CssClass {
        return CssClass(
            selector,
            properties.map { (key, value) -> CssProperty(key, value) },
            nodes,
        )
    }

    operator fun get(name: String): String? = properties[name]
    operator fun set(name: String, value: String?) {
        when (value) {
            null -> properties.remove(name)
            else -> properties[name] = value
        }
    }

    fun inherit(name: String) {
        properties[name] = "inherit"
    }

    fun initial(name: String) {
        properties[name] = "initial"
    }

    fun revert(name: String) {
        properties[name] = "revert"
    }

    fun revertLayer(name: String) {
        properties[name] = "revert-layer"
    }

    fun unset(name: String) {
        properties[name] = "unset"
    }

    var accentColor by string("accent-color")
    var alignContent by string("align-content")
    var alignItems by enum("align-items", CssAlignItems::parse, CssAlignItems::value)
    var alignSelf by string("align-self")
    var alignmentBaseline by string("alignment-baseline")
    var all by string("all")
    var anchorName by string("anchor-name")
    var anchorScope by string("anchor-scope")
    var animationComposition by string("animation-composition")
    var animationDelay by string("animation-delay")
    var animationDirection by string("animation-direction")
    var animationDuration by string("animation-duration")
    var animationFillMode by string("animation-fill-mode")
    var animationIterationCount by string("animation-iteration-count")
    var animationName by string("animation-name")
    var animationPlayState by string("animation-play-state")
    var animationRangeEnd by string("animation-range-end")
    var animationRangeStart by string("animation-range-start")
    var animationRange by string("animation-range")
    var animationTimeline by string("animation-timeline")
    var animationTimingFunction by string("animation-timing-function")
    var animation by string("animation")
    var appearance by string("appearance")
    var aspectRatio by string("aspect-ratio")

    var backdropFilter by string("backdrop-filter")
    var backfaceVisibility by string("backface-visibility")
    var backgroundAttachment by string("background-attachment")
    var backgroundBlendMode by string("background-blend-mode")
    var backgroundClip by string("background-clip")
    var backgroundColor by string("background-color")
    var backgroundImage by string("background-image")
    var backgroundOrigin by string("background-origin")
    var backgroundPositionX by string("background-position-x")
    var backgroundPositionY by string("background-position-y")
    var backgroundPosition by string("background-position")
    var backgroundRepeat by string("background-repeat")
    var backgroundRepeatX by string("background-repeat-x")
    var backgroundRepeatY by string("background-repeat-y")
    var backgroundSize by string("background-size")
    var background by string("background")
    var baselineShift by string("baseline-shift")
    var baselineSource by string("baseline-source")
    var blockSize by string("block-size")

    var borderBlockColor by string("border-block-color")
    var borderBlockEndColor by string("border-block-end-color")
    var borderBlockEndStyle by string("border-block-end-style")
    var borderBlockEndWidth by string("border-block-end-width")
    var borderBlockEnd by string("border-block-end")
    var borderBlockStartColor by string("border-block-start-color")
    var borderBlockStartStyle by string("border-block-start-style")
    var borderBlockStartWidth by string("border-block-start-width")
    var borderBlockStart by string("border-block-start")
    var borderBlockStyle by string("border-block-style")
    var borderBlockWidth by string("border-block-width")
    var borderBlock by string("border-block")
    var borderBottomColor by string("border-bottom-color")
    var borderBottomLeftRadius by string("border-bottom-left-radius")
    var borderBottomRightRadius by string("border-bottom-right-radius")
    var borderBottomStyle by string("border-bottom-style")
    var borderBottomWidth by string("border-bottom-width")
    var borderBottom by string("border-bottom")
    var borderCollapse by string("border-collapse")
    var borderColor by string("border-color")
    var borderEndEndRadius by string("border-end-end-radius")
    var borderEndStartRadius by string("border-end-start-radius")
    var borderImageOutset by string("border-image-outset")
    var borderImageRepeat by string("border-image-repeat")
    var borderImageSlice by string("border-image-slice")
    var borderImageSource by string("border-image-source")
    var borderImageWidth by string("border-image-width")
    var borderImage by string("border-image")
    var borderInlineColor by string("border-inline-color")
    var borderInlineEndColor by string("border-inline-end-color")
    var borderInlineEndStyle by string("border-inline-end-style")
    var borderInlineEndWidth by string("border-inline-end-width")
    var borderInlineEnd by string("border-inline-end")
    var borderInlineStartColor by string("border-inline-start-color")
    var borderInlineStartStyle by string("border-inline-start-style")
    var borderInlineStartWidth by string("border-inline-start-width")
    var borderInlineStart by string("border-inline-start")
    var borderInlineStyle by string("border-inline-style")
    var borderInlineWidth by string("border-inline-width")
    var borderInline by string("border-inline")
    var borderLeftColor by string("border-left-color")
    var borderLeftStyle by string("border-left-style")
    var borderLeftWidth by string("border-left-width")
    var borderLeft by string("border-left")
    var borderRadius by string("border-radius")
    var borderRightColor by string("border-right-color")
    var borderRightStyle by string("border-right-style")
    var borderRightWidth by string("border-right-width")
    var borderRight by string("border-right")
    var borderSpacing by string("border-spacing")
    var borderStartEndRadius by string("border-start-end-radius")
    var borderStartStartRadius by string("border-start-start-radius")
    var borderStyle by string("border-style")
    var borderTopColor by string("border-top-color")
    var borderTopLeftRadius by string("border-top-left-radius")
    var borderTopRightRadius by string("border-top-right-radius")
    var borderTopStyle by string("border-top-style")
    var borderTopWidth by string("border-top-width")
    var borderTop by string("border-top")
    var borderWidth by string("border-width")
    var border by string("border")

    var bottom by string("bottom")
    var boxAlign by string("box-align")
    var boxDecorationBreak by string("box-decoration-break")
    var boxDirection by string("box-direction")
    var boxFlexGroup by string("box-flex-group")
    var boxFlex by string("box-flex")
    var boxLines by string("box-lines")
    var boxOrdinalGroup by string("box-ordinal-group")
    var boxOrient by string("box-orient")
    var boxPack by string("box-pack")
    var boxShadow by string("box-shadow")
    var boxSizing by string("box-sizing")
    var breakAfter by string("break-after")
    var breakBefore by string("break-before")
    var breakInside by string("break-inside")
    var captionSide by string("caption-side")
    var caretAnimation by string("caret-animation")
    var caretColor by string("caret-color")
    var caretShape by string("caret-shape")
    var caret by string("caret")
    var clear by string("clear")
    var clipPath by string("clip-path")
    var clipRule by string("clip-rule")
    var clip by string("clip")
    var colorInterpolationFilters by string("color-interpolation-filters")
    var colorInterpolation by string("color-interpolation")
    var colorScheme by string("color-scheme")
    var color by string("color")
    var columnCount by string("column-count")
    var columnFill by string("column-fill")
    var columnGap by string("column-gap")
    var columnHeight by string("column-height")
    var columnRuleColor by string("column-rule-color")
    var columnRuleStyle by string("column-rule-style")
    var columnRuleWidth by string("column-rule-width")
    var columnRule by string("column-rule")
    var columnSpan by string("column-span")
    var columnWidth by string("column-width")
    var columnWrap by string("column-wrap")
    var columns by string("columns")
    var containIntrinsicBlockSize by string("contain-intrinsic-block-size")
    var containIntrinsicHeight by string("contain-intrinsic-height")
    var containIntrinsicInlineSize by string("contain-intrinsic-inline-size")
    var containIntrinsicSize by string("contain-intrinsic-size")
    var containIntrinsicWidth by string("contain-intrinsic-width")
    var contain by string("contain")
    var containerName by string("container-name")
    var containerType by string("container-type")
    var container by string("container")
    var contentVisibility by string("content-visibility")
    var content by string("content")
    var cornerBlockEndShape by string("corner-block-end-shape")
    var cornerBlockStartShape by string("corner-block-start-shape")
    var cornerBottomLeftShape by string("corner-bottom-left-shape")
    var cornerBottomRightShape by string("corner-bottom-right-shape")
    var cornerBottomShape by string("corner-bottom-shape")
    var cornerEndEndShape by string("corner-end-end-shape")
    var cornerEndStartShape by string("corner-end-start-shape")
    var cornerInlineEndShape by string("corner-inline-end-shape")
    var cornerInlineStartShape by string("corner-inline-start-shape")
    var cornerLeftShape by string("corner-left-shape")
    var cornerRightShape by string("corner-right-shape")
    var cornerShape by string("corner-shape")
    var cornerStartEndShape by string("corner-start-end-shape")
    var cornerStartStartShape by string("corner-start-start-shape")
    var cornerTopLeftShape by string("corner-top-left-shape")
    var cornerTopRightShape by string("corner-top-right-shape")
    var cornerTopShape by string("corner-top-shape")
    var counterIncrement by string("counter-increment")
    var counterReset by string("counter-reset")
    var counterSet by string("counter-set")
    var cursor by string("cursor")
    var cx by string("cx")
    var cy by string("cy")

    var d by string("d")
    var direction by string("direction")
    var display by enum("display", CssDisplay::parse, CssDisplay::value)
    var dominantBaseline by string("dominant-baseline")
    var dynamicRangeLimit by string("dynamic-range-limit")
    var emptyCells by string("empty-cells")
    var fieldSizing by string("field-sizing")
    var fillOpacity by string("fill-opacity")
    var fillRule by string("fill-rule")
    var fill by string("fill")
    var filter by string("filter")
    var flexBasis by string("flex-basis")
    var flexDirection by enum("flex-direction", CssFlexDirection::parse, CssFlexDirection::value)
    var flexFlow by string("flex-flow")
    var flexGrow by string("flex-grow")
    var flexShrink by string("flex-shrink")
    var flexWrap by enum("flex-wrap", CssFlexWrap::parse, CssFlexWrap::value)
    var flex by string("flex")
    var float by string("float")
    var floodColor by string("flood-color")
    var floodOpacity by string("flood-opacity")
    var fontFamily by string("font-family")
    var fontFeatureSettings by string("font-feature-settings")
    var fontKerning by string("font-kerning")
    var fontLanguageOverride by string("font-language-override")
    var fontOpticalSizing by string("font-optical-sizing")
    var fontPalette by string("font-palette")
    var fontSizeAdjust by string("font-size-adjust")
    var fontSize by string("font-size")
    var fontSmooth by string("font-smooth")
    var fontStretch by string("font-stretch")
    var fontStyle by string("font-style")
    var fontSynthesisPosition by string("font-synthesis-position")
    var fontSynthesisSmallCaps by string("font-synthesis-small-caps")
    var fontSynthesisStyle by string("font-synthesis-style")
    var fontSynthesisWeight by string("font-synthesis-weight")
    var fontSynthesis by string("font-synthesis")
    var fontVariantAlternates by string("font-variant-alternates")
    var fontVariantCaps by string("font-variant-caps")
    var fontVariantEastAsian by string("font-variant-east-asian")
    var fontVariantEmoji by string("font-variant-emoji")
    var fontVariantLigatures by string("font-variant-ligatures")
    var fontVariantNumeric by string("font-variant-numeric")
    var fontVariantPosition by string("font-variant-position")
    var fontVariant by string("font-variant")
    var fontVariationSettings by string("font-variation-settings")
    var fontWeight by string("font-weight")
    var fontWidth by string("font-width")
    var font by string("font")
    var forcedColorAdjust by string("forced-color-adjust")

    var gap by string("gap")
    var gridArea by string("grid-area")
    var gridAutoColumns by string("grid-auto-columns")
    var gridAutoFlow by string("grid-auto-flow")
    var gridAutoRows by string("grid-auto-rows")
    var gridColumnEnd by string("grid-column-end")
    var gridColumnStart by string("grid-column-start")
    var gridColumn by string("grid-column")
    var gridRowEnd by string("grid-row-end")
    var gridRowStart by string("grid-row-start")
    var gridRow by string("grid-row")
    var gridTemplateAreas by string("grid-template-areas")
    var gridTemplateColumns by string("grid-template-columns")
    var gridTemplateRows by string("grid-template-rows")
    var gridTemplate by string("grid-template")
    var grid by string("grid")
    var hangingPunctuation by string("hanging-punctuation")
    var height by string("height")
    var hyphenateCharacter by string("hyphenate-character")
    var hyphenateLimitChars by string("hyphenate-limit-chars")
    var hyphens by string("hyphens")
    var imageOrientation by string("image-orientation")
    var imageRendering by string("image-rendering")
    var imageResolution by string("image-resolution")
    var initialLetter by string("initial-letter")
    var inlineSize by string("inline-size")
    var insetBlockEnd by string("inset-block-end")
    var insetBlockStart by string("inset-block-start")
    var insetBlock by string("inset-block")
    var insetInlineEnd by string("inset-inline-end")
    var insetInlineStart by string("inset-inline-start")
    var insetInline by string("inset-inline")
    var inset by string("inset")
    var interactivity by string("interactivity")
    var interestDelay by string("interest-delay")
    var interestDelayEnd by string("interest-delay-end")
    var interestDelayStart by string("interest-delay-start")
    var interpolateSize by string("interpolate-size")
    var isolation by string("isolation")

    var justifyContent by enum("justify-content", CssJustifyContent::parse, CssJustifyContent::value)
    var justifyItems by string("justify-items")
    var justifySelf by string("justify-self")
    var left by string("left")
    var letterSpacing by string("letter-spacing")
    var lightingColor by string("lighting-color")
    var lineBreak by string("line-break")
    var lineClamp by string("line-clamp")
    var lineHeightStep by string("line-height-step")
    var lineHeight by string("line-height")
    var listStyleImage by string("list-style-image")
    var listStylePosition by string("list-style-position")
    var listStyleType by string("list-style-type")
    var listStyle by string("list-style")
    var marginBlockEnd by string("margin-block-end")
    var marginBlockStart by string("margin-block-start")
    var marginBlock by string("margin-block")
    var marginBottom by string("margin-bottom")
    var marginInlineEnd by string("margin-inline-end")
    var marginInlineStart by string("margin-inline-start")
    var marginInline by string("margin-inline")
    var marginLeft by string("margin-left")
    var marginRight by string("margin-right")
    var marginTop by string("margin-top")
    var marginTrim by string("margin-trim")
    var margin by string("margin")
    var markerEnd by string("marker-end")
    var markerMid by string("marker-mid")
    var markerStart by string("marker-start")
    var marker by string("marker")
    var maskBorderMode by string("mask-border-mode")
    var maskBorderOutset by string("mask-border-outset")
    var maskBorderRepeat by string("mask-border-repeat")
    var maskBorderSlice by string("mask-border-slice")
    var maskBorderSource by string("mask-border-source")
    var maskBorderWidth by string("mask-border-width")
    var maskBorder by string("mask-border")
    var maskClip by string("mask-clip")
    var maskComposite by string("mask-composite")
    var maskImage by string("mask-image")
    var maskMode by string("mask-mode")
    var maskOrigin by string("mask-origin")
    var maskPosition by string("mask-position")
    var maskRepeat by string("mask-repeat")
    var maskSize by string("mask-size")
    var maskType by string("mask-type")
    var mask by string("mask")
    var mathDepth by string("math-depth")
    var mathShift by string("math-shift")
    var mathStyle by string("math-style")
    var maxBlockSize by string("max-block-size")
    var maxHeight by string("max-height")
    var maxInlineSize by string("max-inline-size")
    var maxWidth by string("max-width")
    var minBlockSize by string("min-block-size")
    var minHeight by string("min-height")
    var minInlineSize by string("min-inline-size")
    var minWidth by string("min-width")
    var mixBlendMode by string("mix-blend-mode")

    var objectFit by string("object-fit")
    var objectPosition by string("object-position")
    var objectViewBox by string("object-view-box")
    var offsetAnchor by string("offset-anchor")
    var offsetDistance by string("offset-distance")
    var offsetPath by string("offset-path")
    var offsetPosition by string("offset-position")
    var offsetRotate by string("offset-rotate")
    var offset by string("offset")
    var opacity by string("opacity")
    var order by string("order")
    var orphans by string("orphans")
    var outlineColor by string("outline-color")
    var outlineOffset by string("outline-offset")
    var outlineStyle by string("outline-style")
    var outlineWidth by string("outline-width")
    var outline by string("outline")
    var overflowAnchor by string("overflow-anchor")
    var overflowBlock by string("overflow-block")
    var overflowClipMargin by string("overflow-clip-margin")
    var overflowInline by string("overflow-inline")
    var overflowWrap by string("overflow-wrap")
    var overflowX by string("overflow-x")
    var overflowY by string("overflow-y")
    var overflow by string("overflow")
    var overlay by string("overlay")
    var overscrollBehaviorBlock by string("overscroll-behavior-block")
    var overscrollBehaviorInline by string("overscroll-behavior-inline")
    var overscrollBehaviorX by string("overscroll-behavior-x")
    var overscrollBehaviorY by string("overscroll-behavior-y")
    var overscrollBehavior by string("overscroll-behavior")
    var paddingBlockEnd by string("padding-block-end")
    var paddingBlockStart by string("padding-block-start")
    var paddingBlock by string("padding-block")
    var paddingBottom by string("padding-bottom")
    var paddingInlineEnd by string("padding-inline-end")
    var paddingInlineStart by string("padding-inline-start")
    var paddingInline by string("padding-inline")
    var paddingLeft by string("padding-left")
    var paddingRight by string("padding-right")
    var paddingTop by string("padding-top")
    var padding by string("padding")
    var pageBreakAfter by string("page-break-after")
    var pageBreakBefore by string("page-break-before")
    var pageBreakInside by string("page-break-inside")
    var page by string("page")
    var paintOrder by string("paint-order")
    var pathLength by string("path-length")
    var perspectiveOrigin by string("perspective-origin")
    var perspective by string("perspective")
    var placeContent by string("place-content")
    var placeItems by string("place-items")
    var placeSelf by string("place-self")
    var pointerEvents by string("pointer-events")
    var positionAnchor by string("position-anchor")
    var positionArea by string("position-area")
    var positionTryFallbacks by string("position-try-fallbacks")
    var positionTryOrder by string("position-try-order")
    var positionTry by string("position-try")
    var positionVisibility by string("position-visibility")
    var position by string("position")
    var printColorAdjust by string("print-color-adjust")

    var quotes by string("quotes")
    var r by string("r")
    var readingFlow by string("reading-flow")
    var readingOrder by string("reading-order")
    var resize by string("resize")
    var right by string("right")
    var rotate by string("rotate")
    var rowGap by string("row-gap")
    var rubyAlign by string("ruby-align")
    var rubyOverhang by string("ruby-overhang")
    var rubyPosition by string("ruby-position")
    var rx by string("rx")
    var ry by string("ry")
    var scale by string("scale")
    var scrollBehavior by string("scroll-behavior")
    var scrollInitialTarget by string("scroll-initial-target")
    var scrollMarginBlockEnd by string("scroll-margin-block-end")
    var scrollMarginBlockStart by string("scroll-margin-block-start")
    var scrollMarginBlock by string("scroll-margin-block")
    var scrollMarginBottom by string("scroll-margin-bottom")
    var scrollMarginInlineEnd by string("scroll-margin-inline-end")
    var scrollMarginInlineStart by string("scroll-margin-inline-start")
    var scrollMarginInline by string("scroll-margin-inline")
    var scrollMarginLeft by string("scroll-margin-left")
    var scrollMarginRight by string("scroll-margin-right")
    var scrollMarginTop by string("scroll-margin-top")
    var scrollMargin by string("scroll-margin")
    var scrollMarkerGroup by string("scroll-marker-group")
    var scrollPaddingBlockEnd by string("scroll-padding-block-end")
    var scrollPaddingBlockStart by string("scroll-padding-block-start")
    var scrollPaddingBlock by string("scroll-padding-block")
    var scrollPaddingBottom by string("scroll-padding-bottom")
    var scrollPaddingInlineEnd by string("scroll-padding-inline-end")
    var scrollPaddingInlineStart by string("scroll-padding-inline-start")
    var scrollPaddingInline by string("scroll-padding-inline")
    var scrollPaddingLeft by string("scroll-padding-left")
    var scrollPaddingRight by string("scroll-padding-right")
    var scrollPaddingTop by string("scroll-padding-top")
    var scrollPadding by string("scroll-padding")
    var scrollSnapAlign by string("scroll-snap-align")
    var scrollSnapStop by string("scroll-snap-stop")
    var scrollSnapType by string("scroll-snap-type")
    var scrollTargetGroup by string("scroll-target-group")
    var scrollTimelineAxis by string("scroll-timeline-axis")
    var scrollTimelineName by string("scroll-timeline-name")
    var scrollTimeline by string("scroll-timeline")
    var scrollbarColor by string("scrollbar-color")
    var scrollbarGutter by string("scrollbar-gutter")
    var scrollbarWidth by string("scrollbar-width")
    var shapeImageThreshold by string("shape-image-threshold")
    var shapeMargin by string("shape-margin")
    var shapeOutside by string("shape-outside")
    var shapeRendering by string("shape-rendering")
    var speakAs by string("speak-as")
    var stopColor by string("stop-color")
    var stopOpacity by string("stop-opacity")
    var strokeDasharray by string("stroke-dasharray")
    var strokeDashoffset by string("stroke-dashoffset")
    var strokeLinecap by string("stroke-linecap")
    var strokeLinejoin by string("stroke-linejoin")
    var strokeMiterlimit by string("stroke-miterlimit")
    var strokeOpacity by string("stroke-opacity")
    var strokeWidth by string("stroke-width")
    var stroke by string("stroke")

    var tabSize by string("tab-size")
    var tableLayout by string("table-layout")
    var textAlignLast by string("text-align-last")
    var textAlign by string("text-align")
    var textAnchor by string("text-anchor")
    var textAutospace by string("text-autospace")
    var textBoxEdge by string("text-box-edge")
    var textBoxTrim by string("text-box-trim")
    var textBox by string("text-box")
    var textCombineUpright by string("text-combine-upright")
    var textDecorationColor by string("text-decoration-color")
    var textDecorationInset by string("text-decoration-inset")
    var textDecorationLine by string("text-decoration-line")
    var textDecorationSkipInk by string("text-decoration-skip-ink")
    var textDecorationSkip by string("text-decoration-skip")
    var textDecorationStyle by string("text-decoration-style")
    var textDecorationThickness by string("text-decoration-thickness")
    var textDecoration by string("text-decoration")
    var textEmphasisColor by string("text-emphasis-color")
    var textEmphasisPosition by string("text-emphasis-position")
    var textEmphasisStyle by string("text-emphasis-style")
    var textEmphasis by string("text-emphasis")
    var textIndent by string("text-indent")
    var textJustify by string("text-justify")
    var textOrientation by string("text-orientation")
    var textOverflow by string("text-overflow")
    var textRendering by string("text-rendering")
    var textShadow by string("text-shadow")
    var textSizeAdjust by string("text-size-adjust")
    var textSpacingTrim by string("text-spacing-trim")
    var textTransform by string("text-transform")
    var textUnderlineOffset by string("text-underline-offset")
    var textUnderlinePosition by string("text-underline-position")
    var textWrapMode by string("text-wrap-mode")
    var textWrapStyle by string("text-wrap-style")
    var textWrap by string("text-wrap")
    var timelineScope by string("timeline-scope")
    var top by string("top")
    var touchAction by string("touch-action")
    var transformBox by string("transform-box")
    var transformOrigin by string("transform-origin")
    var transformStyle by string("transform-style")
    var transform by string("transform")
    var transitionBehavior by string("transition-behavior")
    var transitionDelay by string("transition-delay")
    var transitionDuration by string("transition-duration")
    var transitionProperty by string("transition-property")
    var transitionTimingFunction by string("transition-timing-function")
    var transition by string("transition")
    var translate by string("translate")
    var unicodeBidi by string("unicode-bidi")
    var userModify by string("user-modify")
    var userSelect by string("user-select")
    var vectorEffect by string("vector-effect")
    var verticalAlign by string("vertical-align")
    var viewTimelineAxis by string("view-timeline-axis")
    var viewTimelineInset by string("view-timeline-inset")
    var viewTimelineName by string("view-timeline-name")
    var viewTimeline by string("view-timeline")
    var viewTransitionClass by string("view-transition-class")
    var viewTransitionName by string("view-transition-name")
    var visibility by string("visibility")
    var whiteSpaceCollapse by string("white-space-collapse")
    var whiteSpace by string("white-space")
    var widows by string("widows")
    var width by string("width")
    var willChange by string("will-change")
    var wordBreak by string("word-break")
    var wordSpacing by string("word-spacing")
    var writingMode by string("writing-mode")
    var x by string("x")
    var y by string("y")
    var zIndex by string("z-index")
    var zoom by string("zoom")

    var mozFloatEdge by string("-moz-float-edge")
    var mozForceBrokenImageIcon by string("-moz-force-broken-image-icon")
    var mozOrient by string("-moz-orient")
    var mozUserFocus by string("-moz-user-focus")
    var mozUserInput by string("-moz-user-input")

    var webkitBoxReflect by string("-webkit-box-reflect")
    var webkitBorderBefore by string("-webkit-border-before")
    var webkitMaskBoxImage by string("-webkit-mask-box-image")
    var webkitMaskComposite by string("-webkit-mask-composite")
    var webkitMaskPositionX by string("-webkit-mask-position-x")
    var webkitMaskPositionY by string("-webkit-mask-position-y")
    var webkitMaskRepeatX by string("-webkit-mask-repeat-x")
    var webkitMaskRepeatY by string("-webkit-mask-repeat-y")
    var webkitTapHighlightColor by string("-webkit-tap-highlight-color")
    var webkitTextFillColor by string("-webkit-text-fill-color")
    var webkitTextSecurity by string("-webkit-text-security")
    var webkitTextStroke by string("-webkit-text-stroke")
    var webkitTextStrokeColor by string("-webkit-text-stroke-color")
    var webkitTextStrokeWidth by string("-webkit-text-stroke-width")
    var webkitTouchCallout by string("-webkit-touch-callout")
}
