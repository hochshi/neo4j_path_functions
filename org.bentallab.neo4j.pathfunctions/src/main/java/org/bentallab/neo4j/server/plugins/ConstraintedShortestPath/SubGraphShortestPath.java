package org.bentallab.neo4j.server.plugins.ConstraintedShortestPath;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.server.plugins.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lab on 24/10/15.
 */
public class SubGraphShortestPath extends ServerPlugin {
    @Description( "Find the shortest path between two nodes under given relationship constraints." )
    @PluginTarget(GraphDatabaseService.class)
    public Iterable<Path> SubGraphShortestPath(
            @Source GraphDatabaseService graphDb,
            @Description("The node to find the shortest path to.") @Parameter( name = "target" ) Node target,
            @Description( "The relationship types to follow when searching for the shortest path(s). " +
                    "Order is insignificant, if omitted all types are followed." )
            @Parameter( name = "types", optional = true ) String[] types,
            @Description( "The maximum path length to search for, default value (if omitted) is 4." )
            @Parameter( name = "depth", optional = true ) Integer depth,
            @Description("An object of given constraints")
            @Parameter(name = "constraints") final Map constraints,
            @Description("Default weight of relationships the don't match the set of supplied constraints")
            @Parameter(name = "defaultValue", optional = true) Double defaultValue
    ){
        ArrayList<Path> paths = new ArrayList<Path>();
        File subDbfile = new File("/var/lib/neo4j/data/subgraph.db");
        boolean exists = subDbfile.exists();
        GraphDatabaseService subDB  = new GraphDatabaseFactory().newEmbeddedDatabase("/var/lib/neo4j/data/subgraph.db");
        if (!exists) {
            exists = createSubGraph(graphDb, subDB, constraints);
        }
        if (!exists) {
            return paths;
        }
        return paths;
    }

    private Boolean createSubGraph(GraphDatabaseService graphDb, GraphDatabaseService subDb, Map constraints) {
        try {
            Transaction tx = graphDb.beginTx();
            Result result = graphDb.execute("MATCH (d1:ECODDomain)-[r:DISTANCE]-(d2:ECODDomain) WHERE r.rmsd <= 3 AND r.length >= 30 AND r.psim >= 30 return DISTINCT d1 AS domain, r AS distance;");
        }
        return false;
    }
}
