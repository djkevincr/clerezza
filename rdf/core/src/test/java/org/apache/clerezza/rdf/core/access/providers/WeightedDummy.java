/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.clerezza.rdf.core.access.providers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.clerezza.commons.rdf.ImmutableGraph;
import org.apache.clerezza.commons.rdf.Graph;
import org.apache.clerezza.commons.rdf.Graph;
import org.apache.clerezza.commons.rdf.IRI;
import org.apache.clerezza.rdf.core.access.EntityAlreadyExistsException;
import org.apache.clerezza.rdf.core.access.EntityUndeletableException;
import org.apache.clerezza.rdf.core.access.NoSuchEntityException;
import org.apache.clerezza.rdf.core.access.WeightedTcProvider;
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleImmutableGraph;
import org.apache.clerezza.commons.rdf.impl.utils.simple.SimpleMGraph;

/**
 *
 * @author mir
 */
public class WeightedDummy implements WeightedTcProvider {

    private Map<IRI, Graph> tripleMap = new HashMap<IRI, Graph>();

    @Override
    public ImmutableGraph createImmutableGraph(IRI name, Graph triples)
            throws EntityAlreadyExistsException {
        if ((name == null) || (name.getUnicodeString() == null)
                || (name.getUnicodeString().trim().length() == 0)) {
            throw new IllegalArgumentException("Name must not be null");
        }

        try {
            // throws NoSuchEntityException if a Graph with that name
            // already exists
            this.getGraph(name);
        } catch (NoSuchEntityException e) {
            ImmutableGraph result;
            if (ImmutableGraph.class.isAssignableFrom(triples.getClass())) {
                result = (ImmutableGraph) triples;
            } else {
                result = new SimpleImmutableGraph(triples);
            }
            tripleMap.put(name, result);

            return result;
        }
        throw new EntityAlreadyExistsException(name);
    }

    @Override
    public Graph createGraph(IRI name) throws EntityAlreadyExistsException {
        if ((name == null) || (name.getUnicodeString() == null)
                || (name.getUnicodeString().trim().length() == 0)) {
            throw new IllegalArgumentException("Name must not be null");
        }

        try {
            // throws NoSuchEntityException if a Graph with that name
            // already exists
            this.getGraph(name);
        } catch (NoSuchEntityException e) {
            Graph result = new SimpleMGraph();
            tripleMap.put(name, result);
            return result;
        }
        throw new EntityAlreadyExistsException(name);
    }

    @Override
    public void deleteGraph(IRI name)
            throws NoSuchEntityException, EntityUndeletableException {
        if (tripleMap.remove(name) == null) {
            throw new NoSuchEntityException(name);
        }
    }

    @Override
    public ImmutableGraph getImmutableGraph(IRI name) throws NoSuchEntityException {
        Graph Graph = tripleMap.get(name);
        if (Graph == null) {
            throw new NoSuchEntityException(name);
        } else if (ImmutableGraph.class.isAssignableFrom(Graph.getClass())) {
            return (ImmutableGraph) Graph;
        }
        throw new NoSuchEntityException(name);
    }

    @Override
    public Graph getMGraph(IRI name) throws NoSuchEntityException {
        Graph Graph = tripleMap.get(name);
        if (Graph == null) {
            throw new NoSuchEntityException(name);
        } else if (Graph.class.isAssignableFrom(Graph.getClass())) {
            return (Graph) Graph;
        }
        throw new NoSuchEntityException(name);
    }

    @Override
    public Set<IRI> getNames(ImmutableGraph ImmutableGraph) {
        throw new UnsupportedOperationException(
                "Not supported yet. equals() has to be implemented first");
    }

    @Override
    public Graph getGraph(IRI name)
            throws NoSuchEntityException {
        Graph Graph = tripleMap.get(name);
        if (Graph == null) {
            throw new NoSuchEntityException(name);
        } else {
            return Graph;
        }
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public Set<IRI> listImmutableGraphs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<IRI> listMGraphs() {
        return new HashSet<IRI>();
    }

    @Override
    public Set<IRI> listGraphs() {
        return listMGraphs();
    }
}
