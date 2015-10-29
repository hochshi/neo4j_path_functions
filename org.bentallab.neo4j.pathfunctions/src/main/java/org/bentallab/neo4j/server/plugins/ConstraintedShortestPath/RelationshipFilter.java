package org.bentallab.neo4j.server.plugins.ConstraintedShortestPath;

import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lab on 20/10/15.
 */
public class RelationshipFilter implements CostEvaluator<Double> {

    private Map<String, Object> constraints;
    private final double defaultCost;

    public RelationshipFilter( Map<String,Object> constraints, double defaultCost ) {
        this.constraints = constraints;
        this.defaultCost = defaultCost;
    }

    public Double getCost(Relationship relationship, Direction direction) {
        if (CheckForConstraints.isRelationshipValid(relationship, constraints)) {
            return 0.0;
        } else {
            return defaultCost;
        }
    }
}
