package org.bentallab.neo4j.server.plugins.ConstraintedShortestPath;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.*;

import java.util.Map;

/**
 * Created by lab on 27/10/15.
 */

public class FindMinimumConstraints  extends ServerPlugin {
    @Description( "Find the minimum constrains require by a relationship type for all given nodes to be connected together." )
    @PluginTarget(GraphDatabaseService.class)
    public String findMinimumConstraints(
            @Source GraphDatabaseService graphDb,
            @Description( "The relationship type to searching for the minimum constrains. ")
            @Parameter( name = "type" ) String type,
            @Description("An object of given constraints")
            @Parameter(name = "constraints") final Map constraints
    ){
        return "";
    };
}
