import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ParserTest {

    private lateinit var parser : Parser

    @BeforeEach
    fun setUp() {
        parser = Parser(" " +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1 id=\"title-id\" class=\"title-class\">My First Heading</h1>\n" +
                "<p>My first paragraph.</p>\n" +
                "<p>My <b>second</b> paragraph.</p>\n" +
                "<p>My <b>another</b> paragraph. <a href=\"\">aa</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html> ")
        parser.parse()
    }

    @Test
    fun `should return element children`() {
        val element = parser.getElementsByTag("p")[2]

        assertEquals(2, element.getChildren().size)
        assertEquals("b", element.getChildren()[0].name)
        assertEquals("a", element.getChildren()[1].name)
    }

    @Test
    fun `should return element attributes`() {
        val element = parser.getElementsByTag("h1")[0]

        assertEquals("", element.getAttribute(""))
        assertEquals("title-id", element.getAttribute("id"))
        assertEquals("title-class", element.getAttribute("class"))
    }

    @Test
    fun `should return parent element`() {

    }

    @Test
    fun `should return next sibling`() {
        var element = parser.getElementsByTag("h1")[0]
        element = element.getNextSibling()!!
        assertEquals("p", element.name)
        element = element.getNextSibling()!!
        assertEquals("p", element.name)
        element = element.getNextSibling()!!
        assertEquals("p", element.name)
        assertEquals(null, element.getNextSibling())
    }

    @Test
    fun `should return elements by tags`() {
        val elements = parser.getElementsByTag("p")

        assertEquals(elements.size, 3)
        assertEquals(elements[0].name, "p")
        assertEquals(elements[0].getText(), "My first paragraph.")
        assertEquals(elements[1].getText(), "My second paragraph.")
    }
}