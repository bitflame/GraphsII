import edu.princeton.cs.algs4.Digraph
import edu.princeton.cs.algs4.DirectedCycle
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
        "digraph3.txt"                   | 1    | 2  | [0,2]
        "digraph25.txt"                  | 2    | 0  | [0, 2]
        "digraph25.txt"                  | 0    | 2  | [0, 2]
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
        "digraph25.txt"                  | 13   | 6  | [0, 1, 2, 3, 6, 7, 13]
        "digraph25.txt"                  | 23   | 17 | [5, 10, 12, 17, 20, 23]
        "digraph25.txt"                  | 23   | 6  | [2, 5, 6, 12, 20, 23]
        "digraph25.txt"                  | 13   | 16 | [3, 7, 9, 13, 16]
        "digraph1.txt"                   | 2    | 0  | [0, 2]
        "digraph1.txt"                   | 10   | 4  | [1, 4, 5, 10]
        "digraph1.txt"                   | 3    | 11 | [1, 3, 5, 10, 11]
        "digraph1.txt"                   | 7    | 2  | [0, 1, 2, 3, 7]
        "digraph-ambiguous-ancestor.txt" | 1    | 2  | [1, 2]
        "digraph-ambiguous-ancestor.txt" | 0    | 2  | [0, 1, 2]
        "digraph-ambiguous-ancestor.txt" | 0    | 3  | [0, 1, 2, 3]
        "digraph-ambiguous-ancestor.txt" | 4    | 3  | [2, 3, 4]
        "digraph-ambiguous-ancestor.txt" | 4    | 3  | [2, 3, 4]
        "digraph-ambiguous-ancestor.txt" | 8    | 6  | [6, 7, 8]
        // "digraph-ambiguous-ancestor.txt" | 9    | 5  | [5, 6, 7, 8, 9]
        "digraph-ambiguous-ancestor.txt" | 9    | 5  | []
    }

    def "SAP length() should return the length of the shortest path between two points"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.length(v, w)

        where:
        file            | v  | w  | result
        "digraph25.txt" | 2  | 0  | 1
        "digraph25.txt" | 0  | 2  | 1
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
        file            | iter1        | iter2       | result
        "digraph25.txt" | [13, 23, 24] | [6, 16, 17] | 3
    }

    def "SAP ancestor() for Integer ids should return a common node between to node ids"() {
        when:
        Digraph digraph = new Digraph(new In(file))
        SAP sap = new SAP(digraph)

        then:
        sap.ancestor(from, to) == result

        where:
        file                             | from | to | result
        "digraph25.txt"                  | 1    | 2  | 0
        "digraph25.txt"                  | 3    | 4  | 1
        "digraph25.txt"                  | 5    | 6  | 2
        "digraph25.txt"                  | 19   | 24 | 12
        "digraph25.txt"                  | 18   | 1  | 0
        "digraph25.txt"                  | 22   | 19 | 0
        "digraph25.txt"                  | 13   | 6  | 0
        "digraph1.txt"                   | 3    | 11 | 1
        "digraph1.txt"                   | 9    | 12 | 5
        "digraph1.txt"                   | 7    | 2  | 0
        "digraph1.txt"                   | 1    | 6  | -1
        "digraph-ambiguous-ancestor.txt" | 1    | 2  | 2
        "digraph-ambiguous-ancestor.txt" | 0    | 2  | 2
        "digraph-ambiguous-ancestor.txt" | 0    | 10 | 10

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

    def "ancestor() should throw Illegal Argument Exception if one of the arguments is null or has null in it "() {
        when:
        Digraph digraph = new Digraph(new In("digraph1.txt"))
        SAP sap = new SAP(digraph)
        //[13, 23, 24] | [6, 16, 17]
        Iterable<Integer> iter_one = [null, 23, 24]
        Iterable<Integer> iter_two = [6, 16, 17]
        sap.length(iter_one, iter_two);
        then:
        thrown(IllegalArgumentException)
    }

    def "should identify input for graphs that are not rooted DAGs"() {
        when:
        Connected connected = new Connected()
        Digraph digraph = connected.createGraph(file, size)
        then:
        connected.isItConnected(digraph) == result
        where:
        file                              | size | result
        "hypernyms3InvalidCycle.txt"      | 3    | true
        "hypernyms6InvalidCycle+Path.txt" | 6    | false
        "hypernyms6InvalidCycle.txt"      | 6    | true
    }

    def "isRooted() should work"() {
        when:
        Connected connected = new Connected()
        Digraph digraph = connected.createGraph(file, size)
        then:
        connected.isRooted(digraph) == result
        where:
        file                            | size | result
        "hypernyms3InvalidTwoRoots.txt" | 3    | false
    }

    def "should spot cycles in files"() {
        when:
        Connected connected = new Connected();
        Digraph digraph = connected.createGraph(file, size)
        DirectedCycle cycleFinder = new DirectedCycle(digraph)
        then:
        cycleFinder.hasCycle() == result
        where:
        file                              | size | result
        "hypernyms3InvalidCycle.txt"      | 3    | true
        "hypernyms6InvalidCycle+Path.txt" | 6    | false
        "hypernyms6InvalidCycle.txt"      | 6    | true
    }
}
