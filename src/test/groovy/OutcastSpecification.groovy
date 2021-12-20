import edu.princeton.cs.algs4.In
import spock.lang.Specification

class OutcastSpecification extends Specification {
    def "Outcast should calculate"() {
        when:
        WordNet wordNet = new WordNet(synsets, hypernyms);
        Outcast outcast = new Outcast(wordNet)

        then:
        outcast.outcast(new In(outcastInput).readAllStrings()) == result

        where:
        synsets        | hypernyms       | outcastInput    | result
        "synsets.txt" | "hypernyms.txt" | "outcast5.txt"  | "table"
        "synsets.txt" | "hypernyms.txt" | "outcast8.txt"  | "bed"
        "synsets.txt" | "hypernyms.txt" | "outcast11.txt" | "potato"
    }
}
