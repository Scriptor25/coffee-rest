package dev.scriptor.ui.css.builder

import dev.scriptor.ui.css.CssClass
import dev.scriptor.ui.css.CssProperty

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

    val properties = mutableMapOf<String, String?>()

    fun <E : Enum<E>> enum(name: String, parse: (String) -> E?): E? =
        when (val value = properties[name]) {
            null -> null
            else -> parse(value)
        }

    fun <E : Enum<E>> enum(name: String, value: E?, get: E.() -> String) {
        properties[name] = when (value) {
            null -> null
            else -> value.get()
        }
    }

    override fun build(): CssClass {
        return CssClass(
            selector,
            properties.mapNotNull { (key, value) ->
                if (value != null)
                    CssProperty(key, value)
                else
                    null
            },
            nodes,
        )
    }

    operator fun get(name: String): String? = properties[name]
    operator fun set(name: String, value: String?) {
        properties[name] = value
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

    var alignItems: CssAlignItems?
        get() = enum("align-items", CssAlignItems::parse)
        set(value) {
            enum("align-items", value, CssAlignItems::value)
        }

    var display: CssDisplay?
        get() = enum("display", CssDisplay::parse)
        set(value) {
            enum("display", value, CssDisplay::value)
        }

    var flexDirection: CssFlexDirection?
        get() = enum("flex-direction", CssFlexDirection::parse)
        set(value) {
            enum("flex-direction", value, CssFlexDirection::value)
        }

    var flexWrap: CssFlexWrap?
        get() = enum("flex-wrap", CssFlexWrap::parse)
        set(value) {
            enum("flex-wrap", value, CssFlexWrap::value)
        }

    var justifyContent: CssJustifyContent?
        get() = enum("justify-content", CssJustifyContent::parse)
        set(value) {
            enum("justify-content", value, CssJustifyContent::value)
        }
}
