package ru.art.test.specification.core

import ru.art.core.extension.FileExtensions
import spock.lang.IgnoreIf
import spock.lang.Specification

import static java.lang.System.getenv
import static ru.art.core.extension.InputStreamExtensions.toByteArray

@IgnoreIf({ getenv('TRAVIS') as boolean })
class InputStreamExtensionsSpecification  extends Specification {
    def "should read file with size more than buffer size"() {
        setup:
        def buffer = 8 * 1024 * 1024
        def stream = "http://www.ovh.net/files/10Mio.dat".toURI().toURL().content as InputStream
        def file = new File("temp.bin")
        new FileOutputStream(file).with {
            it.write(stream.readAllBytes())
            it.flush()
            it.close()
        }

        when:
        def array = FileExtensions.readFileBytes(file.absolutePath, buffer)

        then:
        array.size() > buffer

        cleanup:
        file.delete()
    }

    def "should read input stream with size more than buffer"() {
        setup:
        def buffer = 8 * 1024 * 1024
        def stream = "http://www.ovh.net/files/10Mio.dat".toURI().toURL().content as InputStream

        when:
        def array = toByteArray(stream, buffer)

        then:
        array.size() > buffer

        cleanup:
        stream.close()
    }
}
