package by.dev.madhead.doktor.util.render

import by.dev.madhead.doktor.model.RenderedDok
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class AsciiDoc {
	@DataProvider(name = "data")
	fun data(): Array<Array<Any>> {
		val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

		return listOf(
			"simple",
			"steeper",
			"luke"
		).map {
			arrayOf(
				this::class.java.getResourceAsStream("/by/dev/madhead/doktor/util/render/AsciiDoc/${it}.asc").bufferedReader().use { it.readText() },
				objectMapper.readValue<RenderedDok>(this::class.java.getResourceAsStream("/by/dev/madhead/doktor/util/render/AsciiDoc/${it}.yml"))
			)
		}.toTypedArray()
	}


	@Test(dataProvider = "data")
	fun testValid(input: String, output: RenderedDok) {
		Assert.assertEquals(asciiDoc(input), output, "Unexpected output")
	}

	@Test(expectedExceptions = arrayOf(RenderException::class))
	fun testInvalid() {
		asciiDoc("# Content without front matter is not valid.")
	}
}