import edu.princeton.cs.algs4.Digraph
import edu.princeton.cs.algs4.In
import spock.lang.Specification

class SAPSpecification extends Specification {
    def "Name"() {
        when:
        Digraph digraph = new Digraph(new In("digraph25.txt"))
        SAP sap = new SAP(digraph)

        then:
        sap.getPath(from, to) == shortest

        where:
        from | to | shortest
        1    | 2  | [2, 1, 0]
        3    | 4  | [4, 3, 1]
        4    | 3  | [4, 3, 1]
        5    | 6  | [6, 5, 2]
        6    | 5  | [6, 5, 2]
        4    | 6  | [6, 4, 2, 1, 0]
        1    | 6  | [6, 2, 1, 0]
        17   | 24 | [24, 20, 17, 12, 10, 5]
        23   | 24 | [24, 23, 20]
        11   | 14 | [14, 11, 5, 4, 2, 1]
        17   | 19 | [19, 17, 12, 10, 5]

    }
}
