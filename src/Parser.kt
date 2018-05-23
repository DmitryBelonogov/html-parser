import ParserState.*
import java.util.*
import kotlin.collections.ArrayList

class Parser(private val data: String) {

    val elements: ArrayList<Element> = ArrayList()

    private var state: ParserState

    fun getElementsByTag(tag: String) : List<Element> {
        val res = ArrayList<Element>()

        for(element in elements) {
            if(element.name == tag) {
                res.add(element)
            }
        }

        return res
    }

    fun parse() {
        var char: Char
        val attrs = StringBuilder()
        val name = StringBuilder()
        var depth = 0
        var previousClosed = true

        for (pos in 0 until data.length) {
            char = data[pos]
            when (char) {
                '<' -> {
                    state = START_GET_NAME
                    name.setLength(0)
                    if(!previousClosed) {
                        depth += 1
                    }
                }
                '>' -> {
                    if (state == GET_ATTRS || state == GET_NAME) {
                        state = FULL_NAME
                        val element = Element(name.toString(), attrs.toString(), "")
                        element.startPosition = pos + 1
                        stack.push(element)
                        attrs.setLength(0)
                        if(previousClosed) {
                            depth -= 1
                        }
                        previousClosed = false
                    }
                    if(state == CLOSE_ELEMENT) {
                        val element = stack.pop()
                        if(pos - element.name.length - 2 > element.startPosition) {
                            element.content = data.substring(element.startPosition, pos - element.name.length - 2)
                        }
                        stack.peek().appendChild(element)
                        elements.add(element)
                        previousClosed = true
                    }
                }
                else -> {
                    when (state) {
                        GET_NAME -> {
                            if (char != ' ') {
                                name.append(char)
                            } else {
                                state = GET_ATTRS
                            }
                        }
                        START_GET_NAME -> {
                            if (char != '/') {
                                state = GET_NAME
                                name.append(char)
                            } else {
                                state = CLOSE_ELEMENT
                            }
                        }
                        GET_ATTRS -> {
                            attrs.append(char)
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }



    private val stack = Stack<Element>()

    init {
        state = EMPTY
        stack.push(Element())
    }

}