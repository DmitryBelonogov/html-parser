data class Element(
        var name: String,
        private var attrs: String,
        var content: String
) {

    private var parent: Element? = null
    private var children = mutableListOf<Element>()
    private var childrenSize = 0
    private var position = 0

    private var attributes = HashMap<String, String>()

    var startPosition: Int = 0

    fun appendChild(child: Element) {
        child.parent = this
        child.position = childrenSize
        children.add(childrenSize, child)
        childrenSize += 1
    }

    fun getNextSibling() : Element? {
        if(position == parent!!.childrenSize - 1) {
            return null
        }
        return parent!!.getChildren()[position + 1]
    }

    fun getChildren(): MutableList<Element> {
        return children
    }

    fun getAttribute(name: String) : String {
        return attributes[name].orEmpty()
    }

    fun getText() : String {
        val text = StringBuilder()
        var skip = false

        for(char in content) {
            if(char == '<') {
                skip = true
            }
            if(!skip) {
                text.append(char)
            } else {
                if(char == '>') {
                    skip = false
                }
            }
        }

        return text.toString()
    }

    private fun getAttributes() {
        if(attrs.isNotEmpty()) {
            val attrsArray = attrs.split(" ")

            if(attrsArray.isNotEmpty()) {
                for (attr in attrsArray) {
                    val delimiter = attr.indexOf('=')

                    if(delimiter > 0) {
                        attributes[attr.substring(0, delimiter)] = attr.substring(delimiter + 2, attr.length - 1)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        var s = name
        if (!children.isEmpty()) {
            s += " - " + parent?.name + " - {" + children.map { it.name } + " } " + children.size
        }
        return s
    }

    constructor() : this("root", "", "")

    init {
        getAttributes()
    }

}