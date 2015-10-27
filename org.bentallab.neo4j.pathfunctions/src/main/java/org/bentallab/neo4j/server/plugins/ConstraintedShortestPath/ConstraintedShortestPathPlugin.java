package org.bentallab.neo4j.server.plugins.ConstraintedShortestPath;

import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.*;
import org.neo4j.server.plugins.*;
import org.neo4j.server.rest.repr.Representation;

import java.util.*;

/**
 * Created by lab on 20/10/15.
 */
public class ConstraintedShortestPathPlugin  extends ServerPlugin {
    @Description( "Find the shortest path between two nodes under given relationship constraints." )
    @PluginTarget( Node.class )
    public Iterable<Path> constraintedShortestPath(
//    public Iterable<Representation> constraintedShortestPath(
            @Source Node source,
            @Description("The node to find the shortest path to.") @Parameter( name = "target" ) Node target,
            @Description( "The relationship types to follow when searching for the shortest path(s). " +
                    "Order is insignificant, if omitted all types are followed." )
            @Parameter( name = "types", optional = true ) String[] types,
            @Description( "The maximum path length to search for, default value (if omitted) is 4." )
            @Parameter( name = "depth", optional = true ) Integer depth,
            @Description("An object of given constraints")
            @Parameter(name = "constraints") final Map constraints,
            @Description("Default weight of relationships the don't match the set of supplied constraints")
            @Parameter(name = "defaultValue", optional = true) Double defaultValue){
        PathExpander<?> expander;
        List<Path> paths = new ArrayList<Path>();
        List<PropertyContainer> containers = new ArrayList<PropertyContainer>();
        if ( types == null )
        {
            expander = PathExpanders.allTypesAndDirections();
        }
        else
        {
            PathExpanderBuilder expanderBuilder = PathExpanderBuilder.empty();
            for ( int i = 0; i < types.length; i++ )
            {
                expanderBuilder = expanderBuilder.add( DynamicRelationshipType.withName( types[i] ) );
            }
            expander = expanderBuilder.build();
        }
        defaultValue = (null == defaultValue) ? Double.POSITIVE_INFINITY : defaultValue.doubleValue();
        CostEvaluator<Double> costEvaluator = new RelationshipFilter(constraints, defaultValue);
        try {
            Transaction tx = source.getGraphDatabase().beginTx();
            PathFinder<WeightedPath> shortestPath = GraphAlgoFactory.dijkstra(expander, costEvaluator);
            WeightedPath path  = shortestPath.findSinglePath(source, target);
            if (path.weight() == 0) {
                paths.add(path);
            }
            tx.success();
        } catch (Exception e) {

        }
        return paths;
    };
}
