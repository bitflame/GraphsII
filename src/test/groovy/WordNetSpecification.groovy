import spock.lang.Specification

class WordNetSpecification extends Specification {
    def "WordNet Distance should calculate "() {
        when:
        WordNet wordNet = new WordNet(synset, hypernym)
        then:
        wordNet.distance(nounA, nounB) == result
        where:
        synset        | hypernym        | nounA  | nounB  | result
        "synsets.txt" | "hypernyms.txt" | "worm" | "bird" | 5
    }

    def "WordNet isNoun should work"() {
        when:
        WordNet wordNet = new WordNet(synset, hypernym)
        then:
        wordNet.isNoun(word) == result
        where:
        synset        | hypernym        | word     | result
        "synsets.txt" | "hypernyms.txt" | "entity" | true

    }
}
