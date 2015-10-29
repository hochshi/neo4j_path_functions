package org.bentallab.neo4j.server.plugins.ConstraintedShortestPath;

import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lab on 28/10/15.
 */
public class CheckForConstraints {

    public enum Compare {
        LESSTHAN("lt") {
            @Override
            boolean compare(double x, double y) {
                return (Double.compare(x, y) < 0);
            }
        },
        LESSTHANEQUAL("lte") {
            @Override
            boolean compare(double x, double y) {
                return (Double.compare(x, y) <= 0);
            }
        },
        EQUAL("e") {
            @Override
            boolean compare(double x, double y) {
                return (Double.compare(x, y) == 0);
            }
        },
        GREATERTHANEQUAL("gte") {
            @Override
            boolean compare(double x, double y) {
                return (Double.compare(x, y) >= 0);
            }
        },
        GREATERTHAN("gt") {
            @Override
            boolean compare(double x, double y) {
                return (Double.compare(x, y) > 0);
            }
        };

        private final String symbol;
        Compare(String symbol) {this.symbol = symbol;}
        @Override public String toString() { return symbol; }

        // Implementing a fromString method on an enum type
        private static final Map<String, Compare> stringToEnum
                = new HashMap<String, Compare>();
        static { // Initialize map from constant name to enum constant
            for (Compare compare : values())
                stringToEnum.put(compare.toString(), compare);
        }
        // Returns Operation for string, or null if string is invalid
        public static Compare fromString(String symbol) {
            return stringToEnum.get(symbol);
        }

        abstract boolean compare(double x, double y);
    }

    public static Boolean isRelationshipValid(Relationship rel, final Map<String, Object> constraints) {
        for (String key : constraints.keySet()) {
            Object relationshipProp = rel.getProperty(key, null);
            Double relationshipValue;
            if (null == relationshipProp) {
                return false;
            }
            try{
                relationshipValue = ((Number) relationshipProp).doubleValue();
            } catch (ClassCastException toDouble) {
                try {
                    relationshipValue = Double.parseDouble((String) relationshipProp);
                } catch (ClassCastException parseDouble) {
                    return false;
                }
            }

            Map<String, Double> compareMap = (Map<String, Double>) constraints.get(key);
            for (String compareKey : compareMap.keySet()) {
                Compare comparer = Compare.fromString(compareKey);
                if (!comparer.compare(relationshipValue, compareMap.get(compareKey))) {
                    return false;
                }
            }
        }
        return true;
    }
}
