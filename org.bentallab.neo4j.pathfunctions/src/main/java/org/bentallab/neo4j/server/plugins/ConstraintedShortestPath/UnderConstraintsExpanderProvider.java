package org.bentallab.neo4j.server.plugins.ConstraintedShortestPath;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.BranchState;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lab on 28/10/15.
 */

public abstract class UnderConstraintsExpanderProvider {

    public static <STATE> PathExpander<STATE> underConstraintsExpander(final RelationshipType type, final Map constraints) {
        return new PathExpander<STATE> () {

            public Iterable<Relationship> expand( Path path, BranchState<STATE> state )
            {
                ArrayList<Relationship> matchedRelationships = new ArrayList<Relationship>();
                /*if ( path.length() == 0 )
                {
                    for (Relationship rel: path.endNode().getRelationships( type ) ) {
                        if (CheckForConstraints.isRelationshipValid(rel, constraints))
                            matchedRelationships.add(rel);
                    }
                    return matchedRelationships;
                }
                else
                {
                    Direction direction = getDirectionOfLastRelationship( path );
                    for (Relationship rel: path.endNode().getRelationships( direction, type ) ) {
                        if (CheckForConstraints.isRelationshipValid(rel, constraints))
                            matchedRelationships.add(rel);
                    }
                    return matchedRelationships;
                }*/
                for (Relationship rel: path.endNode().getRelationships( type ) ) {
                    if (CheckForConstraints.isRelationshipValid(rel, constraints))
                        matchedRelationships.add(rel);
                }
                return matchedRelationships;
            }

            public PathExpander<STATE> reverse()
            {
                return this;
            }

            private Direction getDirectionOfLastRelationship( Path path )
            {
                assert path.length() > 0;
                Direction direction = Direction.INCOMING;
                if ( path.endNode().equals( path.lastRelationship().getEndNode() ) )
                {
                    direction = Direction.OUTGOING;
                }
                return direction;
            }
        };
    }
}
