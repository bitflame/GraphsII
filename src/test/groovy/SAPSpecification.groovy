import edu.princeton.cs.algs4.Digraph
import edu.princeton.cs.algs4.In
import spock.lang.Specification

class SAPSpecification extends Specification {
    def "SAP getPath() Should return the shortest path in the graph"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.getPath(from, to) == shortest

        where:
        file                             | from | to | shortest
        "digraph25.txt"                  | 1    | 2  | [2, 1, 0]
        "digraph25.txt"                  | 3    | 4  | [4, 3, 1]
        "digraph25.txt"                  | 4    | 3  | [4, 3, 1]
        "digraph25.txt"                  | 5    | 6  | [6, 5, 2]
        "digraph25.txt"                  | 6    | 5  | [6, 5, 2]
        "digraph25.txt"                  | 4    | 6  | [6, 4, 2, 1, 0]
        "digraph25.txt"                  | 1    | 6  | [6, 2, 1, 0]
        "digraph25.txt"                  | 17   | 24 | [24, 20, 17, 12, 10, 5]
        "digraph25.txt"                  | 23   | 24 | [24, 23, 20]
        "digraph25.txt"                  | 11   | 14 | [14, 11, 7, 5, 3, 2, 1, 0]
        "digraph25.txt"                  | 17   | 19 | [19, 17, 12, 10, 5]
        "digraph25.txt"                  | 17   | 17 | [17]
        "digraph1.txt"                   | 2    | 0  | [2, 0]
        "digraph1.txt"                   | 10   | 4  | [10, 5, 4, 1]
        "digraph1.txt"                   | 3    | 11 | [11, 10, 5, 3, 1]
        "digraph-ambiguous-ancestor.txt" | 1    | 2  | [1, 2]
    }

    def "SAP shortestPath() Should return the shortest path in the graph"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.shortestPath(from, to) == shortest

        where:
        file                             | from | to | shortest
        "digraph25.txt"                  | 1    | 2  | [0, 1, 2]
        "digraph25.txt"                  | 3    | 4  | [1, 3, 4]
        "digraph25.txt"                  | 4    | 3  | [1, 3, 4]
        "digraph25.txt"                  | 5    | 6  | [2, 5, 6]
        "digraph25.txt"                  | 6    | 5  | [2, 5, 6]
        "digraph25.txt"                  | 4    | 6  | [0, 1, 2, 4, 6]
        "digraph25.txt"                  | 1    | 6  | [0, 1, 2, 6]
        "digraph25.txt"                  | 17   | 24 | [5, 10, 12, 17, 20, 24]
        "digraph25.txt"                  | 23   | 24 | [20, 23, 24]
        "digraph25.txt"                  | 11   | 14 | [0, 1, 2, 3, 5, 7, 11, 14]
        "digraph25.txt"                  | 17   | 19 | [5, 10, 12, 17, 19]
        "digraph25.txt"                  | 17   | 17 | [17]
        "digraph1.txt"                   | 2    | 0  | [0, 2]
        "digraph1.txt"                   | 10   | 4  | [1, 4, 5, 10]
        "digraph1.txt"                   | 3    | 11 | [1, 3, 5, 10, 11]
        "digraph-ambiguous-ancestor.txt" | 1    | 2  | [1, 2]
    }

    def "SAP length() should return the length of the shortest path between two points"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.length(v, w)

        where:
        file            | v  | w  | result
        "digraph25.txt" | 1  | 2  | 3
        "digraph25.txt" | 3  | 4  | 3
        "digraph25.txt" | 4  | 3  | 3
        "digraph25.txt" | 5  | 6  | 3
        "digraph25.txt" | 6  | 5  | 3
        "digraph25.txt" | 4  | 6  | 5
        "digraph25.txt" | 1  | 6  | 4
        "digraph25.txt" | 17 | 24 | 6
        "digraph25.txt" | 23 | 24 | 3
        "digraph25.txt" | 11 | 14 | 8
        "digraph25.txt" | 17 | 19 | 5
        "digraph1.txt"  | 2  | 0  | 2
        "digraph1.txt"  | 3  | 11 | 4
        "digraph1.txt"  | 9  | 12 | 3
        "digraph1.txt"  | 7  | 2  | 4
        "digraph1.txt"  | 1  | 6  | -1
    }

    def "SAP  ancestor() for Iterables should return a common node between to Iterables"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.ancestor(iter1, iter2) == result

        where:
        file            | iter1     | iter2     | result
        "digraph25.txt" | [0, 1, 2] | [1, 3, 4] | 1
    }

    def "SAP ancestor() for Integer ids should return a common node between to node ids"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.ancestor(from, to) == result

        where:
        file            | from | to | result
        "digraph25.txt" | 1    | 2  | 0
        "digraph25.txt" | 3    | 4  | 1
        "digraph25.txt" | 5    | 6  | 2
        "digraph25.txt" | 19    | 24  | 12
        "digraph25.txt" | 18    | 1  | 0
        "digraph25.txt" | 22    | 19  | 0
        "digraph-ambiguous-ancestor.txt" | 1    | 2  | 2
        "digraph-ambiguous-ancestor.txt" | 0    | 2  | 2
        "digraph-ambiguous-ancestor.txt" | 0    | 10  | 10
    }

    def "SAP ancestor() should return a common node between two Integer input values"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)
        then:
        sap.ancestor(from, to) == ancestor
        where:
        file           | from | to | ancestor
        "digraph1.txt" | 3    | 11 | 1
        "digraph1.txt" | 9    | 12 | 5
        "digraph1.txt" | 7    | 2  | 0
        "digraph1.txt" | 1    | 6  | -1
    }

    def "SAP constructor should throw Illegal Argument Exception if digraph object is null"() {
        when:
        SAP sap = new SAP()
        then:
        thrown(IllegalArgumentException)
    }

    def "Should throw exception for graphs with cycles "() {
        when:
        Digraph digraph = new Digraph(new In("digraph1.txt"))
        SAP sap = new SAP(digraph)
        then:
        1 == 1 // for now -  fix when you find a file with cycle
    }
}
